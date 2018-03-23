name := "lagom-first"

version := "0.1"

scalaVersion := "2.12.4"

organization in ThisBuild := "com.example"
version in ThisBuild := "1.0-SNAPSHOT"

// the Scala version that will be used for cross-compiled libraries
scalaVersion in ThisBuild := "2.12.4"

val macwire = "com.softwaremill.macwire" %% "macros" % "2.3.0" % "provided"
val scalaTest = "org.scalatest" %% "scalatest" % "3.0.4" % Test

lazy val `first-lagom` = (project in file("."))
 .aggregate(`first-lagom-api`, `first-lagom-impl`)

lazy val `first-lagom-api` = (project in file("first-lagom-api"))
 .settings(
  libraryDependencies ++= Seq(
   lagomScaladslApi
  )
 )

lazy val `first-lagom-impl` = (project in file("first-lagom-impl"))
 .enablePlugins(LagomScala)
 .settings(
  libraryDependencies ++= Seq(
   lagomScaladslTestKit,
   macwire,
   scalaTest,
 //imple depends on api thats why provided
   "com.example" %% "hello-lagom-api" % "1.0-SNAPSHOT"
  )
  )
 .settings(lagomForkedTestSettings: _*)
 .dependsOn(`first-lagom-api`)

//adding dependencies of external service by using its jar and organization name
/**
  * hello-lagom is .named("") in overrided descriptor
  */
lazy val serviceImport = lagomExternalScaladslProject("hello-lagom", "com.example" %% "hello-lagom-impl" % "1.0-SNAPSHOT")
//service locator now just inject service locator to call external service methods
lagomServiceLocatorPort in ThisBuild := 2222
lagomServiceGatewayPort in ThisBuild := 3333
