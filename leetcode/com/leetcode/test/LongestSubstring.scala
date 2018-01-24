package com.leetcode.test

import scala.util.control.Breaks._

//Given a string, find the length of the longest substring without repeating characters.
//Examples:
//Given "abcabcbb", the answer is "abc", which the length is 3.
//Given "bbbbb", the answer is "b", with the length of 1.
//Given "pwwkew", the answer is "wke", with the length of 3. Note that the answer must be a substring, "pwke" is a subsequence and not a substring.

object LongestSubstring {

  /**
    *判断是否有相同字符
    */
  def checkDifferent(inisString: String): Boolean = {
    val array = new Array[Int](256)
    val len = inisString.length
    var result = true
    var flag = true
    if (len > 256) result = false
    for(i<- 0 to len -1 if flag ){
      val x = inisString(i)
      array(x) = array(x)+1
      if(array(x)>1) {
        result = false
        flag = false
      }
    }
    result
  }

  def lengthOfLongestSubstring(s: String) = {
    var first = 0
    var last = s.length -1
    val key = s(first)
    while(first<last){
      val key = s(first)
      while(first<last && s.charAt(last) != key)  last -= 1
      if(key == s.charAt(last)) {
        if(checkDifferent(s.substring(first,last-1))) break
      }

    }
      last- first
  }

  def main(args: Array[String]): Unit = {
    val str = "abcabcbb"
    println(checkDifferent(str))
  }
}
