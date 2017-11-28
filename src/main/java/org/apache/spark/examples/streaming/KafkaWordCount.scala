//package org.apache.spark.examples.streaming
//
//import java.util.HashMap
//import com.fhzz.business.entity.{VehicleFlowStaticsResultId, VehicleFlowStaticsResult}
//import com.fhzz.core.kafka.JdctxConstants
//import com.fhzz.core.tools.DateUtil
//import net.sf.json.JSONObject
//import org.apache.kafka.clients.producer.{KafkaProducer, ProducerConfig, ProducerRecord}
//import org.apache.log4j.{PropertyConfigurator, Logger}
//import org.apache.spark.{HashPartitioner, SparkConf}
//import org.apache.spark.streaming._
//import org.apache.spark.streaming.kafka._
//
///**
//  * Consumes messages from one or more topics in Kafka and does wordcount.
//  * Usage: KafkaWordCount <zkQuorum> <group> <topics> <numThreads>
//  * <zkQuorum> is a list of one or more zookeeper servers that make quorum
//  * <group> is the name of kafka consumer group
//  * <topics> is a list of one or more kafka topics to consume from
//  * <numThreads> is the number of threads the kafka consumer should use
//  *
//  * Example:
//  * `$ bin/run-example \
//  * org.apache.spark.examples.streaming.KafkaWordCount zoo01,zoo02,zoo03 \
//  * my-consumer-group topic1,topic2 1`
//  */
//object KafkaWordCount {
//
//  /**
//    * 设置log信息配置文件
//    */
//  PropertyConfigurator.configure(System.getProperty("user.dir") + "/src/config/log4j.properties")
//
//  private val LOG = Logger.getLogger(getClass())
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
//        val jgskHour = getJgskHour(jgsk)
//        val id = new VehicleFlowStaticsResultId(puid, DateUtil.parseDateDayFormat(jgday))
//        ve.setId(id)
//        setVehicleFlowHour(ve, jgskHour)
//        ve.setTotalNums(1)
//      }
//    }
//    catch {
//      case e: Exception => LOG.error("车流量统计:初始化失败" + e, e)
//    }
//    ve
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
//      case _ => LOG.error("time format error")
//    }
//  }
//
//  def main(args: Array[String]) {
//    if (args.length < 4) {
//      System.err.println("Usage: KafkaWordCount <zkQuorum> <group> <topics> <numThreads>")
//      System.exit(1)
//    }
//    StreamingExamples.setStreamingLogLevels()
//
//    val Array(zkQuorum, group, topics, numThreads) = args
//    val sparkConf = new SparkConf().setAppName("KafkaWordCount")
//    val ssc = new StreamingContext(sparkConf, Seconds(2))
//    val processingInterval = Seconds(5)
//
//    ssc.checkpoint("checkpoint")
//    val topicMap = topics.split(",").map((_, numThreads.toInt)).toMap
//    val lines = KafkaUtils.createStream(ssc, zkQuorum, group, topicMap).map(_._2)
//
//    val words = lines.map { msgLine => {
//      val json = JSONObject.fromObject(msgLine)
//      val vehicleFlowData = statisticsFlow(json)
//      (vehicleFlowData.getId,vehicleFlowData.getTotalNums)
//    }
//    }
//
//    //sum the previous popularity value and current value
//    val updateValue = (iterator: Iterator[(String, Seq[Int], Option[Int])]) => {
//      iterator.flatMap(t => {
//        val newValue:Int = t._2.sum
//        val stateValue:Int = t._3.getOrElse(0)
//        Some(newValue + stateValue)
//      }.map(sumedValue => (t._1, sumedValue)))
//    }
//
////    val initialRDD = ssc.sparkContext.parallelize(List(("page1", 0.00)))
////
////    val stateDstream = words.updateStateByKey[Int](updateValue,
////      new HashPartitioner(ssc.sparkContext.defaultParallelism), true, initialRDD)
////
////    //set the checkpoint interval to avoid too frequently data checkpoint which may
////    //may significantly reduce operation throughput
////    stateDstream.checkpoint(Duration(8*processingInterval.toInt*1000))
////
////
////    //after calculation, we need to sort the result and only show the top 10 hot pages
////    stateDstream.foreachRDD { rdd => {
////      val sortedData = rdd.map{ case (k,v) => (v,k) }.sortByKey(false)
////      val topKData = sortedData.take(10).map{ case (v,k) => (k,v) }
////      topKData.foreach(x => {
////        println(x)
////      })
//
//
//    ssc.start()
//    ssc.awaitTermination()
//  }
//
//}
//
//// Produces some random words between 1 and 100.
//object KafkaWordCountProducer {
//
//  def main(args: Array[String]) {
//    if (args.length < 4) {
//      System.err.println("Usage: KafkaWordCountProducer <metadataBrokerList> <topic> " +
//        "<messagesPerSec> <wordsPerMessage>")
//      System.exit(1)
//    }
//
//    val Array(brokers, topic, messagesPerSec, wordsPerMessage) = args
//
//    // Zookeeper connection properties
//    val props = new HashMap[String, Object]()
//    props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, brokers)
//    props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
//      "org.apache.kafka.common.serialization.StringSerializer")
//    props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
//      "org.apache.kafka.common.serialization.StringSerializer")
//
//    val producer = new KafkaProducer[String, String](props)
//
//    // Send some messages
//    while (true) {
//      (1 to messagesPerSec.toInt).foreach { messageNum =>
//        val str = JdctxConstants.createMsg()
//        val message = new ProducerRecord[String, String](topic, null, str)
//        producer.send(message)
//      }
//      Thread.sleep(1000)
//    }
//  }
//
//}
//
//// scalastyle:on println
