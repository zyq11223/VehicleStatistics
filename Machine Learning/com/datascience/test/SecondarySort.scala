package com.datascience.test

/**
  * Created by yqs on 2017/11/13.
  */
class SecondarySort(val first:String, val second:String) extends Ordered[SecondarySort] with Serializable{

  def compTo(one:String,another:String):Int = {
    val len = one.length -1
    val v1 = one.toCharArray
    val v2 = another.toCharArray
    for(i <- 0 to len){
      val c1 = v1(i)
      val c2 = v2(i)
      if(c1 != c2) return c1 -c2
    }
    return 0
  }

  override def compare(that: SecondarySort): Int = {
  val minus = compTo(this.first,that.first)
  if(minus !=0) return  minus
  return  -compTo(this.second,that.second)
  }

  override def equals(obj:Any) :Boolean  = {
    if(!obj.isInstanceOf[SecondarySort]) return false
    val obj2 = obj.asInstanceOf[SecondarySort]
    return (this.first==obj2.first) && (this.second==obj2.second)
  }

  override def toString :String = {
    first +" "+ second
  }

  override  def hashCode :Int = {
    return this.first.hashCode()+this.second.hashCode();
  }
}
