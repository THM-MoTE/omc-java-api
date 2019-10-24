package omc.modelica.parser

import org.scalatest._
import scala.util._

class PrimitiveParserSpec
    extends FlatSpec
    with Matchers {

  val p = new PrimitiveParser() {}

  "The PrimitiveParser" should "parse identifiers" in {
    val s = "Modelica.SIUnits"
    val s2 = "M432lica.Electr76cl"
    p.parseWith(p.identifer, s) shouldBe a [Success[_]]
    p.parseWith(p.identifer, s2) shouldBe a [Success[_]]
  }
  it should "parse numbers" in {
    val n = "-456.78923"
    val n2 = "56784.0923"
    p.parseWith(p.number, n) shouldBe a [Success[_]]
    p.parseWith(p.number, n2) shouldBe a [Success[_]]
  }
  it should "parse strings" in {
    val s = "\"blup\""
    val s2 = "\"der\\\"bi\""
    p.parseWith(p.string, s) shouldBe a [Success[_]]
    val res = p.parseWith(p.string, s2).get
    res shouldBe "der\\\"bi"
  }
  it should "parse boolean literals" in {
    p.parseWith(p.bool, "true").get shouldBe (true)
    p.parseWith(p.bool, "false").get shouldBe (false)
  }

}
