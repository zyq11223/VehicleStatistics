package com.leetcode.test



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
  def pow(x: Int, y: Int):Int = if(y-1>=0) x* pow(x,y-1)else return 1

  def addTwoNumbers(l1: ListNode, l2: ListNode): ListNode = {
    val sum = toInt(l1) + toInt(l2)
    val sumString = String.valueOf(sum)
    val linkList = new Array[ListNode](sumString.length)
    var j = 0
    for (i <- (0 to sumString.length - 1).reverse) {
      val c = sumString.charAt(i).toString
      if(isIntByRegex(c)){
        linkList(j) = new ListNode(Integer.parseInt(sumString.charAt(i).toString))
        j += 1
      }
    }
    for (i <- 0 until linkList.length) {
      if (i + 1 <= linkList.length - 1) {
        linkList(i).next =linkList(i + 1)
      }
    }
    linkList(0)
  }
  /**
    * 转化为Int
    */
  def toInt(l: ListNode):Int = {
    var temp = l
    var nums = 0
    var i = 0
    while (temp!= null  && temp.next != null) {
      nums += temp._x * pow(10,i)
      i = i+1
      temp = temp.next
      }
    println(nums)
    nums
  }

  def main(args: Array[String]) {
    val listNode1 = new ListNode(9)
    val listNode2 = Array[Int](1, 9, 9, 9, 9, 9, 9, 9, 9, 9)
    val linkList = new Array[ListNode](listNode2.length)
    for (i <- 0 to 9) linkList(i) = new ListNode(listNode2(i))

    for (i <- 0 until listNode2.length) {
      if (i + 1 <= linkList.length - 1) linkList(i).next = linkList(i + 1)
    }
    addTwoNumbers(listNode1, linkList(0))
  }
}
