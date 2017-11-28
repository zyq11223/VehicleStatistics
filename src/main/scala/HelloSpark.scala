package main.scala
import scala.math.ceil
import org.apache.spark.{SparkContext, SparkConf}

/**
  * Created by yqs
  * @description:
  * @edit :
  * @date:  2016/12/26
  * @modify :
  * @since  : V1.0
  * @copyright : FiberHome FHZ Telecommunication Technologies Co.Ltd.
  */
object HelloSpark {
  def main(args: Array[String]) {
    val conf = new SparkConf().setAppName("yqs")
    val sc: SparkContext = new SparkContext(conf)
    sc.textFile("/user/root/spark/README.md").flatMap(line => line.split(" ")).
      map(word => (word, 1)).reduceByKey((a, b) => a + b).map(x => (x._2, x._1)).sortByKey(false).map(x => (x._2, x._1)).
      saveAsTextFile("/user/root/sparkOut2")
    val num = 3.14
    val fun = ceil _
    if (sc.isStopped) {
      sc.stop();
    }
  }
}
