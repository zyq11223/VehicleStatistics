package main.scala

/**
  * Created by Administrator on 2016/12/12.
  */
object ScalaTest {

  def numsFrom(n: BigInt): Stream[BigInt] = n #:: numsFrom(n + 1)

  def main(args: Array[String]) {
    println("hello scala")
    (1 to 9).filter(_ % 2 == 0).map("*" * _).foreach(println _)
    "Mary has a little lamb".split(" ").sortWith(_.length < _.length).foreach(println _)
    (List(5.0, 20.0, 9.95) zip List(10, 2)).foreach(println _)
    (List(5.0, 20.0, 9.95) zipAll(List(10, 2, 4), 0.0, 1)).foreach(println _)
    val squares = numsFrom(1).map(x => x * x)
    (squares.take(5).force).foreach(println _)

    //    for (i <- (0 until (100)).par) print(i + " ")

    val ch: Char = '-'
    val sign = ch match {
      case '+' => 1
      case '-' => -1
      case _ => 0
    }
    println(sign)
  }
}
