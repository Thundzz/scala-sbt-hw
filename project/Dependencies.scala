import sbt._

object Dependencies {
  lazy val scalaTest = "org.scalatest" %% "scalatest" % "3.0.5"
  lazy val json4s = "org.json4s" %% "json4s-jackson" % "3.6.2"
  lazy val akkaActor = "com.typesafe.akka" %% "akka-actor" % "2.5.18"
  lazy val akkaTestKit = "com.typesafe.akka" %% "akka-testkit" % "2.5.18"
}
