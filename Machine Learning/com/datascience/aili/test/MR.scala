package com.datascience.aili.test

import org.apache.spark.{SparkConf, SparkContext}

object MR {


  def main(args: Array[String]): Unit = {
    val sc = new SparkContext(new SparkConf().setAppName("WordCount"))
    val base = "/user/ds/"
    /**
      * 读取多个数据文件
      */
    val rawData = sc.textFile(base+"part-*")

    /**
      * 读取数据字典
      */
    val domainDic = sc.textFile(base+"domain_dic.txt")
    var mapresult = domainDic.map(line => line.split("\\s+"))


  }
}
