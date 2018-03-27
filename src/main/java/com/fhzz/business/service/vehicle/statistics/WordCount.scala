package com.fhzz.business.service.vehicle.statistics
import java.util.{Date, Properties}
import com.fhzz.core.tools.DateUtil
import kafka.serializer.StringDecoder
import net.sf.json.JSONObject
import org.apache.spark.{HashPartitioner, SparkConf}
import org.apache.spark.streaming.kafka.KafkaUtils
import org.apache.spark.streaming.{Seconds, StreamingContext}
import kafka.producer.KeyedMessage
import kafka.producer.ProducerConfig
import kafka.producer.Producer

/**
  * Created by yqs on 2017/1/17
  */
class WordCount extends Serializable {
  def getSparkContext(): StreamingContext = {
    /**
      * 初始化SparkContext，批量间隔为5s
      */
    val sparkConf = new SparkConf().setAppName("VehicleInfoStatisticsApp").set("spark.serializer", "org.apache.spark.serializer.KryoSerializer")
    //      .set("spark.rpc.netty.dispatcher.numThreads", "5").set("spark.executor.cores", "5").set("spark.num.executors", "5")
    //      .set("spark.driver.memory", "5G")
    //      .set("spark.executor.memory", "20G")
    val batchDuration = Seconds(2)

    // 建立Spark Streaming启动环境
    val ssc = new StreamingContext(sparkConf, batchDuration)
    ssc.checkpoint("checkpoint")
    ssc
  }

  /**
    * 获取行RDD数据
    */
  def getLineRDD(ssc: StreamingContext ,brokers:String,topicsName:String,group:String) = {
    val topics = Set(topicsName)
    val zkQuorum: String = "172.21.89.3:24002,172.21.89.5:24002,172.21.89.4:24002/kafka"
    val kafkaParams = Map[String, String]("metadata.broker.list" -> brokers,
      "bootstrap.servers"->"172.21.89.7:21007,172.21.89.8:21007,172.21.89.6:21007",
      "serializer.class" -> "kafka.serializer.StringEncoder",
      "zookeeper.connect" -> zkQuorum,
      "auto.offset.reset" -> "largest",
      "group.id" -> group
    )
    val rdd = KafkaUtils.createDirectStream[String, String, StringDecoder, StringDecoder](ssc, kafkaParams, topics).map(_._2)
    rdd
  }

  /**
    * 统计小时车流量
    */
  def statisticsFlow(msgLine: String):String = {
    var jgday = ""
    try {

      val json = JSONObject.fromObject(msgLine)
      val jgsk = json.getString("JGSK")
      /**
        * 异常数据不统计
        */
      if (jgsk.length >= 10)
        jgday = jgsk.substring(0, 10)
    }
    catch {
      case e: Exception =>  println("车流量统计:初始化失败" + e, e)
    }
    finally {
      return jgday
    }
    jgday
  }

  /**
    * 车流量小时更新
    */
  val updateFunc = (iter: Iterator[(String, Seq[Int], Option[Int])]) => {
    iter.flatMap{case(x,y,z)=>Some(y.sum + z.getOrElse(0)).map(m=>(x, m))}
  }


  def sendKafka(key:java.lang.String,value:Int) {
    val curJgDay = DateUtil.getDateDayFormat(new Date())
    val topicName = "vehicleStatic"
    if (curJgDay.equalsIgnoreCase(key)) {
      val producerConf = new Properties()
      producerConf.put("serializer.class", "kafka.serializer.StringEncoder")
      producerConf.put("metadata.broker.list", "172.21.89.7:21005,172.21.89.8:21005,172.21.89.6:21005")
      producerConf.put("request.required.acks", "1")
      val config = new ProducerConfig(producerConf)
      val producer = new Producer[String, String](config)
      val msg = "{\"Type\":\"vehicle\",\"Time\":" + "\"" + key + "\"" + ",\"Nums\":" + value + "}"
      val data = new KeyedMessage[String, String](topicName, msg)
      producer.send(data)
      producer.close()
    }
  }

  def getStart(brokers:String,topicsName:String,group:String){
    val ssc = getSparkContext
    ssc.checkpoint("checkpoint")
    val rdd = getLineRDD(ssc,brokers,topicsName,group)
    val vehicleData = rdd.map(msgLine => {
      val vehicleFlowData = statisticsFlow(msgLine)
      (vehicleFlowData,1)
    })

    val results = vehicleData.filter(x => x._1 != "").updateStateByKey(updateFunc,new HashPartitioner(ssc.sparkContext.defaultParallelism), true)
    results.foreachRDD(rdd =>
      rdd.foreachPartition(p => {
        p.foreach(x => {
          println("{\"Type\":\"vehicle\",\"Time\":" + "\"" + x._1 + "\"" + ",\"Nums\":" + x._2 + "}")
          sendKafka(x._1, x._2)
        })
      })
    )
    ssc.start()
    ssc.awaitTermination()
  }
}

object WordCount {
  def main(args: Array[String]): Unit = {
    val topics = "VEHICLE_INFO"
    val brokers = "172.21.89.7:21005,172.21.89.8:21005,172.21.89.6:21005"
    val group: String = "vehicleStatic"
    val wordCount = new WordCount
    wordCount.getStart(brokers,topics,group)
  }
}
