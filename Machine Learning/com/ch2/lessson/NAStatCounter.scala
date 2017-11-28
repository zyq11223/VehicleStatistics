package com.ch2.lessson

import org.apache.spark.rdd.RDD
import org.apache.spark.util.StatCounter

/**
  * Created by Administrator on 2017/9/27.
  */
class NAStatCounter extends Serializable {
  val stats: StatCounter = new StatCounter()
  var missing: Long = 0

  def add(x: Double): NAStatCounter = {
    if (java.lang.Double.isNaN(x)) {
      missing += 1
    } else {
      stats.merge(x)
    }
    this
  }

  def merge(other: NAStatCounter): NAStatCounter = {
    stats.merge(other.stats)
    missing += other.missing
    this
  }

  override def toString = {
    "stats:" + stats.toString() + "NaN: " + missing
  }

}

object NAStatCounter extends Serializable {
  def apply(x:Double) = new NAStatCounter().add(x)
}
