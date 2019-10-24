package omc.modelica.parser

trait StructuresParser extends PrimitiveParser {
  def list: Parser[List[Any]] = "{" ~> repsep((primitives | list), ",") <~ "}"
}
