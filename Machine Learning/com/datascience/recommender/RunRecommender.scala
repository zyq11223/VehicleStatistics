package com.datascience.recommender

import org.apache.spark.mllib.recommendation._
import org.apache.spark.{SparkConf, SparkContext}

/**
  * Created by Administrator on 2017/9/29.
  *
  * spark-shell --master local[*] --driver-memory 5g
  */
object RunRecommender {
  def main(args: Array[String]) {
    val sc = new SparkContext(new SparkConf().setAppName("Recommender"))
    val base = "/user/ds/"
    val rawUserArtistData = sc.textFile(base +"user_artist_data.txt")
    val rawArtistData  = sc.textFile(base +"artist_data.txt")
    val rawArtistAlias = sc.textFile(base +"artist_alias.txt")


    /*    val artistByID = rawUserArtistData.map{ line =>
          val (id,name) = line.span(_ !='\t')
          (id.toInt,name.trim)
        }*/


    val artistByID = rawArtistData.flatMap { line =>
      val (id, name) = line.span(_ != '\t')
      if (name.isEmpty) {
        None
      }
      else {
        try {
          Some((id.toInt, name.trim))
        }
        catch {
          case e: NumberFormatException => None
        }
      }
    }


    val artistAlias = rawArtistAlias.flatMap { line =>
      val tokens = line.split('\t')
      if (tokens(0).isEmpty) {
        None
      } else {
        Some((tokens(0)).toInt, tokens(1).toInt)
      }
    }.collectAsMap()

    val bArtistAlias = sc.broadcast(artistAlias)

    val trainData  = rawUserArtistData.map{ line =>
      val Array(userID,artistID,count) = line.split(' ').map(_.toInt)
      val finalArtistID = bArtistAlias.value.getOrElse(artistID,artistID)
      Rating(userID,finalArtistID,count)
    }.cache()

    val model = ALS.trainImplicit(trainData,10,5,0.01,1.0)
    model.userFeatures.mapValues(_.mkString(",")).first()

    val rawArtistsForUser = rawUserArtistData.map(_.split(' ')).filter{case Array(user,_,_) =>user.toInt == 2089594}

    val existingProducts = rawArtistsForUser.map{ case Array(_,artist,_) => artist.toInt}.collect().toSet

    artistByID.filter{case (id,name) => existingProducts.contains(id)}.values.collect().foreach(println)

    val recommendations = model.recommendProducts(2089594,5)

    val recommendedProductIDs = recommendations.map(_.product).toSet

    artistByID.filter{case(id,name) =>
        recommendedProductIDs.contains(id)
    }.values.collect().foreach(println)

  }

}
