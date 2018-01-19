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
  def isIntByRegex(s : String) = {
    val pattern = """^(\d+)$""".r
    s match {
      case pattern(_*) => true
      case _ => false
    }
  }
  def pow(x: Int, y: Int):Int = if(y>=1) x* pow(x,y-1) else return 1

//  char *addBigInter(const char *a,const char *b) {
//    char *result = NULL;
//    int i, j, resultLen;
//    int len1 = strlen(a);
//    int len2 = strlen(b);
//    resultLen = (len1 > len2) ? len1: len2;
//    result = (char *)malloc((resultLen+2)*sizeof(char));
//    memset(result, '0', (resultLen+1)*sizeof(char));
//    result[resultLen+1] = '\0';
//    int carry = 0, temp;
//    for(i=len1-1, j=len2-1; i>=0 || j>=0; i--, j--, resultLen--)
//    {
//      temp = carry;
//      if(i>=0) temp += a[i] - '0';
//      if(j>=0) temp += b[j] - '0';
//      if (temp >= 10) {
//        result[resultLen] = temp - 10 + '0';
//        carry = 1;
//      } else {
//        result[resultLen] = temp + '0';
//        carry = 0;
//      }
//    }
//    result[resultLen] = carry + '0';
//    return result;
//  }


//  def addBigInter(a:String,b:String) :String ={
//     val aChar = convert(a)
//     val bChar =  convert(b)
//
//
//  }


  /**
    * 转化为ArrayBuffer
    */
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

  def convert(s:String):Array[Char] = {
    val aChar = new Array[Char](s.length)
    for (i<- 0 until s.length)  aChar(i) = s.charAt(i)
    aChar
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
        result += temp - 10
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

        for (i <- 0 until linkList.length) {
          if (i + 1 <= linkList.length - 1) {
            linkList(i).next =linkList(i + 1)
          }
        }
    linkList(0)


//    val sum = toInt(l1) + toInt(l2)
//    val sumString = String.valueOf(sum)
//    val linkList = new Array[ListNode](sumString.length)
//    var j = 0
//    for (i <- (0 to sumString.length - 1).reverse) {
//      val c = sumString.charAt(i).toString
//      if(isIntByRegex(c)){
//        linkList(j) = new ListNode(Integer.parseInt(sumString.charAt(i).toString))
//        j = j+ 1
//      }
//    }
//    for (i <- 0 until linkList.length) {
//      if (i + 1 <= linkList.length - 1) {
//        linkList(i).next =linkList(i + 1)
//      }
//    }
//    linkList(0)
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
