name := "event-driven-orders"

version := "1.0"

scalaVersion := "2.11.8"

val spray = {
  val akkaV = "2.3.9"
  val sprayV = "1.3.3"

  Seq(
    "io.spray" %% "spray-can" % sprayV,
    "io.spray" %% "spray-routing" % sprayV,
    "io.spray" %% "spray-testkit" % sprayV % "test",
    "com.typesafe.akka" %% "akka-actor" % akkaV,
    "com.typesafe.akka" %% "akka-testkit" % akkaV % "test",
    "org.specs2" %% "specs2-core" % "3.8.4"
  )
}

fork in run := true

javaOptions += "-Xmx4G"

import Dependencies._

lazy val orderService = project.in(file("order-service")).settings(
  libraryDependencies ++= (akkaDeps ++ sprayDeps ++ testDeps)
)

lazy val productService = project.in(file("product-service")).settings(
  libraryDependencies ++= (akkaDeps ++ sprayDeps ++ testDeps)
)

lazy val root = project.in(file("."))
  .aggregate(orderService, productService)