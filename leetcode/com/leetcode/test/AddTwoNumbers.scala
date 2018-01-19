package com.leetcode.test

import scala.collection.mutable.ArrayBuffer


/**
  * Created by yqs on 2018/1/18.
  */
object AddTwoNumbers {

  class ListNode(var _x: Int = 0) {
    var next: ListNode = null
    var x: Int = _x
  }
  def toArrayBuffer(l: ListNode): ArrayBuffer[Int] = {
    var temp = l
    var b = new ArrayBuffer[Int]()
    var result = new ArrayBuffer[Int]()
    while (temp != null) {
      b += temp._x
      temp = temp.next
    }
    for (i <- (0 to b.length-1).reverse) {
      result += b(i)
    }
    result
  }


  def add(l1:ArrayBuffer[Int],l2:ArrayBuffer[Int]):ArrayBuffer[Int] = {

    /**
      * 运算和
      */
    var temp = 0
    /**
      * 进位
      */
    var  carry = 0
    var j = l2.length -1
    var result = new ArrayBuffer[Int]()
    for (i <- (0 to l1.length - 1).reverse) {
      temp = carry
      if (i >= 0) temp += l1(i)
      if (j >= 0) {
        temp += l2(j)
        j -= 1
      }
      if (temp >= 10) {
        result += temp - 10
        carry = 1
      } else {
        result += temp
        carry = 0
      }
    }
    if(carry !=0) result += carry
    result
  }

  def addTwoNumbers(l1: ListNode, l2: ListNode): ListNode = {
    val l1ArrayBuffer = toArrayBuffer(l1)
    val l2ArrayBuffer = toArrayBuffer(l2)
    val result = if(l1ArrayBuffer.length>=l2ArrayBuffer.length)add(l1ArrayBuffer,l2ArrayBuffer) else add(l2ArrayBuffer,l1ArrayBuffer)

    val linkList = new Array[ListNode](result.length)
    var j= 0
    for(i <- (0 to result.length - 1).reverse){
      linkList(j) = new ListNode(result(i))
      j += 1
    }

    for (i <- (0 to linkList.length-1).reverse) {
      if (i -1  >= 0) {
        linkList(i).next =linkList(i - 1)
      }
    }
    linkList(linkList.length-1)
  }

  def main(args: Array[String]) {
    val listNode1 = Array[Int](2 , 4 , 3)
        val linkList1 = new Array[ListNode](listNode1.length)
        for (i <- 0 to listNode1.length - 1) linkList1(i) = new ListNode(listNode1(i))
        for (i <- 0 until listNode1.length) if (i + 1 <= linkList1.length - 1) linkList1(i).next = linkList1(i + 1)

        val listNode2 = Array[Int](5,6,4)
        val linkList2 = new Array[ListNode](listNode2.length)
        for (i <- 0 to listNode2.length - 1) linkList2(i) = new ListNode(listNode2(i))
        for (i <- 0 until listNode2.length) if (i + 1 <= linkList2.length - 1) linkList2(i).next = linkList2(i + 1)
        addTwoNumbers(linkList1(0), linkList2(0))
  }
}
