package com.fhzz.core.tools

import java.sql.{Connection, DriverManager, ResultSet, Statement}
import java.util

import com.fhzz.core.constants.Constants

/**
  * Created by yqs on 2017/2/6
  */
object JdbcUtils {

  private val PROPERTIESREAD: PropertiesRead = new PropertiesRead("jdbc")

  /**
    * 获取连接
    * @param driverClass
    * 驱动类
    * @param url
    * URL
    * @param user
    * 用户名
    * @param password
    * 密码
    */
  def getConnection(driverClass: String, url: String, user: String, password: String) : Connection = {
    var connection: Connection = null
    try {
      Class.forName(driverClass)
      connection = DriverManager.getConnection(url, user, password)
    }
    catch {
      case e: Exception => e.printStackTrace
    }
    connection
  }

  /**
    * 关闭连接
    * @param conn
    * 连接
    * @param stmt
    *
    * @param res
    * 结果集
    */
  def close(conn: Connection, stmt: Statement, res: ResultSet) {
    try {
      if (res != null) res.close()

      if (stmt != null) stmt.close()

      if (conn != null) conn.close()
    }
    catch {
      case e: Exception => e.printStackTrace
    }
  }

  /**
    * 全国车牌号 hphm1 --省市组成的映射
    */
  def queryAttribute() : util.HashMap[String,util.HashMap[String,String]] = {
    val map = new util.HashMap[String,util.HashMap[String,String]] ()
    val driverClass: String = PROPERTIESREAD.get(Constants.DRIVER_CLASS,"com.mysql.jdbc.Driver")
    val url: String = PROPERTIESREAD.get(Constants.DRIVER_URL,"jdbc:mysql://master1:3306/vehicle?useUnicode=true&characterEncoding=UTF-8")
    val user: String = PROPERTIESREAD.get(Constants.USER_NAME,"root")
    val password: String = PROPERTIESREAD.get(Constants.PASSWORD,"123456")

    val conn: Connection = JdbcUtils.getConnection(driverClass, url, user, password)
    val statement = conn.createStatement()
    val resultSet = statement.executeQuery("select city_key,city_seq,city_name,city_key,provinceid,letter from carbelong")

    while (resultSet.next()) {
      val key =(resultSet.getString("city_key"))
      val value = new util.HashMap[String,String]()
      value.put("city_key",resultSet.getString("city_key"))
      value.put("city_name",resultSet.getString("city_name"))
      value.put("sequence",resultSet.getString("city_seq"))
      value.put("province_id",resultSet.getString("provinceid"))
      value.put("letter",resultSet.getString("letter"))
      map.put(key,value)
    }
    close(conn,statement,resultSet)
    map
  }

  /**
    * 全国简称 --省市组成的映射
    */
  def getUnknownMap(): util.HashMap[String, util.HashMap[String,String]] = {
    val map = new util.HashMap[String, util.HashMap[String,String]]()
    val driverClass: String = PROPERTIESREAD.get(Constants.DRIVER_CLASS,"com.mysql.jdbc.Driver")
    val url: String = PROPERTIESREAD.get(Constants.DRIVER_URL,"jdbc:mysql://master1:3306/vehicle?useUnicode=true&characterEncoding=UTF-8")
    val user: String = PROPERTIESREAD.get(Constants.USER_NAME,"root")
    val password: String = PROPERTIESREAD.get(Constants.PASSWORD,"123456")

    val conn: Connection = JdbcUtils.getConnection(driverClass, url, user, password)
    val statement = conn.createStatement()
    val resultSet = statement.executeQuery("select city_key,city_seq,city_name,city_key,provinceid from carbelong where provinceid = 0 OR provinceid = -1")

    while (resultSet.next()) {
      val key = (resultSet.getString("city_key"))
      val value = new util.HashMap[String,String]()
      value.put("city_name",resultSet.getString("city_name"))
      value.put("province_id",resultSet.getString("city_seq"))
      value.put("sequence",String.valueOf(-1))
      map.put(key,value)
    }
    close(conn,statement,resultSet)
    map
  }

  def main(args: Array[String]) {
    val map = JdbcUtils.queryAttribute()
    System.out.println(map)
  }
}
