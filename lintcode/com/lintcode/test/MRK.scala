package com.lintcode.test

import org.apache.spark.{SparkConf, SparkContext}

/**
  * Created by yqs on 2018/2/24.
  */
class MRK {
  def main(args: Array[String]) {
    val sc = new SparkContext(new SparkConf().setAppName("MRK"))
    val base = "/user/ds/"
    /**
      * 读取多个数据文件
      */
    val rawData = sc.textFile(base + "part_*.txt")
    val words = rawData.flatMap(_.split(" ")).map(x => (x, 1)).reduceByKey(_+_).sortBy(_._2,false).take(2).foreach(println)
  }
}
