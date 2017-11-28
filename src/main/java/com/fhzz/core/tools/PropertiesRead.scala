package com.fhzz.core.tools

import java.io.IOException
import java.util.Properties

/**
  * Created by yqs on 2017/1/16
  * 读取配置文件
  */
class PropertiesRead(var proName: String = "", var PROPERTIES: Properties = null) {

  def this(proName: String) {
    this()
    this.proName = proName
    this.PROPERTIES = PropertiesConfig.load(proName + ".properties")
  }

  def get(key: String, defaultValue: String): String = {
    if (key == null) {
      return defaultValue
    }
    val value = PROPERTIES.getProperty(key, defaultValue)

    try {
      return new String(value.getBytes("ISO8859-1"), "UTF-8")
    }
    catch {
      case e: IOException => e.printStackTrace()
        return ""
    }
  }

  def get(key: String): String = {
    get(key, "")
  }
}

