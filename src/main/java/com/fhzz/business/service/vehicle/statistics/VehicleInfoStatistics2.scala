//package com.fhzz.business.service.vehicle.statistics
//
//import java.util.Date
//import com.fhzz.business.entity._
//import com.fhzz.business.service.vehicle.statistics.VehicleInfoStatistics._
//import com.fhzz.core.constants.Constants
//import com.fhzz.core.dao.{HibernateTemplateSupport, DaoTemplate}
//import com.fhzz.core.tools.{StringUtils, JdbcUtils, DateUtil}
//import net.sf.json.JSONObject
//import org.apache.log4j.{PropertyConfigurator, Logger}
//import org.apache.spark.SparkConf
//import org.apache.spark.streaming.kafka.KafkaUtils
//import org.apache.spark.streaming.{Seconds, StreamingContext}
//import org.hibernate.{Session, Transaction}
//import org.springframework.context.support.ClassPathXmlApplicationContext
//import scala.collection.JavaConverters._
//import scala.collection.mutable
//
///**
//  * Created by yqs on 2017/1/4
//  * 车辆信息统计（Spark 主类）
//  */
//class VehicleInfoStatistics
//{
//  private val LOG = Logger.getLogger(getClass())
//
//  private var daoTemplate:DaoTemplate  = null
//
//  /**
//    * 赋值daoTemplate
//    */
//  def loadInitData(): Boolean = {
//    var isOk = false
//    try {
//      val context = new ClassPathXmlApplicationContext("classpath:resources\\spring-context.xml")
//      val obj = context.getBean("daoTemplate")
//
//      val current = new Date()
//      val dataDayFormate  = DateUtil.getDateDayFormat(current)
//      val vfsrSql = "from VehicleFlowStaticsResult where jgday='"+ dataDayFormate + "'"
//      val crSql = "from CarbelongResult where jgday='" + dataDayFormate +  "'"
//
//      daoTemplate = obj.asInstanceOf[DaoTemplate]
//      val mysqlHibernateTemplate: HibernateTemplateSupport = daoTemplate.getMysqlHibernateTemplate()
//      val ve: java.util.List[VehicleFlowStaticsResult] = mysqlHibernateTemplate.find(vfsrSql).asInstanceOf[java.util.List[VehicleFlowStaticsResult]]
//      opLoadedFlowDataToMap(ve)
//      val ca: java.util.List[CarbelongResult] = mysqlHibernateTemplate.find(crSql).asInstanceOf[java.util.List[CarbelongResult]]
//      opLoadedCarBelongDataToMap(ca)
//      isOk = true
//      LOG.info("车流量统计:初始化" + "，加载数据成功")
//    }
//    catch {
//      case e: Exception => LOG.error("车流量统计:初始化失败" + e, e)
//    }
//    isOk
//  }
//
//  /**
//    * 将加载的数据映射到cache map中
//    */
//  def opLoadedFlowDataToMap(vehicleFlowStatisticsVoList: java.util.List[VehicleFlowStaticsResult]) {
//    if (vehicleFlowStatisticsVoList == null)
//      return
//
//    for (ve <- vehicleFlowStatisticsVoList.asScala)
//      mapVehicleFlowToCache(ve)
//  }
//
//  /**
//    * 映射车流量实体到缓存中
//    */
//  def mapVehicleFlowToCache(ve: VehicleFlowStaticsResult) {
//    val id = ve.getId
//    val key = id.getPuid + id.getJgday
//
//    if (!vehicleFlowCache.contains(key)) {
//      vehicleFlowCache.put(key, ve)
//    }
//  }
//
//  /**
//    * 将加载的数据映射到cache map中
//    */
//  def opLoadedCarBelongDataToMap(carbelongResult: java.util.List[CarbelongResult]) {
//    if (carbelongResult == null)
//      return
//
//    for (ca <- carbelongResult.asScala)
//      mapCarBelongCache(ca)
//  }
//
//  /**
//    * 映射车流量实体到缓存中
//    */
//  def mapCarBelongCache(ca: CarbelongResult) {
//    val id = ca.getId
//    val key = id.getCitySeq + id.getPuid + id.getJgday
//    if (!vehicleFlowCache.contains(key)) {
//      carBelongCache.put(key, ca)
//    }
//  }
//
//  def getConsumer() {
//    var ts: Transaction = null
//    var session: Session = null
//    try {
//      val mysqlHibernateTemplate = daoTemplate.getMysqlHibernateTemplate()
//      session = mysqlHibernateTemplate.getOtherSession()
//      ts = session.beginTransaction()
//
//      val zkQuorum: String = "master1:2181"
//      val group: String = "yqstest5"
//      /**
//        * 定义topic
//        */
//      val topics = "kafakTest"
//      val numThreads: Int = 2
//      /**
//        * 初始化SparkContext，批量间隔为5s
//        */
//      val sparkConf = new SparkConf().setAppName("VehicleInfoStatisticsApp").set("spark.serializer", "org.apache.spark.serializer.KryoSerializer")
//      val ssc: StreamingContext = new StreamingContext(sparkConf, Seconds(5))
//      ssc.checkpoint("checkpoint")
//      LOG.info("Starting consumer...")
//
//      // Create direct kafka stream with brokers and topics
//      val topicMap = Map(topics -> numThreads)
//      val rdd = KafkaUtils.createStream(ssc, zkQuorum, group, topicMap).map(_._2)
//
//      /**
//        * 车流量小时统计
//        * TODO
//        */
//      val vehicleFlowDataStatisticsData = rdd.map { msgLine => {
//        val json = JSONObject.fromObject(msgLine)
//        val vehicleFlowData = statisticsFlow(json)
//        vehicleFlowData
//      }
//      }
//
//      /**
//        * 统计市/省归属地的
//        */
//      val carbelongResultData = rdd.map { msgLine => {
//        val json = JSONObject.fromObject(msgLine)
//        val carBelongData = statisticsCarBelong(json)
//        carBelongData
//      }
//      }
//
//      /**
//        * 车流量计算与统计
//        */
//      val vehicleFlowDataResult = vehicleFlowDataStatisticsData.map(v => (v.getId, 1L)).reduceByKey(_ + _)
//
//      /**
//        * 车归属地统计
//        */
//      val carbelongCityResult = carbelongResultData.map(v => (v(0).getId, 1L)).reduceByKey(_ + _)
//
//      /**
//        * 车归属地统计
//        */
//      val carbelongProvicedResult = carbelongResultData.map(v => (v(1).getId, 1L)).reduceByKey(_ + _)
//
//
//      /**
//        *保存车流量统计结果到mysql中
//        */
//      vehicleFlowDataResult.foreachRDD(rdd => {
//        rdd.map( iter => {
//          session.saveOrUpdate(iter)
//        })
//      }
//      )
//
//      /**
//        * 保存车归属市统计结果到mysql
//        */
//      carbelongCityResult.foreachRDD(rdd => {
//        rdd.map( iter => {
//          session.saveOrUpdate(iter)
//        })
//      }
//      )
//
//      /**
//        * 保存车归属省统计结果到mysql
//        */
//      carbelongProvicedResult.foreachRDD(rdd => {
//        rdd.map( iter => {
//          session.saveOrUpdate(iter)
//        })
//      }
//      )
//
//      session.flush()
//      ts.commit()
//
//      ssc.start()
//      ssc.awaitTermination()
//    }
//    catch {
//      case e: Exception => LOG.error("车流量消费 getConsumer:失败" + e, e)
//    }
//    finally {
//      session.close()
//    }
//  }
//
//
//  /**
//    * 统计小时车流量
//    */
//  def statisticsFlow(json: JSONObject) = {
//    val ve = new VehicleFlowStaticsResult()
//    try {
//      val puid = json.getString("PUID")
//      val jgsk = json.getString("JGSK")
//
//      /**
//        * 异常数据不统计
//        */
//      if (jgsk.length >= 10) {
//        val jgday = jgsk.substring(0, 10)
//        val jgskHour = getJgskHour(jgday)
//        val id = new VehicleFlowStaticsResultId(puid, DateUtil.parseDateDayFormat(jgday))
//        ve.setId(id)
//        setVehicleFlowHour(ve, jgskHour)
//      }
//    }
//    catch {
//      case e: Exception => LOG.error("车流量统计:初始化失败" + e, e)
//    }
//    ve
//  }
//
//  /**
//    * 设置小时字段
//    */
//  def setVehicleFlowHour(ve: VehicleFlowStaticsResult, hour: Int) {
//    val i = hour
//    i match {
//      case 0 => ve.setZeroOclock(1)
//      case 1 => ve.setOneOclock(1)
//      case 2 => ve.setTwoOclock(1)
//      case 3 => ve.setThreeOclock(1)
//      case 4 => ve.setFourOclock(1)
//      case 5 => ve.setFiveOclock(1)
//      case 6 => ve.setSixOclock(1)
//      case 7 => ve.setSevenOclock(1)
//      case 8 => ve.setEightOclock(1)
//      case 9 => ve.setNineOclock(1)
//      case 10 => ve.setTenOclock(1)
//      case 11 => ve.setElevenOclock(1)
//      case 12 => ve.setTwelveOclock(1)
//      case 13 => ve.setThirteenOclock(1)
//      case 14 => ve.setFourteenOclock(1)
//      case 15 => ve.setFifteenOclock(1)
//      case 16 => ve.setSixteenOclock(1)
//      case 17 => ve.setSeventeenOclock(1)
//      case 18 => ve.setEighteenOclock(1)
//      case 19 => ve.setNineteenOclock(1)
//      case 20 => ve.setTwentyOclock(1)
//      case 21 => ve.setTwentyOneOclock(1)
//      case 22 => ve.setTwentyTwoOclock(1)
//      case 23 => ve.setTwentyThreeOclock(1)
//      case _ => LOG.error("time formate error")
//    }
//  }
//
//  /**
//    * 获取jgsk小时部分
//    */
//  def getJgskHour(jgsk: String): Integer = {
//    val date = DateUtil.parseDate(jgsk)
//    val hour = DateUtil.getHour(date)
//    hour
//  }
//
//  def statisticsCarBelong(json: JSONObject) = {
//
//    val cityResult = new CarbelongResult()
//    val provinceResult = new CarbelongResult()
//    try {
//      /**
//        * 设备编码、经过时间、车牌号码,卡口编号
//        */
//      val puid = json.getString("PUID")
//      val jgsk = json.getString("JGSK")
//      val hphm = json.getString("HPHM")
//      val hphm1 = if (hphm.length() > 2) hphm.substring(0, 2).toLowerCase() else "未知"
//      val jgday = jgsk.substring(0, 10)
//      var map = cityOfNationMap.get(hphm1)
//
//      if (map == null) {
//        val abbreviation = getAbbreviation(hphm1.substring(0, 1), hphm1)
//        map = if (!StringUtils.isDigit(abbreviation)) cityKeyOfNationMap.get(abbreviation) else map
//      }
//
//      var citySeq: String = null
//      var provinceId: String = null
//      try {
//        citySeq = map.get(Constants.SEQUENCE_NAME)
//        provinceId = map.get(Constants.PROVINCE_ID)
//      }
//      catch {
//        case e: Exception => citySeq = "-1"
//          provinceId = "-1"
//      }
//
//      /**
//        * 统计市
//        */
//      val id = new CarbelongResultId()
//      id.setCitySeq(citySeq)
//      id.setPuid(puid)
//      id.setJgday(DateUtil.parseDateDayFormat(jgday))
//      cityResult.setId(id)
//      cityResult.setProvinceid(Integer.parseInt(provinceId))
//
//      /**
//        * 统计省
//        */
//      val idProvince = new CarbelongResultId()
//      idProvince.setCitySeq(provinceId)
//      idProvince.setPuid(puid)
//      idProvince.setJgday(DateUtil.parseDateDayFormat(jgday))
//
//      provinceResult.setId(idProvince)
//      provinceResult.setProvinceid(Integer.parseInt(provinceId))
//    }
//    catch {
//      case e: Exception => LOG.error("车辆归属地统计: 统计数据格式错误," + json.toString() + e.getMessage(), e)
//    }
//    val s = Array[CarbelongResult](cityResult,provinceResult)
//    s
//  }
//
//  /**
//    * 获取简称
//    */
//  def getAbbreviation(abbreviation: String, hphm1: String): String = {
//    val name = if (hphm1.equals("军v") || hphm1.equals("空k") || hphm1.equals("海h")
//      || hphm1.equals("北b") || hphm1.equals("沈s")
//      || hphm1.equals("兰l") || hphm1.equals("南n")
//      || hphm1.equals("广g") || hphm1.equals("成c")) "未知"
//    else abbreviation
//    name
//  }
//
//  /**
//    * 获取非空int
//    */
//  def getNoNullInteger(i: Int) = {
//    val s = if (String.valueOf(i) == null || String.valueOf(i) == "") 0 else i
//    s
//  }
//}
//
//object VehicleInfoStatistics {
//
//  /**
//    * 设置log信息配置文件
//    */
//  PropertyConfigurator.configure(System.getProperty("user.dir") + "/src/config/log4j.properties")
//
//  private val LOG = Logger.getLogger(getClass())
//
//  /**
//    * 车流量小时统计Map缓存扩展
//    */
//  var vehicleFlowCache = new mutable.HashMap[String, VehicleFlowStaticsResult]()
//
//  /**
//    * 车辆归属地统计Map缓存扩展
//    */
//  var carBelongCache = new mutable.HashMap[String, CarbelongResult]()
//
//
//  /**
//    * 全国车牌号 hphm1 --省市组成的映射
//    */
//  private val cityOfNationMap = JdbcUtils.queryAttribute()
//
//  /**
//    * 全国简称 --省市组成的映射
//    */
//  private val cityKeyOfNationMap = JdbcUtils.getUnknownMap()
//
//  def main(args: Array[String]): Unit = {
//    try {
//      val v : VehicleInfoStatistics = new VehicleInfoStatistics()
//      v.loadInitData()
//      v.getConsumer()
//    }
//    catch {
//      case e: Exception => e.printStackTrace()
//    }
//  }
//
//}
