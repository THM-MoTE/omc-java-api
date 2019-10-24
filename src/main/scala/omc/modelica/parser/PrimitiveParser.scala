package omc.modelica.parser

import scala.util.parsing.combinator._
import scala.util.Try

trait PrimitiveParser extends JavaTokenParsers {
  def number: Parser[Double] = floatingPointNumber ^^ { _.toDouble }
  def string: Parser[String] = {
    //parser for '<...>' taken from original stringLiteral.
    //source: https://github.com/scala/scala-parser-combinators/blob/1.2.x/shared/src/main/scala/scala/util/parsing/combinator/JavaTokenParsers.scala
    (stringLiteral |
    ("\'"+"""([^"\x00-\x1F\x7F\\]|\\[\\'"bfnrt]|\\u[a-fA-F0-9]{4})*"""+"\'").r) ^^ { str => str.substring(1, str.length-1) }
  }

  def bool: Parser[Boolean] = (
      "true" ^^^ {true}
    | "false"  ^^^ {false}
    )
  def identifer: Parser[String] = """([a-zA-Z][\w.]+)""".r

  def primitives: Parser[Any] = number | bool | identifer | string

  def parseWith[T](p:Parser[T], input:CharSequence): Try[T] =
    parseAll(p, input) match {
      case Success(res,_) => scala.util.Success(res)
      case e:NoSuccess => scala.util.Failure(new IllegalArgumentException("parser error: "+e.msg))
    }
}
