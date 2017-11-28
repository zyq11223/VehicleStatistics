package main.scala

import org.apache.spark.{SparkContext, SparkConf}
import org.apache.spark.rdd.RDD

/**
  * Created by yqs on 2017/1/12
  */
object TestRddOperator {
  def main(args: Array[String]) {
    val conf = new SparkConf().setAppName("yqs")
    val sc: SparkContext = new SparkContext(conf)

    /**
      * 转化操作
      */
    println("=======转化操作========")

    val rddInt: RDD[Int] = sc.makeRDD(List(1, 2, 3, 4, 5, 6, 2, 5, 1))
    val rddStr: RDD[String] = sc.parallelize(Array("a", "b", "c", "d", "b", "a"), 1)
    val rddFile: RDD[String] = sc.textFile("/user/root/spark/README.md", 1)
    val rdd01: RDD[Int] = sc.makeRDD(List(1, 3, 5, 3))
    val rdd02: RDD[Int] = sc.makeRDD(List(2, 4, 5, 1))

    /**
      * map 操作
      */
    println("=======map操作========")
    println(rddInt.map(x => x + 1).collect().mkString(","))
    println("=======map操作========")

    /**
      * filter操作
      */
    println("=====filter操作===")
    println(rddInt.filter(x => x > 4).collect().mkString(","))
    println("=====filter操作===")

    /**
      * flateMap操作
      */
    println("=====flateMap操作===")
    println(rddFile.flatMap { x => x.split(",") }.first())
    println("=====flateMap操作===")

    /**
      *
      * distinct去重操作
      */
    println("======distinct去重======")
    println(rddInt.distinct().collect().mkString(","))
    println(rddStr.distinct().collect().mkString(","))
    println("======distinct去重======")

    /**
      * union操作
      */
    println("======union操作======")
    println(rdd01.union(rdd02).collect().mkString(","))
    println("======union操作======")

    /**
      * intersection操作
      */
    println("=====intersection操作======")
    println(rdd01.intersection(rdd02).collect().mkString(","))
    println("=====intersection操作======")

    /**
      * substract操作
      */
    println("==== substract操作======")
    println(rdd01.subtract(rdd02).collect().mkString(","))
    println("===== substract操作======")

    /* cartesian操作 */
    println("==== cartesian操作======")
    println(rdd01.cartesian(rdd02).collect().mkString(","))
    println("===== cartesian操作======")


    /**
      * 行动操作
      */
    println("=======行动操作========")

    val rddInt2: RDD[Int] = sc.makeRDD(List(1, 2, 3, 4, 5, 6, 2, 5, 1))
    val rddStr2: RDD[String] = sc.parallelize(Array("a", "b", "c", "d", "b", "a"), 1)

    /**
      * count操作
      */
    println("=====count操作======")
    println(rddInt2.cache)
    println("=====count操作======")


    /**
      * countByValue操作
      */
    println("=====countByValue操作======")
    println(rddInt2.countByValue())
    println("=====countByValue操作======")



    /**
      * reduce操作
      */
    println("=====reduce操作======")
    println(rddInt2.reduce((x, y) => x + y))
    println("=====reduce操作======")

    /**
      * fold操作
      */
    println("=====fold操作======")
    println(rddInt2.fold(0)((x, y) => x + y))
    println("=====fold操作======")

    /**
      * aggregate操作
      */
    println("=====aggregate操作======")
    val res: (Int, Int) = rddInt2.aggregate((0, 0))((x, y) => (x._1 + x._2, y), (x, y) => (x._1 + x._2, y._1 + y._2))
    println(res._1 + "," + res._2)
    println("======aggregate操作======")

    /**
      * foeach操作
      */
    println("=====foeach操作======")
    println(rddStr2.foreach { x => println(x) })
    println("=====foeach操作======")

  }
}
