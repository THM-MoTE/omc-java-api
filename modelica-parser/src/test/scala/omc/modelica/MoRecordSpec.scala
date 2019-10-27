package omc.modelica

import ast.MoRecord
import org.scalatest._
import scala.util._

class MoRecordSpec
    extends FlatSpec
    with Matchers {
  "The Record class" should "return only values that match the given type" in {
    val record = MoRecord("test", Map("t" -> 5.0, "s" -> "blup"))
    val opt1 = record.get[AnyVal]("t")
    opt1 shouldBe ('defined)
    opt1.get shouldBe a [AnyVal]
    val opt2 = record.get[Double]("t")
    opt2 shouldBe ('defined)
    opt2.get shouldBe a [Double]
  }
}
