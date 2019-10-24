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

  it should "parse simple records" in {
    val empty = """record test
        |
        |
        |end test;
        |""".stripMargin
    val simple = """record test
        | i = 45.89,
        | s = "blup",
        | resultFile = "a/b/results.mat"
        |end test;""".stripMargin
    p.parseWith(p.record, empty) shouldBe a [Success[_]]
    val rec = p.parseWith(p.record, simple).get
    rec.name shouldBe "test"
    rec.get("i").get shouldBe 45.89
    rec.get("resultFile").get shouldBe "a/b/results.mat"
    val complex = """record SimulationResult
        |resultFile = "«DOCHOME»/Test_res.mat",
        |simulationOptions = "startTime = 0.0,
        |stopTime = 3.0,
        |numberOfIntervals = 500,
        |tolerance = 1e-06,
        |method = 'dassl',
        |fileNamePrefix = 'Test',
        |options = '',
        |outputFormat = 'mat',
        |variableFilter = '.*',
        |timeTemplates = 0.028273708,
        |timeCompile = 0.530051523,
        |timeSimulation = 0.037270063
        |end SimulationResult;""".stripMargin

//    val recComplex = p.parseWith(p.record, complex).get
//    recComplex.name shouldBe "SimulationResult"
//    recComplex.get("timeCompile").get shouldBe 0.530051523
//    recComplex.get("outputFormat").get shouldBe "mat"
//    recComplex.get("method").get shouldBe "dassl"
  }
}
