package com.datascience.lsa

import org.apache.spark.{SparkConf, SparkContext}

/**
  * Created by yqs on 2018/2/1.
  */
class RunLSA {
  def main(args: Array[String]) {
    val path= "/user/ds/"
    val sc = new SparkContext(new SparkConf().setAppName("LSA"))

  }
}
