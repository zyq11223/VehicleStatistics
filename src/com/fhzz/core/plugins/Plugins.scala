package com.fhzz.core.plugins

import net.sf.json.JSONObject


/**
  * Created by yqs on 2017/2/8
  */
trait Plugins {
  /**
    * 处理一条数据
    */
  def handle(jsonObject: JSONObject)

  /**
    * 批量处理数据(solr插入后)
    */
  def batchHandle(jsonObjectList: java.util.ArrayList[JSONObject])
}
