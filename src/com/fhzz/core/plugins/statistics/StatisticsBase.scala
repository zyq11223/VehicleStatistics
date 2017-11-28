package com.fhzz.core.plugins.statistics

import java.util.concurrent.{ConcurrentLinkedQueue, ExecutorService, Executors}

import com.fhzz.core.plugins.Plugins
import net.sf.json.JSONObject
import org.apache.log4j.Logger

import scala.util.control.Breaks


/**
  * Created by yqs on 2017/2/8
  */
abstract class StatisticsBase extends Plugins {

  private val LOG = Logger.getLogger(getClass())

  /**
    * 执行统计方法
    */
  def doStatistics(datas: java.util.ArrayList[JSONObject]) : Object

  /**
    * 执行写数据
    */
  def doFlushData(datas: java.util.ArrayList[Object])

  /**
    * 加载初始数据
    */
  def loadInitData(): Boolean

  /**
    * 统计等待时间
    */
  var statisticsWaitRate: Int = 1000 // 默认1秒

  /**
    * 提交统计结果等待时间
    */
  var flushWaitRate: Int = 10000 // 默认10秒

  /**
    * 数据源类型
    */
  var sourceType = new String("")

  /**
    * 内存临时区
    */
  var inputDataList = new ConcurrentLinkedQueue[java.util.ArrayList[JSONObject]]()

  /**
    * 统计缓存区
    */
  var statictisCacheDataList = new ConcurrentLinkedQueue[Object]()

  /**
    * 构造函数
    * @param statisticsWaitRate
    * 统计等待时间
    * @param flushWaitRate
    * 提交统计结果等待时间
    * @param sourceType
    * 数据源类型
    */
  def this(statisticsWaitRate: Integer, flushWaitRate: Integer, sourceType: String) {
    this()
    this.statisticsWaitRate = statisticsWaitRate
    this.flushWaitRate = flushWaitRate
    this.sourceType = sourceType
  }

  /**
    * 初始化
    */
  def init(): Boolean = {
    loadInitData()
  }

  def start() = {
    while (true) {
      val inits = init()
      if (inits) Breaks.break()
      else {
        try {
          Thread.sleep(flushWaitRate)
        } catch {
          case e: InterruptedException => LOG.error("StatisticsBase start()" + e, e)
        }
      }
      val pool: ExecutorService = Executors.newFixedThreadPool(2)
      pool.execute(new Statistics())
      pool.execute(new FlushResult())
    }
  }

  /**
    * 处理一条数据
    */
  override  def handle(jsonObject: JSONObject): Unit = {
    // do nothing
  }

  /**
    * 批量处理数据(solr插入后)
    */
  override  def  batchHandle(jsonObjectList: java.util.ArrayList[JSONObject]): Unit = {
    // 不互斥添加
    inputDataList.offer(jsonObjectList)
  }

  /**
    * 统计
    */
  class Statistics extends Runnable {
    def run() {
      while (true) {
        try {
          Thread.sleep(statisticsWaitRate)
          while (!inputDataList.isEmpty()) {
            val pollList = inputDataList.poll()
            val statisticsEntity = doStatistics(pollList)
            statictisCacheDataList.offer(statisticsEntity)
          }
        } catch {
          case e: InterruptedException =>
        }
      }
    }
  }

  /**
    * 写入统计结果
    */
  class FlushResult extends Runnable {
    def run() {
      while (true) {
        try {
          Thread.sleep(flushWaitRate)
          val needToSave = new java.util.ArrayList[Object]()
          while (!statictisCacheDataList.isEmpty()) {
            val poll = statictisCacheDataList.poll()
            needToSave.add(poll)
          }
          doFlushData(needToSave)
        } catch {
          case e: InterruptedException => LOG.error("StatisticsBase FlushResult()" + e, e)
        }
      }
    }
  }
}
