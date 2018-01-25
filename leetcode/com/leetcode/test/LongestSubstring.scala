package com.leetcode.test

import scala.collection.mutable

//思路：HashMap+Two Pointer——用HashMap标记某个字符最晚出现的地方。用pre标记不重复的起始位置。当出现重复字符的时候，分成三种情况讨论：
//1）原有重复字符正好是pre节点
//2）原有重复字符不是pre节点
//a）原有重复字符在pre节点之后
//b）原有重复字符在pre节点之前

object LongestSubstring {
  def lengthOfLongestSubstring(s: String): Int = {
    var max = 0
    var pre =0
    val hm = new mutable.HashMap[Char,Int]()
    for(i<-0 until s.length){
      val c = s.charAt(i)
      if(!hm.contains(c)){
        hm.put(c,i)
        max = Math.max(max,i-pre+1)
      }else{
        if(pre <= hm(c)){
          max = Math.max(max,i - hm(c))
          pre = hm(c) + 1
        }
        else max = Math.max(max,i - pre + 1)
        hm.put(c,i)
      }
    }
    max
  }

  def main(args: Array[String]): Unit = {
//    val str = "abcabcbb"
//    val str = "bbbbb"
//   val str = "pwwkew"
  val str = "cdd"
    println(lengthOfLongestSubstring(str))
  }
}
