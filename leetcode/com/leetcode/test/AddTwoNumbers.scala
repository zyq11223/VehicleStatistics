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

  def convert(s:String):Array[Char] = {
    val aChar = new Array[Char](s.length)
    for (i<- 0 until s.length)  aChar(i) = s.charAt(i)
    aChar
  }

  def addTwoNumbers(l1: ListNode, l2: ListNode): ListNode = {
    val sum = toInt(l1) + toInt(l2)
    val sumString = String.valueOf(sum)
    val linkList = new Array[ListNode](sumString.length)
    var j = 0
    for (i <- (0 to sumString.length - 1).reverse) {
      val c = sumString.charAt(i).toString
      if(isIntByRegex(c)){
        linkList(j) = new ListNode(Integer.parseInt(sumString.charAt(i).toString))
        j = j+ 1
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
    while (temp!= null  &&( temp._x != 0||temp.next != null)) {
      nums += temp._x * pow(10,i)
      i = i+1
      temp = temp.next
      }
    nums
  }

  def main(args: Array[String]) {
    val listNode1 = Array[Int](9)
    val linkList1 = new Array[ListNode](listNode1.length)
    for (i <- 0 to listNode1.length - 1) linkList1(i) = new ListNode(listNode1(i))
    for (i <- 0 until listNode1.length) if (i + 1 <= linkList1.length - 1) linkList1(i).next = linkList1(i + 1)

    val listNode2 = Array[Int](1, 9, 9, 9, 9, 9, 9, 9, 9, 9)
    val linkList2 = new Array[ListNode](listNode2.length)
    for (i <- 0 to listNode2.length - 1) linkList2(i) = new ListNode(listNode2(i))
    for (i <- 0 until listNode2.length) if (i + 1 <= linkList2.length - 1) linkList2(i).next = linkList2(i + 1)
    addTwoNumbers(linkList1(0), linkList2(0))
  }
}
