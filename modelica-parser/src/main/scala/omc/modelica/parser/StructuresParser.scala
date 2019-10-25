package omc.modelica.parser

import omc.modelica.ast._

import scala.util.Try

trait StructuresParser extends PrimitiveParser {
  def list: Parser[List[Any]] = "{" ~> repsep((primitives | list), accept(',')) <~ "}"

  def record: Parser[MoRecord] = ("record" ~> ident) ~ (repsep(recordField, ",") <~ "end" <~ ident <~ ";") ^^ {
    case name~fields => MoRecord(name, fields.toMap)
  }

  def recordField: Parser[(String, Any)] = ((ident <~ "=") ~ recordValues) ^^ { case k~v => k -> v }
  def recordValues: Parser[Any] = (number | bool | string | list)

  def stringList(expr:String): Try[List[String]] = parseWith(list, expr).map(StructuresParser.anyListToString)
  def flattenedStringList(expr:String): Try[List[String]] = flattenedList(expr).map(StructuresParser.anyListToString)
  def flattenedList(expr:String): Try[List[Any]] = {
    def doFlatten(xs:List[Any]): List[Any] = xs match {
      case (x:List[Any])::xs => doFlatten(x) ++ doFlatten(xs)
      case x::xs => x :: doFlatten(xs)
      case Nil => Nil
    }
    parseWith(list, expr).map(doFlatten)
  }
}

object StructuresParser {
  val anyListToString: List[Any] => List[String] = xs => xs.map(_.toString)
  def getInstance: StructuresParser = new StructuresParser() {}
}
