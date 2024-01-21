import sbt._

object Dependencies {

  val PostgresVersion = "42.3.4"
  val SlickVersion = "3.3.3"
  val SlickPgVersion = "0.20.3"
  val CatsEffectVersion = "3.5.3"
  val TapirVersion = "1.9.6"
  val Http4sVersion = "0.23.24"
  val Fs2Version = "3.9.3"
  val LogbackVersion         = "1.2.10"

  val projectDependencies = Seq(
    // DB
    "org.tpolecat" %% "doobie-core"      % "1.0.0-RC4",
    "org.tpolecat" %% "doobie-h2"        % "1.0.0-RC4",          // H2 driver 1.4.200 + type mappings.
    "org.tpolecat" %% "doobie-hikari"    % "1.0.0-RC4",          // HikariCP transactor.
    "org.tpolecat" %% "doobie-postgres"  % "1.0.0-RC4",          // Postgres driver 42.6.0 + type mappings.
    "io.estatico"  %% "newtype"          % "0.4.4",

    // cats
    "org.typelevel" %% "cats-effect" % CatsEffectVersion,

    // tapir
    "com.softwaremill.sttp.tapir" %% "tapir-http4s-server" % TapirVersion,
    "com.softwaremill.sttp.tapir" %% "tapir-swagger-ui-bundle" % TapirVersion,
    "com.softwaremill.sttp.tapir" %% "tapir-json-circe" % TapirVersion,

    "io.circe" %% "circe-generic" % "0.14.6",

    "org.http4s" %% "http4s-ember-server" % Http4sVersion,
    "org.http4s" %% "http4s-ember-client" % Http4sVersion,
    "org.http4s" %% "http4s-circe" % Http4sVersion,
    "org.http4s" %% "http4s-dsl" % Http4sVersion,

    "co.fs2" %% "fs2-core" % Fs2Version,
    "co.fs2" %% "fs2-io" % Fs2Version,

    // misc
    "org.typelevel" %% "log4cats-slf4j"   % "2.6.0",
    "ch.qos.logback" % "logback-classic" % LogbackVersion % Runtime,
    "com.github.pureconfig" %% "pureconfig" % "0.17.4",
  )
}
