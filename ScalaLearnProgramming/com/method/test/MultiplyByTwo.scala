package com.method.test

/**
  * Created by Administrator on 2017/7/18.
  */
object MultiplyByTwo {
   def multiplyByTwo(x:Int):Int = {
     println("Inside multplyByTwo")
     x*2
   }

  def main(args: Array[String]) {
     val r = multiplyByTwo(5)
    println(r)

  }
}
