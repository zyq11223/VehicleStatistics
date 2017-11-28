package com.fhzz.core.constants

/**
  * Created by yqs on 2017/1/4
  * 常量类
  */
object Constants {

  /**
    * cityOfNation路径
    */
   val CITY_OF_NATION :String  ="src/config/cityOfNation.xml"

  /**
    * sequence
    */
   val SEQUENCE_NAME  :String = "sequence"

  /**
    * CITY_NAME
    */
   val CITY_NAME  :String = "city_name"

  /**
    * CITY_KEY
    */
   val CITY_KEY   :String = "city_key"

  /**
    * province_id
    */
   val PROVINCE_ID  :String  = "province_id"

  /**
    * 驱动类
    */
   val DRIVER_CLASS   :String  = "mysql.connection.driver.class"

  /**
    * URL
    */
   val DRIVER_URL   :String  = "mysql.connection.url"

  /**
    * 用户名
    */
   val USER_NAME  :String  =  "mysql.connection.username"

  /**
    * 密码
    */
   val PASSWORD  :String  = "mysql.connection.password"

  /**
    * 最大连接数
    */
  val MAXCONNECTION  :String  = "mysql.max.connection"

  /**
    * 连接数
    */
  val CONNECTIONNUM  :String  = "mysql.connection.num"

  /**
    * bootstrap.servers
    */
  val KAFKA_BOOTSTRAP_SERVERS = "bootstrap.servers"

  /**
    * group.id
    */
  val KAFKA_GROUP_ID = "group.id"

  /**
    * enable.auto.commit
    */
  val KAFKA_ENABLE_AUTO_COMMIT = "enable.auto.commit"

  /**
    * auto.commit.interval.ms
    */
  val KAFKA_AUTO_COMMIT_INTERVAL_MS = "auto.commit.interval.ms"

  /**
    * session.timeout.ms
    */
  val KAFKA_SESSION_TIMEOUT_MS = "session.timeout.ms"

  /**
    * key.deserializer
    */
  val KAFKA_KEY_DESERIALIZER = "key.deserializer"

  /**
    * value.deserializer
    */
  val KAFKA_VALUE_DESERIALIZER = "value.deserializer"

  /**
    * auto.offset.reset
    */
  val KAFKA_AUTO_OFFSET_RESET = "auto.offset.reset"

  /**
    * zookeeper.connect
    */
  val KAFKA_ZOOKEEPER_CONNECT = "zookeeper.connect"

  val KAFKA_CONNECTIONS_MAX_IDLE_MS = "connections.max.idle.ms"

  /**
    * consumer.timeout.ms
    */
  val KAFKA_CONSUMER_TIMEOUT_MS = "consumer.timeout.ms"

  /**
    * fetch.message.max.bytes
    */
  val FETCH_MESSAGE_MAX_BYTES = "fetch.message.max.bytes"

  /**
    * zookeeper.session.timeout.ms
    */
  val ZOOKEEPER_SESSION_TIMEOUT_MS = "zookeeper.session.timeout.ms"

  /**
    * zookeeper.connection.timeout.ms
    */
  val ZOOKEEPER_CONNECTION_TIMEOUT_MS = "zookeeper.connection.timeout.ms"

  /**
    * zookeeper.sync.time.ms
    */
  val ZOOKEEPER_SYNC_TIME_MS = "zookeeper.sync.time.ms"


  /**
    * rebalance.backoff.ms
    */
  val REBALANCE_BACKOFF_MS = "rebalance.backoff.ms"

  /**
    * rebalance.max.retries
    */
  val REBALANCE_MAX_RETRIES = "rebalance.max.retries"

  /**
    * refresh.leader.backoff.ms
    */
  val REFRESH_LEADER_BACKOFF_MS = "refresh.leader.backoff.ms"

  /**
    *  zookeeper hosts
    */
  val ZOOKEEPER_QUORUM = "zookeeper.quorum"

}
