import Dependencies.projectDependencies

version := "1.0-SNAPSHOT"
scalaVersion := "2.13.12"

lazy val root = (project in file("."))
  .settings(
    name := """MeetUpApp""",
    organization := "ab",
    libraryDependencies ++= projectDependencies,
    addCompilerPlugin("org.typelevel" %% "kind-projector"     % "0.13.2" cross CrossVersion.full),
    addCompilerPlugin("com.olegpy"    %% "better-monadic-for" % "0.3.1")
  )
