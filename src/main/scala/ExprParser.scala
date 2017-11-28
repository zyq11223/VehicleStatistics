package main.scala

import scala.util.parsing.combinator.RegexParsers

/**
  * Created by yqs on 2017/1/16
  */
class ExprParser extends RegexParsers{

  val number = "[0-9]+".r

  def expr : Parser[Any] = term ~ opt (("+" | "-") ~ expr)

  def  term : Parser[Any] = factor ~ rep("*" ~ factor)

  def factor : Parser[Any] = number | "(" ~ expr ~")"

}
