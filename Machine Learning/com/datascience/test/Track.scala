package com.datascience.test

/**
  * Created by yqs on 2017/11/13.
  */
class Track extends  java.io.Serializable  {

  var productNo : String = ""
  var lacId : String  = ""
  var moment : String = ""
  var startTime : String = ""
  var userId  : String = ""
  var countyId : String = ""
  var staytime : Int  = 0
  var cityId  : String = ""

  def this(_productNo: String, _lacId: String,_moment: String, _startTime: String,_userId: String,_countyId: String,_staytime: Int, _cityId: String)
  {
    this()
    this.productNo = _productNo
    this.lacId = _lacId
    this.moment = _moment
    this.startTime = _startTime
    this.userId = _userId
    this.countyId = _countyId
    this.staytime = _staytime
    this.cityId = _cityId
  }

  override def toString :String = {
      productNo +" "+ lacId + " "+ moment + " " + startTime + "  " + userId  + "  "+ countyId + "  "+ staytime + "  " + cityId
  }
}
