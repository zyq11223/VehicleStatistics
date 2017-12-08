package com.datascience.test
import org.apache.spark.{SparkConf, SparkContext}


/**
  * Created by yqs on 2017/11/13.
  */
object FindTrack {

  def parse(line: String) = {
    val pieces = line.split("\t")
    val product_no = pieces(0).toString
    val lac_id = pieces(1).toString
    val moment = pieces(2).toString
    val start_time =  pieces(3).toString
    val user_id = pieces(4).toString
    val county_id = pieces(5).toString
    val staytime = pieces(6).toInt
    val city_id = pieces(7).toString
    val se = new SecondarySort(product_no, start_time)
    val track = new Track(product_no, lac_id,moment,start_time,user_id,county_id,staytime, city_id)
    (se,track)
  }

  def isHeader(line: String): Boolean = {
    line.contains("product_no")
  }

  def compTo(one:String,another:String):Int = {
    val len = one.length -1
    val v1 = one.toCharArray
    val v2 = another.toCharArray
    for(i <- 0 to len){
      val c1 = v1(i)
      val c2 = v2(i)
      if(c1 != c2) return c1 -c2
    }
    return 0
  }

  def add(x:Track, y:Track): Track = {
    if (compTo(x.startTime, y.startTime) < 0) {
      new Track(x.productNo,x.lacId,x.moment,x.startTime,x.userId,x.countyId,x.staytime + y.staytime,x.cityId)
    }
    else {
      new Track(y.productNo,y.lacId,y.moment,y.startTime,y.userId,y.countyId,x.staytime + y.staytime,y.cityId)
    }
  }

  def get(x:(SecondarySort,Iterable[Track])) :Track = {
      val xIter = x._2.head
     xIter
  }

  def main(args: Array[String]) {
    val sc = new SparkContext(new SparkConf().setAppName("FindTrack"))
    val base = "/user/ds/"
    val rawData = sc.textFile(base + "track.txt")

    val mds = rawData.filter(x => !isHeader(x)).map{x => parse(x)}.groupByKey().sortByKey(true).collect().map{x => get(x)}.reduceLeft{ (x, y) =>
      if((x.productNo == y.productNo && x.lacId == y.lacId))
        add(x, y)
      else
      {
        println(x)
        y
      }
    }
  }
}
