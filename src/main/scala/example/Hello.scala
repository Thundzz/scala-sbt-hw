package example

import org.json4s._
import org.json4s.jackson.Serialization
import org.json4s.jackson.Serialization.{read, write}
import scala.util.Try

object A {
    implicit val formats = Serialization.formats(NoTypeHints)

    def unapply(str : String) : Option[A] = 
        Try(read[A](str)).toOption
}

object B {
    implicit val formats = Serialization.formats(NoTypeHints)

    def unapply(str : String) : Option[B] = 
        Try(read[B](str)).toOption
}

case class A(a : String)
case class B(b : String, c : Option[Int])


object Hello extends App {
  /*
  val string= """{
    "b" : "azjiazdi",
    "c" : 3
  }
  """
  */
  val json = """
  {
    "b" : "bonjour theo",
    "c" : 3
  }
  """

  json match {
    case A(a) => println(s"It was an A : $a")
    case B(b) => println(s"It was a B : $b")
    case msg => println(s"I could not understand what that was : $msg")
  }
  
}

