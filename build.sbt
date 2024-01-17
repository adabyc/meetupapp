name := """MeetUpApp"""
organization := "ab"
version := "1.0-SNAPSHOT"

scalaVersion := "2.13.12"

lazy val root = (project in file("."))
  .enablePlugins(PlayScala)


val PostgresVersion = "42.3.4"
val SlickVersion = "3.3.3"
val SlickPgVersion = "0.20.3"
val CatsEffectVersion = "3.5.3"

libraryDependencies ++= Seq(
  guice,
  // DB
  "org.postgresql" % "postgresql" % PostgresVersion,
  "com.typesafe.slick" %% "slick" % SlickVersion,
  "com.typesafe.slick" %% "slick-hikaricp" % SlickVersion,
  "com.github.tminglei" %% "slick-pg" % SlickPgVersion,

  // cats

  "org.typelevel" %% "cats-effect" % CatsEffectVersion,
)

