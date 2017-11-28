package com.fhzz.core.kafka

import java.util.Properties

import com.fhzz.core.constants.Constants
import com.fhzz.core.tools.{PropertiesRead, StringUtils}
import kafka.consumer.{Consumer, ConsumerConfig, ConsumerConnector, KafkaStream}

import scala.collection._


/**
  * Created by yqs on 2017/1/16
  * Spark 消费kafka 客户端
  */
class SparkAndKfkConsumer {
  /**
    * 消费者连接器
    */
  var consumerConnector: ConsumerConnector = null

  /**
    * kafka字节流
    */
  var kafkaStream: Map[String, List[KafkaStream[Array[Byte], Array[Byte]]]] = null

  /**
    * 配置文件
    */
  val pread: PropertiesRead = new PropertiesRead("kafka")

  /**
    * 获取配置属性
    */
  def createConsumerConfig(groupId: String): Properties = {
    val props = new Properties
    props.put(Constants.KAFKA_BOOTSTRAP_SERVERS, pread.get(Constants.KAFKA_BOOTSTRAP_SERVERS))
    props.put(Constants.KAFKA_GROUP_ID, groupId)
    props.put(Constants.KAFKA_ENABLE_AUTO_COMMIT, pread.get(Constants.KAFKA_ENABLE_AUTO_COMMIT))
    props.put(Constants.KAFKA_AUTO_COMMIT_INTERVAL_MS, pread.get(Constants.KAFKA_AUTO_COMMIT_INTERVAL_MS))
    props.put(Constants.KAFKA_SESSION_TIMEOUT_MS, pread.get(Constants.KAFKA_SESSION_TIMEOUT_MS))
    props.put(Constants.KAFKA_KEY_DESERIALIZER, pread.get(Constants.KAFKA_KEY_DESERIALIZER))
    props.put(Constants.KAFKA_VALUE_DESERIALIZER, pread.get(Constants.KAFKA_VALUE_DESERIALIZER))
    props.put(Constants.KAFKA_AUTO_OFFSET_RESET, pread.get(Constants.KAFKA_AUTO_OFFSET_RESET))
    props.put(Constants.KAFKA_ZOOKEEPER_CONNECT, pread.get(Constants.KAFKA_ZOOKEEPER_CONNECT))
    props.put(Constants.KAFKA_CONNECTIONS_MAX_IDLE_MS, pread.get(Constants.KAFKA_CONNECTIONS_MAX_IDLE_MS))
    props.put(Constants.KAFKA_CONSUMER_TIMEOUT_MS, pread.get(Constants.KAFKA_CONSUMER_TIMEOUT_MS))
    props.put(Constants.FETCH_MESSAGE_MAX_BYTES, pread.get(Constants.FETCH_MESSAGE_MAX_BYTES))
    props.put(Constants.ZOOKEEPER_SESSION_TIMEOUT_MS, pread.get(Constants.ZOOKEEPER_SESSION_TIMEOUT_MS))
    props.put(Constants.ZOOKEEPER_CONNECTION_TIMEOUT_MS, pread.get(Constants.ZOOKEEPER_CONNECTION_TIMEOUT_MS))
    props.put(Constants.ZOOKEEPER_SYNC_TIME_MS, pread.get(Constants.ZOOKEEPER_SYNC_TIME_MS))
    props.put(Constants.REBALANCE_BACKOFF_MS, pread.get(Constants.REBALANCE_BACKOFF_MS))
    props.put(Constants.REBALANCE_MAX_RETRIES, pread.get(Constants.REBALANCE_MAX_RETRIES))
    props.put(Constants.REFRESH_LEADER_BACKOFF_MS, pread.get(Constants.REFRESH_LEADER_BACKOFF_MS))
    props
  }

  /**
    * 获取消费者连接器
    */
  def getConsumerConnector(): ConsumerConnector = {
    consumerConnector
  }


  /**
    * 获取字节流
    */
  def getKafkaStream(): Map[String, List[KafkaStream[Array[Byte], Array[Byte]]]] = {
    kafkaStream
  }

  /**
    * 默认构造函数
    */
  def this(topicName: String, groupId: String, numThreads: Int) {
    this()
    if (StringUtils.isNullOREmpty(topicName) && StringUtils.isNullOREmpty(groupId)) {
      throw new IllegalArgumentException("Args is illegal,Please check!")
    }
    val consumerConfig = new ConsumerConfig(createConsumerConfig(groupId))
    this.consumerConnector = Consumer.create(consumerConfig)
    val topicCountMap = Map(topicName -> numThreads)
    this.kafkaStream = consumerConnector.createMessageStreams(topicCountMap)
  }
}

object
SparkAndKfkConsumer {
  def main(args: Array[String]) {
    val topicName: String = "jdctx"
    val groupId: String = "yqstest3"
    val numThreads: Int = 1
    val k = new SparkAndKfkConsumer(topicName, groupId, numThreads)
    val lines = k.getKafkaStream().map(_._2)
    println(lines.toString())
  }
}


