package com.ch2.lessson

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkContext, SparkConf}

/**
  * Created by Administrator on 2017/9/27.
  */
object RunIntro {
  def isHeader(line: String): Boolean = {
    line.contains("id_1");
  }

  def toDouble(s: String) = {
    if ("?".equals((s))) Double.NaN else s.toDouble
  }

  def parse(line: String) = {
    val pieces = line.split(",")
    val id1 = pieces(0).toInt
    val id2 = pieces(1).toInt
    val scores = pieces.slice(2, 11).map(toDouble)
    val matched = pieces(11).toBoolean
    MatchData(id1, id2, scores, matched)
  }


  def statsWithMissing(rdd: RDD[Array[Double]]): Array[NAStatCounter] = {
    val nastats = rdd.mapPartitions((iter: Iterator[Array[Double]]) => {
      val nas: Array[NAStatCounter] = iter.next().map(d => NAStatCounter(d))
      iter.foreach(arr => {
        nas.zip(arr).foreach { case (n, d) => n.add(d) }
      })
      Iterator(nas)
    })
    nastats.reduce((n1, n2) => {
      n1.zip(n2).map { case (a, b) => a.merge(b) }
    })
  }




  def main(args: Array[String]) {
    val sc = new SparkContext(new SparkConf().setAppName("Intro"))
    val rawblocks = sc.textFile("/linkage")
    val head = rawblocks.take(10)
    val noheader = rawblocks.filter(x => !isHeader(x))

    val scores = rawblocks.map(toDouble)
    val mds = head.filter(x => !isHeader(x)).map(x => parse(x))
    val parsed = noheader.map(line => parse(line))
    val grouped = mds.groupBy(md => md.matched)
    grouped.mapValues(x => x.size).foreach(println)
    val matchCounts = parsed.map(md => md.matched).countByValue()

    val stats = (0 until 9).map(i => {
      parsed.map(md => md.scores(i)).filter(!java.lang.Double.isNaN(_)).stats()
    })


    val nasRDD = parsed.map(md => {
      md.scores.map(d => NAStatCounter(d))
    })

    val  nas1 = Array(1.0,Double.NaN).map(d=>NAStatCounter(d))
    val  nas2 = Array(Double.NaN,2.0).map(d =>NAStatCounter(d))
   // val merged = nas1.zip(nas2).map(p =>p._1.merge(p._2))

    //val merged  = nas1.zip(nas2).map{case (a,b) => a.merge(b)}

    val nas = List(nas1,nas2)
    val merged = nas.reduce((n1,n2) =>{
      n1.zip(n2).map{case(a,b) => a.merge(b)}
    }
    )

    val reduced = nasRDD.reduce((n1,n2) => {
            n1.zip(n2).map{case(a,b) => a.merge(b)}
    })

    val statsm = statsWithMissing(parsed.filter(_.matched).map(_.scores))
    val statsn = statsWithMissing(parsed.filter(!_.matched).map(_.scores))

    statsm.zip(statsn).map{case (m,n) =>
         (m.missing+n.missing,m.stats.mean-n.stats.mean)
           }.foreach(println)

  }
}


