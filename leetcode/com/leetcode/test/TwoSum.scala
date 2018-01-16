package com.leetcode.test

/**
  * Created by yqs on 2018/1/16.
  */
object TwoSum {
  def twoSum(nums: Array[Int], target: Int): Array[Int] = {
    val len = nums.length
    val array = new Array[Int](len)
    array
  }

  def minAndMax(nums:Array[Int]):Array[Int] ={
    var min  = 0
    var max = 0
    for (num <- nums)
    {
      min = if(min <= num) min else num
      max = if(max >= num) min else num
    }
    Array(min,max)
  }

  def main(args: Array[String]) {

  }

}
