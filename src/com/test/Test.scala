package com.test

import com.fhzz.business.entity.{CarbelongResult, CarbelongResultId}

import scala.collection.mutable


/**
  * Created by Administrator on 2017/3/16.
  */
object Test {
  def main(args: Array[String]) {
    val list = new  mutable.ListBuffer[(String, String)]()
     list .+=(("name","yqs"))
    list .+=(("name2","yqs2"))
    print(list)
  }
}
