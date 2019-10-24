package omc.modelica.parser

import org.scalatest._
import scala.util._

class StructuresParserSpec
  extends FlatSpec
    with Matchers {

  val p = new StructuresParser() {}
  "The StructuresParser" should "parse simple arrays" in {
    val ar = "{456, -342.0, 9}"
    val empty = "{}"
    val differentPrimitives = "{Modelica.SIUnits, 56.0, \"string\"}"
    p.parseWith(p.list, ar).get shouldBe List(456.0, -342.0, 9.0)
    p.parseWith(p.list, empty).get shouldBe List.empty[Any]
    p.parseWith(p.list, differentPrimitives).get shouldBe List("Modelica.SIUnits", 56.0, "string")
  }
  it should "parse nested arrays" in {
    val ar  ="{456, {\"test\", mani},{}, 789.34}"
    p.parseWith(p.list, ar) shouldBe a [Success[_]]
  }
}
