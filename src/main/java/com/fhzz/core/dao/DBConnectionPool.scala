package com.fhzz.core.dao


import java.util.ResourceBundle
import java.util.LinkedList
import java.sql.DriverManager
import java.sql.Connection

import com.fhzz.core.constants.Constants
import com.fhzz.core.tools.PropertiesRead

/**
  * 数据库连接池工具类
  * 语言：scala
  * 时间：2016-07-09
  */
object DBConnectionPool {
  private val PROPERTIESREAD: PropertiesRead = new PropertiesRead("jdbc")

  private val max_connection = PROPERTIESREAD.get(Constants.MAXCONNECTION) //连接池总数
  private val connection_num = PROPERTIESREAD.get(Constants.CONNECTIONNUM) //连接池总数
  private var current_num = 0 //当前连接池已产生的连接数
  private val pools = new LinkedList[Connection]() //连接池
  private val driver = PROPERTIESREAD.get(Constants.DRIVER_CLASS)
  private val url = PROPERTIESREAD.get(Constants.DRIVER_URL)
  private val username =  PROPERTIESREAD.get(Constants.USER_NAME)
  private val password =  PROPERTIESREAD.get(Constants.PASSWORD)

  /**
    * 加载驱动
    */
  private def before() {
    if (current_num > max_connection.toInt && pools.isEmpty()) {
      print("busyness")
      Thread.sleep(2000)
      before()
    } else {
      Class.forName(driver)
    }
  }
  /**
    * 获得连接
    */
  private def initConn(): Connection = {
    val conn = DriverManager.getConnection(url, username, password)
    conn
  }
  /**
    * 初始化连接池
    */
  private def initConnectionPool(): LinkedList[Connection] = {
    AnyRef.synchronized({
      if (pools.isEmpty()) {
        before()
        for (i <- 1 to connection_num.toInt) {
          pools.push(initConn())
          current_num += 1
        }
      }
      pools
    })
  }
  /**
    * 获得连接
    */
  def getConn():Connection={
    initConnectionPool()
    pools.poll()
  }
  /**
    * 释放连接
    */
  def releaseCon(con:Connection){
    pools.push(con)
  }

}