package omc.modelica

object ast {
  case class MoRecord(name:String, fields:Map[String, Any]) {
    def get[T:scala.reflect.ClassTag](field:String): Option[T] =
      fields.get(field)
        .collect {
          case t:T => t
        }
  }
}
