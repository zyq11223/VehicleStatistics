package com.leetcode.test

/**
  * Created by yqs on 2018/1/15.
  */
object DailyTemperatures {
  def dailyTemperatures(temperatures: Array[Int]): Array[Int] = {
    val length= temperatures.length
    if(length>30000 || length <1 )
      throw    new IllegalArgumentException("Arrays length is error")
    val array=new Array[Int](length)
    for (i <- 0 to length -1){
      var flag = true
      for(j <- i+1 to length -1 if flag) if(temperatures(i)<temperatures(j)){
        if(temperatures(i)>100|| temperatures(i)<30 || temperatures(j)>100|| temperatures(j)<30  )
          throw new  IllegalArgumentException("Arrays values is error")
        array(i) = j -i
        flag =false
      }
    }
    array
  }

  def main(args: Array[String]) {
    val array = Array(73, 74, 75, 71, 69, 72, 76, 73)
    dailyTemperatures(array).foreach(println)
  }
}
