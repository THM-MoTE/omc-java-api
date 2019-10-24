package omc.modelica.parser

import scala.util.parsing.combinator._
import scala.util.Try

trait PrimitiveParser extends JavaTokenParsers {
  def number: Parser[Double] = floatingPointNumber ^^ { _.toDouble }
  def string: Parser[String] = stringLiteral ^^ { str => str.substring(1, str.length-1) }
  def bool: Parser[Boolean] = (
      "true" ^^^ {true}
    | "false"  ^^^ {false}
    )
  def identifer: Parser[String] = """([a-zA-Z][\w.]+)""".r

  def primitives: Parser[Any] = number | string | identifer | bool

  def parseWith[T](p:Parser[T], input:CharSequence): Try[T] =
    parseAll(p, input) match {
      case Success(res,_) => scala.util.Success(res)
      case e:NoSuccess => scala.util.Failure(new IllegalArgumentException("parser error: "+e.msg))
    }
}
