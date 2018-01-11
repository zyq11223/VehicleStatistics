package com.datascience.kmeans

import org.apache.spark.mllib.clustering.{KMeansModel, KMeans}
import org.apache.spark.mllib.linalg.{Vectors,Vector}
import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}



/**
  * Created by yqs on 2017/12/5.
  */
object RunKMeans2 {

  def distance(a:Vector,b:Vector) =
    math.sqrt(a.toArray.zip(b.toArray).map(p => p._1 - p._2).map(d => d * d).sum)

  def disToCentroid(datum:Vector,model:KMeansModel) = {
    val cluster = model.predict(datum)
    val centroid = model.clusterCenters(cluster)
    distance(centroid,datum)
  }

  def clusteringScore(data: RDD[Vector],k:Int)  = {
    val kmeans  = new KMeans()
    kmeans.setK(k)
    val model = kmeans.run(data)
    kmeans.setRuns(10)
    kmeans.setEpsilon(1.0e-6)
    data.map(datum => disToCentroid(datum,model)).mean()
  }


  def main(args: Array[String]) {
    val sc = new SparkContext(new SparkConf().setAppName("KNN"))
    val base = "/user/ds/kddcup/"
    val rawData  = sc.textFile(base +"kddcup.data")
    rawData.map(_.split(',').last).countByValue().toSeq.
      sortBy(_._2).reverse.foreach(println)

    val labelsAndData = rawData.map { line =>
      val buffer = line.split(',').toBuffer
      buffer.remove(1, 3)
      val label = buffer.remove(buffer.length - 1)
      val vector = Vectors.dense(buffer.map(_.toDouble).toArray)
      (label, vector)
    }
    val data  = labelsAndData.values.cache()
    val kmenas = new KMeans()
    val models  = kmenas.run(data)
    models.clusterCenters.foreach(println)

    val clusterLabelCount = labelsAndData.map{ case(label,datum) =>
      val cluster  = models.predict(datum)
      (cluster,label)
    }.countByValue

    clusterLabelCount.toSeq.sorted.foreach{
      case((cluster,label),count) =>
        println(format("%1s %18s %8s",cluster,label,count))
    }

//    (5 to 40 by 5).map(k => (k,clusteringScore(data,k))).foreach(println)
//    (30 to 100 by 10).map(k => (k,clusteringScore(data,k))).foreach(println)

//    val sample = data.map(datum =>
//    models.predict(datum) +"," +datum.toArray.mkString(",")).sample(false,0.05)
//    sample.saveAsTextFile("/user/ds/sample")


    val dataAsArray = data.map(_.toArray)
    val numCols = dataAsArray.first().length
    val n  = dataAsArray.count()
    val sums = dataAsArray.reduce((a,b) => a.zip(b).map(t => t._1 + t._2))
    val sumSquares = dataAsArray.fold(new Array[Double](numCols))((a,b) => a.zip(b).map(t => t._1+ t._2 *t._2 ))

    /**
      * 方差
      */
    val stdevs = sumSquares.zip(sums).map {
      case(sumSq,sum) => math.sqrt(n* sumSq - sum*sum)/n
    }

    /**
      * 平均值
      */
    val means = sums.map(_/n)

    /**
      * 特征规范化
      * @param datum
      */
    def normalize(datum:Vector)  = {
      val normalizedArray = (datum.toArray,means,stdevs).zipped.map(
      (value,mean,stdev) =>
        if(stdev <= 0) (value - mean) else (value - mean) /stdev
      )
      Vectors.dense(normalizedArray)
    }


    val normalizedData = data.map(normalize).cache()
    (60 to 120 by 10).par.map(k => (k,clusteringScore(normalizedData,k))).toList.foreach(println)
  }

}
