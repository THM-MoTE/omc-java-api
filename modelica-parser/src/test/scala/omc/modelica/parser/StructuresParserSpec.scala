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
  it should "convert lists if values are all the same type" in {
    val expr = "{5,6,7,8,9}"
    val erg = p.typedList[Double](expr)
    erg shouldBe a [Success[_]]
    erg.get shouldBe (List(5,6,7,8,9).map(_.toDouble))
  }
  it should "not convert the list if values aren't the  same type" in {
    val expr = "{5,6,7,'test', 'blup'}"
    val erg = p.typedList[Double](expr)
    erg shouldBe a [Failure[_]]
    val Failure(ex) = erg
    ex shouldBe a [IllegalArgumentException]
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
    rec.get[Any]("i").get shouldBe 45.89
    rec.get[Any]("resultFile").get shouldBe "a/b/results.mat"
  }

  it should "parse complex records" in {
    val complex = """record SimulationResult
                    |resultFile = "«DOCHOME»/Test_res.mat",
                    |simulationOptions = "startTime = 0.0",
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

    val recComplex = p.parseWith(p.record, complex).get
    recComplex.name shouldBe "SimulationResult"
    recComplex.get[Any]("timeCompile").get shouldBe 0.530051523
    recComplex.get[Any]("outputFormat").get shouldBe "mat"
    recComplex.get[Any]("method").get shouldBe "dassl"
  }

  it should "parse lists inside of records" in {
    val record =
      """record test
        |a = 5,
        |xs = {3.0,4.5,5.0}
        |end test;
        |""".stripMargin
    val rec = p.parseWith(p.record, record).get
    rec.get[Any]("xs").get shouldBe List(3.0,4.5,5.0)
  }
}
