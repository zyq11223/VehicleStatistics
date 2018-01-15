package com.datascience.aili.test

import java.io.IOException
import java.util.regex.Pattern
import org.apache.spark.{SparkConf, SparkContext}
import scala.collection.mutable
import scala.io.{BufferedSource, Source}
import org.apache.spark.SparkConf

/**
  * Created by yqs on 2018/1/12
  */
object MR {
  /**
    * 读取域名映射字典文件
    */
  def readDomainDic(path: String): mutable.HashMap[String, String] = {
    var file: BufferedSource = null
    val map = new mutable.HashMap[String, String] {}
    try {
      file = Source.fromFile(path)
      for (line <- file.getLines) {
        val pieces = line.split("\\s")
        val ip = pieces(0).toString
        val domain = pieces(1).toString
        map += (ip -> domain)
      }
    } catch {
      case e: IOException => {
        e.printStackTrace
      }
    }
    finally {
      file.close()
    }
    map
  }
  /**
    * 判断是否Ip
    */
  def isboolIp(ipAddress: String): Boolean = {
    if (ipAddress.length() < 7 || ipAddress.length() > 15 || "".equals(ipAddress)) return false
    val rexp = "([1-9]|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])(\\.(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])){3}"
    val pat = Pattern.compile(rexp)
    val mat = pat.matcher(ipAddress)
    return mat.find()
  }

  /**
    *
    * 解析数据格式
    */
  def parse(dictionary: mutable.HashMap[String, String], line: String) = {
    var value: String = null
    val pieces = line.split("/")
    val data = pieces(2).toString
    var port: String = null
    var ipOrDomain: String = null
    /**
      * 包含端口
      */
    if (data.contains(":")) {
      val datas = data.split(":")
      ipOrDomain = datas(0).toString
      port = datas(1).toString
    }
    else ipOrDomain= data
    ipOrDomain = if (isboolIp(ipOrDomain)) dictionary.getOrElse(ipOrDomain, ipOrDomain) else ipOrDomain
    value = if( port != null)  ipOrDomain + ":"+ port else ipOrDomain
    value
  }
  def main(args: Array[String]): Unit = {
    val sc = new SparkContext(new SparkConf().setAppName("MR"))
    val base = "/user/ds/"
    /**
      * 读取多个数据文件
      */
    val rawData = sc.textFile(base + "part-*.txt")
    /**
      * 读取本地数据字典
      */
    val domainDic = readDomainDic(base + "domain_dic.txt")
    /**
      * 广播变量共享数据
      */
    val broadcastVar = sc.broadcast(domainDic)
    val result = rawData.map(line =>(parse(broadcastVar.value, line),1)).reduceByKey(_+_).sortBy(_._2,false)
    result.collect().foreach(println)
    result.saveAsTextFile(base + "result/")
  }
}
