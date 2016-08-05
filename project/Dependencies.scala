import sbt._

object Dependencies {

  val akkaV = "2.3.9"
  val sprayV = "1.3.3"

  val sprayDeps = Seq(
    "io.spray" %% "spray-can" % sprayV,
    "io.spray" %% "spray-routing" % sprayV,
    "io.spray" %% "spray-testkit" % sprayV % "test",
    "io.spray" %%  "spray-json" % "1.3.2"
  )

  val akkaDeps = Seq(
    "com.typesafe.akka" %% "akka-actor" % akkaV,
    "com.typesafe.akka" %% "akka-testkit" % akkaV % "test"
  )

  val testDeps = Seq(
    "org.specs2" %% "specs2-core" % "3.8.4" % "test"
  )

  val kafkaDeps = Seq(
    "org.apache.kafka" % "kafka_2.11" % "0.10.0.0"
  )

  val jsonDeps = Seq(
    "com.owlike" % "genson-scala_2.11" % "1.4"
  )
}