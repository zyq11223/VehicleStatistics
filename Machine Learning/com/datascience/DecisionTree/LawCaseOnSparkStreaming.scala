package com.datascience.DecisionTree


import kafka.serializer.StringDecoder
import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.streaming.kafka.KafkaUtils
import org.apache.spark.streaming.{Seconds, StreamingContext}
import org.apache.spark.mllib.linalg.Vectors
import org.apache.spark.mllib.regression.LabeledPoint
import org.apache.spark.mllib.tree.DecisionTree

/**
  * Created by yqs on 2018/3/1.
  */
class LawCaseOnSparkStreaming
{
  /**
    * 获取行RDD数据
    */
  def getLineRDD(ssc: StreamingContext) = {
    val zkQuorum: String = "master1:2181"
    val group: String = "yqs"
    val topics = Set("yqsTest")
    val brokers = "master1:9092"
    val kafkaParams = scala.Predef.Map[scala.Predef.String, scala.Predef.String]("bootstrap.servers" -> brokers, "serializer.class" -> "kafka.serializer.StringEncoder",
      "auto.offset.reset" -> "smallest", "zookeeper.connect" -> zkQuorum, "group.id" -> group)
    val rdd = KafkaUtils.createDirectStream[String, String, StringDecoder, StringDecoder](ssc, kafkaParams, topics).map(_._2)
    rdd
  }

  def getConsumer() {
    try {
      /**
        * 初始化SparkContext，批量间隔为5s
        */
      val sparkConf = new SparkConf().setAppName("LawCaseOnSparkStreaming").set("spark.serializer", "org.apache.spark.serializer.KryoSerializer")
      val ssc: StreamingContext = new StreamingContext(sparkConf, Seconds(2))
      val sc = new SparkContext(sparkConf)
      ssc.checkpoint("checkpoint")

      val modeData = sc.textFile("/user/ml")
      val rawData = getLineRDD(ssc)

      val data = modeData.map(line => {
        val tempData = line.split(",")
        val okData = tempData.filter(x => {
          if (x.contains('-'))
            false
          else
            true
        })
        val data = okData.map(_.toDouble)
        val features = Vectors.dense(data.init)
        val label = data.last
        LabeledPoint(label, features)
      })
      data.cache()
      val model = DecisionTree.trainRegressor(data, Map[Int, Int](), "variance", 4, 5)

      rawData.foreachRDD{
        rdd =>
          rdd.map{
            x =>
              val inputData = x.split(",").map(_.toDouble)
              val todayData = LabeledPoint(0, Vectors.dense(inputData))
              val todayLabel = model.predict(todayData.features)
//              val insertLawCaseResult = new InsertLawCaseResult()
//              insertLawCaseResult.insert(inputData.toString, todayLabel)
          }
      }
      ssc.start()
      ssc.awaitTermination()
    }
    catch {
      case e: Exception => System.err.println("消费 getConsumer:失败" + e, e)
    }
  }
}


object LawCaseOnSparkStreaming {
  def main(args: Array[String]) {
    try {
      val lawCaseOnSparkStreaming = new LawCaseOnSparkStreaming
      lawCaseOnSparkStreaming.getConsumer()
    }
    catch {
      case e: Exception => System.err.println("消费 getConsumer:失败" + e, e)
    }
  }
}
