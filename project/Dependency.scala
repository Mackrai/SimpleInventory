import sbt._

object Dependency {
  object Version {
    val catsEffect = "3.3.14"
    val doobie     = "1.0.0-RC2"
    val http4s     = "0.23.12"
    val log4cats   = "2.5.0"
    val logback    = "1.4.4"
    val pureConfig = "0.17.1"
    val tapir      = "1.1.3"
  }

  val catsEffect = "org.typelevel" %% "cats-effect" % Version.catsEffect

  val doobie: Seq[ModuleID] = Seq(
    "org.tpolecat" %% "doobie-core",
    "org.tpolecat" %% "doobie-postgres"
  ).map(_ % Version.doobie)

  val http4s: Seq[ModuleID] = Seq(
    "http4s-blaze-server",
    "http4s-blaze-client",
    "http4s-circe",
    "http4s-dsl"
  ).map("org.http4s" %% _ % Version.http4s)

  val logback = "ch.qos.logback" % "logback-classic" % Version.logback

  val log4cats = "org.typelevel" %% "log4cats-slf4j" % Version.log4cats

  val pureConfig = "com.github.pureconfig" %% "pureconfig" % Version.pureConfig

  val tapir: Seq[ModuleID] = Seq(
    "tapir-core",
    "tapir-json-circe",
    "tapir-http4s-server",
    "tapir-http4s-client",
    "tapir-swagger-ui-bundle",
    "tapir-openapi-docs",
    "tapir-swagger-ui"
  ).map("com.softwaremill.sttp.tapir" %% _ % Version.tapir)
}
