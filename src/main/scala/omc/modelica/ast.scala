package omc.modelica

object ast {
  case class MoRecord(name:String, fields:Map[String, Any]) {
    def get(field:String): Option[Any] = fields.get(field)
  }
}
