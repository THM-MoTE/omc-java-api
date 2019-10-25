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
    val s3 = "'blup\\\' dub'"
    val s4 = "''"
    p.parseWith(p.string, s) shouldBe a [Success[_]]
    val res = p.parseWith(p.string, s2).get
    res shouldBe "der\\\"bi"
    p.parseWith(p.string, s3).get shouldBe "blup\\\' dub"
    p.parseWith(p.string, s4) shouldBe a [Success[_]]
    p.parseWith(p.string, "'.*'") shouldBe a [Success[_]]
  }
  it should "parse boolean literals" in {
    p.parseWith(p.bool, "true").get shouldBe (true)
    p.parseWith(p.bool, "false").get shouldBe (false)
  }

  it should "parse any primitive" in {
    val n = "-456.8"
    val s = "\'dub\'"
    val s2 = "\"dub\""
    val t = "true"
    p.parseWith(p.primitives, n).get shouldBe -456.8
    p.parseWith(p.primitives, s).get shouldBe "dub"
    p.parseWith(p.primitives, s2).get shouldBe "dub"
    p.parseWith(p.primitives, t).get shouldBe true
  }

}
