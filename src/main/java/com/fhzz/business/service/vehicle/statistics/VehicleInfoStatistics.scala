package com.fhzz.business.service.vehicle.statistics

import java.sql.{Statement, DriverManager, Connection}
import java.util
import java.util.Date
import _root_.kafka.serializer.StringDecoder
import com.fhzz.business.entity._
import com.fhzz.business.service.vehicle.statistics.VehicleInfoStatistics._
import com.fhzz.core.constants.Constants
import com.fhzz.core.dao.{HibernateTemplateSupport, DaoTemplate}
import com.fhzz.core.tools.{StringUtils, JdbcUtils, DateUtil}
import net.sf.json.JSONObject
import org.apache.log4j.{Logger, PropertyConfigurator}
import org.apache.spark.{HashPartitioner, SparkConf}
import org.apache.spark.streaming._
import org.apache.spark.streaming.kafka.KafkaUtils
import org.springframework.context.support.ClassPathXmlApplicationContext
import scala.collection.JavaConverters._
import scala.collection.mutable
import scala.collection.mutable.ArrayBuffer

/**
  * Created by yqs on 2017/1/4
  * 车辆信息统计（Spark 主类）
  */
class VehicleInfoStatistics extends Serializable {

  /**
    * 车流量小时统计Map缓存扩展
    */
  var vehicleFlowCache = new scala.collection.mutable.HashMap[VehicleFlowStaticsResultId, VehicleFlowStaticsResult]()

  /**
    * 车辆归属地统计Map缓存扩展
    */
  var carBelongCache = new scala.collection.mutable.HashMap[CarbelongResultId, CarbelongResult]()

  /**
    * 全国车牌号 hphm1 --省市组成的映射
    */
  var cityOfNationMap = new util.HashMap[String, util.HashMap[String, String]]

  /**
    * 全国简称 --省市组成的映射
    */
  var cityKeyOfNationMap = new util.HashMap[String, util.HashMap[String, String]]

  /**
    * 实时流量清理
    */
  var clearDataCount = 100

  /**
    * 实时流量计算
    */
  var dateTimesCount = 0


  def this(cityOfNationMap: util.HashMap[String, util.HashMap[String, String]], cityKeyOfNationMap: util.HashMap[String, util.HashMap[String, String]]) {
    this()
    this.cityOfNationMap = cityOfNationMap
    this.cityKeyOfNationMap = cityKeyOfNationMap
    loadInitData
  }

  /**
    * 初始化数据
    * 赋值daoTemplate
    */
  def loadInitData() = {
    var daoTemplate: DaoTemplate = null
    try {
      val context = new ClassPathXmlApplicationContext("classpath:resources/spring-context.xml")
      val obj = context.getBean("daoTemplate")
      daoTemplate = obj.asInstanceOf[DaoTemplate]
      val mysqlHibernateTemplate: HibernateTemplateSupport = daoTemplate.getMysqlHibernateTemplate()

      val current = new Date()
      val dateDayFormat = DateUtil.getDateDayFormat(current)
      val vehicleFlowStaticsSql = "from VehicleFlowStaticsResult where jgday='"+dateDayFormat+"'"

      val ve: java.util.List[VehicleFlowStaticsResult] = mysqlHibernateTemplate.find(vehicleFlowStaticsSql).asInstanceOf[java.util.List[VehicleFlowStaticsResult]]
      opLoadedFlowDataToMap(ve)

      val carbelongResultSql = "from CarbelongResult where jgday='"+dateDayFormat+"'"
      val ca: java.util.List[CarbelongResult] = mysqlHibernateTemplate.find(carbelongResultSql).asInstanceOf[java.util.List[CarbelongResult]]
      opLoadedCarBelongDataToMap(ca)
      LOG.info("车流量统计:初始化" + "，加载数据成功")
    }
    catch {
      case e: Exception => LOG.error("车流量统计:初始化失败" + e, e)
    }
  }

  def getSparkContext(): StreamingContext = {
    /**
      * 初始化SparkContext，批量间隔为5s
      */
    val sparkConf = new SparkConf().setAppName("VehicleInfoStatisticsApp").set("spark.serializer", "org.apache.spark.serializer.KryoSerializer")
      .set("spark.rpc.netty.dispatcher.numThreads", "2").set("spark.executor.cores", "3")
      //.set("spark.executor.memory", "5G")
    val ssc: StreamingContext = new StreamingContext(sparkConf, Seconds(5))
    ssc.checkpoint("checkpoint")
    LOG.info("Starting consumer...")
    ssc
  }

  def initRDD() = {
    val vehicleRdd = mapToVehiclelist(vehicleFlowCache)
    val carbelongRDD = mapToCarbelonglist(carBelongCache)
    (vehicleRdd, carbelongRDD)
  }

  /**
    * map 转为 车小时流量list
    */
  def mapToVehiclelist(map: scala.collection.mutable.HashMap[VehicleFlowStaticsResultId, VehicleFlowStaticsResult]) = {
    val iter = map.keysIterator
    val list = new mutable.ListBuffer[(VehicleFlowStaticsResultId, VehicleFlowStaticsResult)]()
    while (iter.hasNext) {
      val key = iter.next
      val value = map(key)
      list.+=((key, value))
    }
    list
  }

  /**
    * map 转为车归属地list
    */
  def mapToCarbelonglist(map: scala.collection.mutable.HashMap[CarbelongResultId, CarbelongResult]) = {
    val iter = map.keysIterator
    val list = mutable.ListBuffer[(CarbelongResultId, CarbelongResult)]()
    while (iter.hasNext) {
      val key = iter.next()
      val value = map(key)
      list.+=((key, value))
    }
    list
  }

  /**
    * 获取行RDD数据
    */
  def getLineRDD(ssc: StreamingContext) = {
    val zkQuorum: String = "master1:2181"
    val group: String = "yqs"
    val topics = Set("yqsTest")
    val brokers = "master1:9092"
    val kafkaParams = Map[String, String]("bootstrap.servers" -> brokers, "serializer.class" -> "kafka.serializer.StringEncoder",
      "auto.offset.reset" -> "smallest", "zookeeper.connect" -> zkQuorum, "group.id" -> group)
    val rdd = KafkaUtils.createDirectStream[String, String, StringDecoder, StringDecoder](ssc, kafkaParams, topics).map(_._2)
    rdd
  }

  /**
    * 将加载的数据映射到cache map中
    */
  def opLoadedFlowDataToMap(vehicleFlowStatisticsVoList: java.util.List[VehicleFlowStaticsResult]) {
    if (vehicleFlowStatisticsVoList == null)
      return

    for (ve <- vehicleFlowStatisticsVoList.asScala)
      mapVehicleFlowToCache(ve)
  }

  /**
    * 映射车流量实体到缓存中
    */
  def mapVehicleFlowToCache(ve: VehicleFlowStaticsResult) {
    val id = ve.getId
    if (!vehicleFlowCache.contains(id)) {
      vehicleFlowCache.put(id, ve)
    }
  }

  /**
    * 将加载的数据映射到cache map中
    */
  def opLoadedCarBelongDataToMap(carbelongResult: java.util.List[CarbelongResult]) {
    if (carbelongResult == null)
      return

    for (ca <- carbelongResult.asScala)
      mapCarBelongCache(ca)
  }

  /**
    * 映射车流量实体到缓存中
    */
  def mapCarBelongCache(ca: CarbelongResult) {
    val id = ca.getId
    if (!carBelongCache.contains(id)) {
      carBelongCache.put(id, ca)
    }
  }

  /**
    * 车流量小时更新
    */
  def updateVehicleFlowStatics(curVal: Seq[VehicleFlowStaticsResult], preVal: Option[VehicleFlowStaticsResult]) = {
    if (curVal == null || curVal.size == 0) {
      Some(preVal.getOrElse(null))
    }
    else {
      var one = new VehicleFlowStaticsResult()
      curVal.foreach(x => {
        one = setTotalNums(one, x)
      })
      val pre = preVal.getOrElse(null)
      if (pre == null) {
        Some(one)
      }
      else {
        one = setTotalNums(one, pre)
        Some(one)
      }
    }
  }

  /**
    * 更新车辆城市归属地
    */
  def updateCarbelongCityStatics(curVal: Seq[CarbelongResult], preVal: Option[CarbelongResult]) = {
    if (curVal == null || curVal.size == 0) {
      Some(preVal.getOrElse(null))
    }
    else {
      var one = new CarbelongResult
      curVal.foreach(x => {
        one.setTotalNums(one.getTotalNums + x.getTotalNums)
        one.setProvinceid(x.getProvinceid)
      })
      val pre = preVal.getOrElse(null)
      if (pre == null) {
        Some(one)
      }
      else {
        one = sumTotalNums(one, pre)
        Some(one)
      }
    }
  }

  def sumTotalNums(one: CarbelongResult, two: CarbelongResult) = {
    one.setTotalNums(one.getTotalNums + two.getTotalNums)
    one
  }

  def setTotalNums(one: VehicleFlowStaticsResult, two: VehicleFlowStaticsResult) = {
    one.setTotalNums(one.getTotalNums + two.getTotalNums)
    one.setZeroOclock(one.getZeroOclock + two.getZeroOclock)
    one.setOneOclock(one.getOneOclock + two.getOneOclock)
    one.setTwoOclock(one.getTwoOclock + two.getTwoOclock)
    one.setThreeOclock(one.getThreeOclock + two.getThreeOclock)
    one.setFourOclock(one.getFourOclock + two.getFourOclock)
    one.setFiveOclock(one.getFiveOclock + two.getFiveOclock)
    one.setSixOclock(one.getSixOclock + two.getSixOclock)
    one.setSevenOclock(one.getSevenOclock + two.getSevenOclock)
    one.setEightOclock(one.getEightOclock + two.getEightOclock)
    one.setNineOclock(one.getNineOclock + two.getNineOclock)
    one.setTenOclock(one.getTenOclock + two.getTenOclock)
    one.setElevenOclock(one.getElevenOclock + two.getElevenOclock)
    one.setTwelveOclock(one.getTwelveOclock + two.getTwelveOclock)
    one.setThirteenOclock(one.getThirteenOclock + two.getThirteenOclock)
    one.setFourteenOclock(one.getFourteenOclock + two.getFourteenOclock)
    one.setFifteenOclock(one.getFifteenOclock + two.getFifteenOclock)
    one.setSixteenOclock(one.getSixteenOclock + two.getSixteenOclock)
    one.setSeventeenOclock(one.getSeventeenOclock + two.getSeventeenOclock)
    one.setEighteenOclock(one.getEighteenOclock + two.getEighteenOclock)
    one.setNineteenOclock(one.getNineteenOclock + two.getNineteenOclock)
    one.setTwentyOclock(one.getTwentyOclock + two.getTwentyOclock)
    one.setTwentyOneOclock(one.getTwentyOneOclock + two.getTwentyOneOclock)
    one.setTwentyTwoOclock(one.getTwentyTwoOclock + two.getTwentyTwoOclock)
    one.setTwentyThreeOclock(one.getTwentyThreeOclock + two.getTwentyThreeOclock)
    one
  }


  def getConsumer() {
    try {
      val ssc: StreamingContext = getSparkContext
      val rdd = getLineRDD(ssc)
      LOG.info("Coming data in this interva...")
      val initVehicleFlowRdd = ssc.sparkContext.parallelize(initRDD._1)
      val initCarbelongRdd = ssc.sparkContext.parallelize(initRDD._2)

      /**
        * 更新车流量
        */
      val updateVehicleFlowSate = (curVal: Seq[VehicleFlowStaticsResult], preVal: Option[VehicleFlowStaticsResult]) => {
        updateVehicleFlowStatics(curVal, preVal)
      }

      /**
        * 更新车城市归属地
        */
      val updateCarbelongData = (curVal: Seq[CarbelongResult], preVal: Option[CarbelongResult]) => {
        updateCarbelongCityStatics(curVal, preVal)
      }


      /**
        * 车流量小时统计
        */
      val vehicleFlowData = rdd.map(msgLine => {
        val json = JSONObject.fromObject(msgLine)
        val vehicleFlowData = statisticsFlow(json)
        (vehicleFlowData.getId, vehicleFlowData)
      }
      )

      /**
        * 统计市归属地的
        */
      val carbelongCityResultData = rdd.map { msgLine => {
        val json = JSONObject.fromObject(msgLine)
        val carBelongData = statisticsCarBelong(json, true)
        (carBelongData.getId, carBelongData)
      }
      }

      /**
        * 统计省归属地的
        */
      val carbelongProvinceResultData = rdd.map { msgLine => {
        val json = JSONObject.fromObject(msgLine)
        val carBelongData = statisticsCarBelong(json, false)
        (carBelongData.getId, carBelongData)
      }
      }

      /**
        * 车流量计算与统计
        */
      vehicleFlowData.updateStateByKey(updateVehicleFlowSate, new HashPartitioner(ssc.sparkContext.defaultParallelism), initVehicleFlowRdd).foreachRDD(
        rdd => {
          val data = rdd.collect()
          var tranData = mutable.Map[VehicleFlowStaticsResultId, VehicleFlowStaticsResult]()
          data.foreach(x => {
            tranData += ((x._1, x._2))
          })
          saveVehicleFlowHour(tranData)
        })


      /**
        * 车归属市统计
        */
      carbelongCityResultData.updateStateByKey(updateCarbelongData, new HashPartitioner(ssc.sparkContext.defaultParallelism), initCarbelongRdd)
        .foreachRDD(
          rdd => {
            val data = rdd.collect()
            var tranData = mutable.Map[CarbelongResultId, CarbelongResult]()
            data.foreach(x => {
              tranData += ((x._1, x._2))
            })
            saveCarBelong(tranData)
          })

      /**
        * 车归属省统计
        */
      carbelongProvinceResultData.updateStateByKey(updateCarbelongData, new HashPartitioner(ssc.sparkContext.defaultParallelism), initCarbelongRdd)
        .foreachRDD(
          rdd => {
            val data = rdd.collect()
            var tranData = mutable.Map[CarbelongResultId, CarbelongResult]()
            data.foreach(x => {
              tranData += ((x._1, x._2))
            })
            saveCarBelong(tranData)
          })
      ssc.start()
      ssc.awaitTermination()
    }
    catch {
      case e: Exception => LOG.error("车流量消费 getConsumer:失败" + e, e)
    }
  }

  /**
    * 统计小时车流量
    */
  def statisticsFlow(json: JSONObject) = {
    val ve = new VehicleFlowStaticsResult()
    try {
      val puid = json.getString("PUID")
      val jgsk = json.getString("JGSK")

      /**
        * 异常数据不统计
        */
      if (jgsk.length >= 10) {
        val jgday = jgsk.substring(0, 10)
        val jgskHour = getJgskHour(jgsk)
        val id = new VehicleFlowStaticsResultId(puid, DateUtil.parseDateDayFormat(jgday))
        ve.setId(id)
        setVehicleFlowHour(ve, jgskHour)
      }
    }
    catch {
      case e: Exception => LOG.error("车流量统计:初始化失败" + e, e)
    }
    ve
  }




  /**
    * 设置小时字段
    */
  def setVehicleFlowHour(ve: VehicleFlowStaticsResult, hour: Int) {
    val i = hour
    i match {
      case 0 => ve.setZeroOclock(1)
      case 1 => ve.setOneOclock(1)
      case 2 => ve.setTwoOclock(1)
      case 3 => ve.setThreeOclock(1)
      case 4 => ve.setFourOclock(1)
      case 5 => ve.setFiveOclock(1)
      case 6 => ve.setSixOclock(1)
      case 7 => ve.setSevenOclock(1)
      case 8 => ve.setEightOclock(1)
      case 9 => ve.setNineOclock(1)
      case 10 => ve.setTenOclock(1)
      case 11 => ve.setElevenOclock(1)
      case 12 => ve.setTwelveOclock(1)
      case 13 => ve.setThirteenOclock(1)
      case 14 => ve.setFourteenOclock(1)
      case 15 => ve.setFifteenOclock(1)
      case 16 => ve.setSixteenOclock(1)
      case 17 => ve.setSeventeenOclock(1)
      case 18 => ve.setEighteenOclock(1)
      case 19 => ve.setNineteenOclock(1)
      case 20 => ve.setTwentyOclock(1)
      case 21 => ve.setTwentyOneOclock(1)
      case 22 => ve.setTwentyTwoOclock(1)
      case 23 => ve.setTwentyThreeOclock(1)
      case _ =>
    }
    ve.setTotalNums(ve.getTotalNums() + 1)
  }

  /**
    * 获取jgsk小时部分
    */
  def getJgskHour(jgsk: String): Integer = {
    val date = DateUtil.parseDate(jgsk)
    val hour = DateUtil.getHour(date)
    hour
  }

  def statisticsCarBelong(json: JSONObject, falg: Boolean) = {
    val cityResult = new CarbelongResult()
    val provinceResult = new CarbelongResult()
    try {
      /**
        * 设备编码、经过时间、车牌号码,卡口编号
        */
      val puid = json.getString("PUID")
      val jgsk = json.getString("JGSK")
      val hphm = json.getString("HPHM")
      val hphm1 = if (hphm.length() > 2) hphm.substring(0, 2).toLowerCase() else "未知"
      val jgday = jgsk.substring(0, 10)
      var map = cityOfNationMap.get(hphm1)

      if (map == null) {
        val abbreviation = getAbbreviation(hphm1.substring(0, 1), hphm1)
        map = if (!StringUtils.isDigit(abbreviation)) cityKeyOfNationMap.get(abbreviation) else map
      }

      var citySeq: String = null
      var provinceId: String = null
      try {
        citySeq = map.get(Constants.SEQUENCE_NAME)
        provinceId = map.get(Constants.PROVINCE_ID)

      }
      catch {
        case e: Exception => citySeq = "-1"
          provinceId = "-1"
      }

      /**
        * 统计市
        */
      val id = new CarbelongResultId()
      id.setCitySeq(citySeq)
      id.setPuid(puid)
      id.setJgday(DateUtil.parseDateDayFormat(jgday))
      cityResult.setProvinceid(Integer.parseInt(provinceId))
      cityResult.setId(id)
      cityResult.setTotalNums(cityResult.getTotalNums + 1)

      /**
        * 统计省
        */
      val idProvince = new CarbelongResultId()
      idProvince.setCitySeq(provinceId)
      idProvince.setPuid(puid)
      idProvince.setJgday(DateUtil.parseDateDayFormat(jgday))

      provinceResult.setProvinceid(Integer.parseInt(provinceId))
      provinceResult.setId(idProvince)
      provinceResult.setTotalNums(provinceResult.getTotalNums + 1)
    }
    catch {
      case e: Exception => LOG.error("车辆归属地统计: 统计数据格式错误," + json.toString() + e.getMessage(), e)
    }
    val s = if (falg) cityResult else provinceResult
    s
  }

  /**
    * 获取简称
    */
  def getAbbreviation(abbreviation: String, hphm1: String): String = {
    val name = if (hphm1.equals("军v") || hphm1.equals("空k") || hphm1.equals("海h")
      || hphm1.equals("北b") || hphm1.equals("沈s")
      || hphm1.equals("兰l") || hphm1.equals("南n")
      || hphm1.equals("广g") || hphm1.equals("成c")) "未知"
    else abbreviation
    name
  }

  /**
    * 获取非空int
    */
  def getNoNullInteger(i: Int) = {
    val s = if (String.valueOf(i) == null || String.valueOf(i) == "") 0 else i
    s
  }



  /**
    * 清理缓存数据
    */
  def clearCache() {
    // 清理过期缓存
    dateTimesCount += 1
    if(clearDataCount <= dateTimesCount){
      dateTimesCount = 0
      val curJgDay = DateUtil.getDateDayFormat(new Date())


      /**
        * 清理小时流量
        */
      val keySet = vehicleFlowCache.keySet
      var needToDelete =  ArrayBuffer[VehicleFlowStaticsResultId]()
      for(key <- keySet) {
        var jgday = key.getJgday.toString
        jgday = jgday.substring(jgday.length - 10)
        if (StringUtils.isSmallThan(jgday, curJgDay)) needToDelete += key
      }
      for (key <- needToDelete) vehicleFlowCache.remove(key)

        /**
          * 清理归属地
          */
        val keySetCarBelong = carBelongCache.keySet
        var needToDeleteCarBelong = ArrayBuffer[CarbelongResultId]()
        for(key <- keySetCarBelong) {
          var jgday = key.getJgday.toString
          jgday = jgday.substring(jgday.length -10)
          if(StringUtils.isSmallThan(jgday,curJgDay)) needToDeleteCarBelong += key
          }
          for (key <- needToDeleteCarBelong) carBelongCache.remove(key)
      }
    }


  /**
    * 保存小时流量信息
    * @param datas
    */
  def saveVehicleFlowHour(datas: mutable.Map[VehicleFlowStaticsResultId, VehicleFlowStaticsResult]) {
    var con: Connection = null
    var st: Statement = null
    try {
      Class.forName("com.mysql.jdbc.Driver")
      val uri = "jdbc:mysql://10.3.100.9:3306/vehicle?useUnicode=true&characterEncoding=UTF-8"
      val user = "root"
      val password = "123456"
      con = DriverManager.getConnection(uri, user, password)
      st = con.createStatement()
      for (x <- datas) {
        val puid = x._1.getPuid
        val jgday = new java.sql.Date(x._1.getJgday.getTime)
        val totalNums = x._2.getTotalNums
        val zeroOclock = x._2.getZeroOclock
        val oneOclock = x._2.getOneOclock
        val twoOclock = x._2.getTwoOclock
        val threeOclock = x._2.getThreeOclock
        val fourOclock = x._2.getFourOclock
        val fiveOclock = x._2.getFiveOclock
        val sixOclock = x._2.getSixOclock
        val sevenOclock = x._2.getSevenOclock
        val eightOclock = x._2.getEightOclock
        val nineOclock = x._2.getNineOclock
        val tenOclock = x._2.getTenOclock
        val elevenOclock = x._2.getElevenOclock
        val twelveOclock = x._2.getTwelveOclock
        val thirteenOclock = x._2.getThirteenOclock
        val fourteenOclock = x._2.getFourteenOclock
        val fifteenOclock = x._2.getFifteenOclock
        val sixteenOclock = x._2.getSixteenOclock
        val seventeenOclock = x._2.getSixteenOclock
        val eighteenOclock = x._2.getEighteenOclock
        val nineteenOclock = x._2.getNineteenOclock
        val twentyOclock = x._2.getTwentyOclock
        val twentyOneOclock = x._2.getTwentyOneOclock
        val twentyTwoOclock = x._2.getTwentyTwoOclock
        val twentyThreeOclock = x._2.getTwentyThreeOclock
        val sql = "replace INTO `vehicle_flow_statistics_result` VALUES ('" + puid + "', " +
          "'" + jgday + "', '" + totalNums + "', '" + zeroOclock + "', '" + oneOclock + "', '" + twoOclock + "', " +
          "'" + threeOclock + "','" + fourOclock + "', '" + fiveOclock + "', '" + sixOclock + "', '" + sevenOclock + "', " +
          "'" + eightOclock + "', '" + nineOclock + "', '" + tenOclock + "','" + elevenOclock + "','" + twelveOclock + "', " +
          "'" + thirteenOclock + "','" + fourteenOclock + "','" + fifteenOclock + "', '" + sixteenOclock + "', '" + seventeenOclock + "'," +
          " '" + eighteenOclock + "', '" + nineteenOclock + "', '" + twentyOclock + "', '" + twentyOneOclock + "', '" + twentyTwoOclock + "', " +
          "'" + twentyThreeOclock + "');"
        st.addBatch(sql)
      }
      st.executeBatch()
    }
    catch {
      case e: Exception => LOG.error("车辆归属地统计: 保存到Mysql失败：" + e.getMessage(), e)
    }
    finally {
      JdbcUtils.close(con, st, null)
    }
  }

  /**
    * 保存车辆归属地
    * @param datas
    */
  def saveCarBelong(datas: mutable.Map[CarbelongResultId, CarbelongResult]) {
    var con: Connection = null
    var st: Statement = null
    try {
      Class.forName("com.mysql.jdbc.Driver")
      val uri = "jdbc:mysql://10.3.100.9:3306/vehicle?useUnicode=true&characterEncoding=UTF-8"
      val user = "root"
      val password = "123456"
      con = DriverManager.getConnection(uri, user, password)
      st = con.createStatement()
      for (x <- datas) {
        val puid = x._1.getPuid
        val citySeq = x._1.getCitySeq
        val jgday = new java.sql.Date(x._1.getJgday.getTime)
        val totalNums = x._2.getTotalNums
        val provinceId = x._2.getProvinceid

        val sql = "replace INTO `carbelong_result` VALUES ('" + citySeq + "', '" + jgday + "', '" + puid + "', '" + totalNums + "', '" + provinceId + "');"
        st.addBatch(sql)
      }
      st.executeBatch()
    }
    catch {
      case e: Exception => LOG.error("车辆归属地统计: 保存到Mysql失败：" + e.getMessage(), e)
    }
    finally {
      JdbcUtils.close(con, st, null)
    }
  }

}

object VehicleInfoStatistics {
  /**
    * 设置log信息配置文件
    */
  PropertyConfigurator.configure(System.getProperty("user.dir") + "/src/config/log4j.properties")

  private val LOG = Logger.getLogger(getClass())

  def main(args: Array[String]): Unit = {
    try {
      /**
        * 全国车牌号 hphm1 --省市组成的映射
        */
      val cityOfNationMap = JdbcUtils.queryAttribute()

      /**
        * 全国简称 --省市组成的映射
        */
      val cityKeyOfNationMap = JdbcUtils.getUnknownMap()
      val v: VehicleInfoStatistics = new VehicleInfoStatistics(cityOfNationMap, cityKeyOfNationMap)
      v.getConsumer
    }
    catch {
      case e: Exception => e.printStackTrace()
    }
  }
}
