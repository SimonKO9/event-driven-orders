organization := "com.github.simonthecat"

name := "event-driven-orders"

version := "1.0.0"

scalaVersion := "2.11.8"

fork in run := true

javaOptions += "-Xmx4G"

import Dependencies._

resolvers += Resolver.bintrayRepo("cakesolutions", "maven")

val commonSettings = Seq(
  resolvers += Resolver.bintrayRepo("cakesolutions", "maven"),
  scalaVersion := "2.11.8"
)

lazy val common = project.in(file("common"))
    .settings(
      libraryDependencies ++= (jsonDeps)
    )
  .settings(commonSettings)

lazy val orderService = project.in(file("order-service"))
  .dependsOn(common)
  .settings(
    commonSettings,
    libraryDependencies ++= (kafkaDeps ++ testDeps)
)

lazy val productService = project.in(file("product-service"))
  .dependsOn(common)
  .settings(
    commonSettings,
    libraryDependencies ++= (kafkaDeps ++ testDeps)
)

lazy val clientApi = project.in(file("client-api"))
  .dependsOn(common)
  .settings(
    commonSettings,
    libraryDependencies ++= (akkaDeps ++ sprayDeps ++ kafkaDeps ++ testDeps)
  )

lazy val root = project.in(file("."))
  .settings(commonSettings)
  .aggregate(orderService, productService, clientApi)