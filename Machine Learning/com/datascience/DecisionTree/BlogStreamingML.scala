package com.datascience.DecisionTree


import kafka.serializer.StringDecoder
import org.apache.spark.mllib.tree.model.RandomForestModel
import org.apache.spark.streaming.kafka.KafkaUtils
import org.apache.spark.streaming.{Seconds, StreamingContext}
import org.apache.spark.{SparkContext, SparkConf}
import org.apache.spark.mllib.regression.LabeledPoint
import org.apache.spark.mllib.linalg.Vectors

/**
  * Created by yqs on 2018/3/2.
  */
object BlogStreamingML {

  val stringArrToDoubleArr = (arr: Array[String]) => {
    val doubleArr = new Array[Double](arr.length - 1) //TODO 减去1的目的是去掉标签
    for (x <- (0 until arr.length - 1)) {
      doubleArr(x) = arr(x).toDouble

    }
    doubleArr
  }

  def main(args: Array[String]) {
    if (args.length < 1) {
      println("please input the topic of kafka !")
      System.exit(0)
    }


    //1：Kafka 配置
    val zkQuorum: String = "master1:2181"
    val group: String = "yqs"
    val topics = Set("yqsTest")
    val brokers = "master1:9092"
    val kafkaParams = Map[String, String]("bootstrap.servers" -> brokers, "serializer.class" -> "kafka.serializer.StringEncoder",
      "auto.offset.reset" -> "smallest", "zookeeper.connect" -> zkQuorum, "group.id" -> group)

    val conf = new SparkConf().setAppName("Streaming-ML") //.setMaster("local[*]")
    val sc = new SparkContext(conf)
    val ssc = new StreamingContext(sc, Seconds(2))
    //加载离线训练好的模型
    val model = RandomForestModel.load(sc, "/har-model/")
    // 流数据来源至Kafka，加载数据
    val rawData = KafkaUtils.createDirectStream[String, String, StringDecoder, StringDecoder](ssc, kafkaParams, topics).map(_._2)


    val dic = collection.mutable.Map[String, Double]()
    //加载广播的字典数据代码略
    //广播字典数据
    sc.broadcast(dic)

    //模拟Spark ML加载LibSvm文件的源码自己实现的加载数据转为底层存储格式
    rawData.foreachRDD {
      rdd =>
        val labeledPoint = rdd.map {
          x =>
            val userId = x.substring(0, 29)
            val data = x.substring(30, x.length).replaceAll(",", " ").split(" ")
            val values = stringArrToDoubleArr(data)
            val arr = x.split(" ")
            val label = arr(arr.length - 1)
            val indexs = (0 to data.length - 2).toArray
            //重点：二元组，第一个元素是UserId，第二个元素是矩阵的表示
            (userId, LabeledPoint(dic.getOrElse(label.toLowerCase, 5.0), Vectors.sparse(indexs.length, indexs, values)))
        }


        val keyAndLabelAndPreds = labeledPoint.map { point =>
          val userId = point._1
          val labeledPoint = point._2
          val prediction = model.predict(labeledPoint.features)
          //userId,数据自带的标签，识别的标签
          (userId, labeledPoint.label, prediction)
        }

        //存储缓存供外部系统实时获取，10秒失效
//        keyAndLabelAndPreds.foreachPartition {
//          lazy val jedis = RedisUtils.getJedis(6)
//          partition =>
//            partition.foreach {
//              //10秒过期
//              case (key, label, predict) => jedis.set(key, predict.toString, "NX", "EX", 10L);
//            }
//            jedis.close()
//        }


    }

    ssc.start()
    ssc.awaitTermination()


  }
}
