package com.fhzz.core.tools

import java.io.{FileInputStream, IOException}
import java.util.Properties

/**
  * Created by yqs on 2017/1/16
  */
object PropertiesConfig {

  def load(fileName: String): Properties = {
    val p = new Properties()
    if(fileName == null || fileName.trim().length() == 0){
      return p
    }
    try {
//      val path = Thread.currentThread().getContextClassLoader.getResource(fileName).getPath
      val path = System.getProperty("user.dir")+"/src/config/"+fileName
      p.load(new FileInputStream(path))
    }
    catch {
      case e: IOException => e.printStackTrace()
    }
    p
  }
}
