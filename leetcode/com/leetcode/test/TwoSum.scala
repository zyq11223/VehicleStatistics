package com.leetcode.test

/**
  * Created by yqs on 2018/1/16.
  */
object TwoSum {
  def twoSum(nums: Array[Int], target: Int): Array[Int] = {
    var flag = true
    var result = new Array[Int](2)
    var map = scala.collection.mutable.Map[Int,Int]()
    for (i<- 0 until nums.length if flag) {
         if(map.contains(target - nums(i))) {
             result(0) = map(target - nums(i))
             result(1) = i
             flag = false
           }
      else
         map +=(nums(i)->i)
      }
    result
  }
  def main(args: Array[String]) {
    val nums = Array(3,2, 7, 11, 15)
    val target = 9
    twoSum(nums,target).foreach(println)
  }

}
