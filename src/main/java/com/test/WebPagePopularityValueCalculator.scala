package com.test

import org.apache.spark.SparkConf
import org.apache.spark.streaming.kafka.KafkaUtils
import org.apache.spark.streaming.{Seconds, StreamingContext}

/**
  * Created by Administrator on 2017/2/23.
  */
object WebPagePopularityValueCalculator {

  private val checkpointDir = "popularity-data-checkpoint"

  private val msgConsumerGroup = "user-behavior-topic-message-consumer-group"

  def main(args: Array[String]) {


    val zkServers = "master1:2181"

    val conf = new SparkConf().setAppName("Web Page Popularity Value Calculator")


    val ssc = new StreamingContext(conf, Seconds(10))

    //using updateStateByKey asks for enabling checkpoint

    ssc.checkpoint(checkpointDir)

    val kafkaStream = KafkaUtils.createStream(

      //Spark streaming context

      ssc,

      //zookeeper quorum. e.g zkserver1:2181,zkserver2:2181,…

      zkServers,

      //kafka message consumer group ID

      msgConsumerGroup,

      //Map of (topic_name -> numPartitions) to consume. Each partition is consumed in its own thread

      Map("user-behavior-topic" -> 3))

    val msgDataRDD = kafkaStream.map(_._2)

    //for debug use only

    //println("Coming data in this interval…")

    //msgDataRDD.print()

    // e.g page37|5|1.5119122|-1

    val popularityData = msgDataRDD.map { msgLine => {

      val dataArr: Array[String] = msgLine.split("\\|")
      val pageID = dataArr(0)
      //calculate the popularity value
      val popValue: Double = dataArr(1).toFloat * 0.8 + dataArr(2).toFloat * 0.8 + dataArr(3).toFloat * 1
      (pageID, popValue)
    }
    }
  }
}