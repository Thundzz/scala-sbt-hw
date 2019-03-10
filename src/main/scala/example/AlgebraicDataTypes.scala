package example

import org.json4s.JsonAST._
import org.json4s.{CustomSerializer, Formats, NoTypeHints}
import org.json4s.jackson.Serialization
import org.json4s.jackson.Serialization.read


sealed trait Genre
case class Rock(category: Int) extends Genre
case class Metal(subcategory: String) extends Genre

object GenreSerializer {
  def deserialize(jobject: JValue, format: Formats): Genre = {
    jobject match {
      case JObject(JField("category", JInt(s)) :: Nil) => Rock(s.toInt)
      case JObject(JField("subcategory", JString(s)) :: Nil) => Metal(s)
    }
  }
}

class GenreSerializer2 extends CustomSerializer[Genre](format => ( {
  case x : JValue => GenreSerializer.deserialize(x, format)
}, {
  case x: Genre =>
    JObject(JField("start", JInt(BigInt(1))) ::
      JField("end", JInt(BigInt(2))) :: Nil)
}
))


object MyMain extends App {
  def adt(): Unit = {
    implicit val formats: AnyRef with Formats = Serialization.formats(NoTypeHints) + new GenreSerializer2
    val json =
      """
      {
        "category" : 3
      }
      """
    val json2 =
      """
      {
        "subcategory" : "3"
      }
      """
    println(read[Genre](json))
    println(read[Genre](json2))
  }

  adt()
}