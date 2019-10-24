package omc.modelica.parser

import omc.modelica.ast._

trait StructuresParser extends PrimitiveParser {
  def list: Parser[List[Any]] = "{" ~> repsep((primitives | list), ",") <~ "}"

  def record: Parser[MoRecord] = ("record" ~> ident) ~ (repsep(recordField, ",") <~ "end" <~ ident <~ ";") ^^ {
    case name~fields => MoRecord(name, fields.toMap)
  }

  def recordField: Parser[(String, Any)] = ((ident <~ "=") ~ recordValues) ^^ { case k~v => k -> v }
  def recordValues: Parser[Any] = (number | bool | string | list)
}
