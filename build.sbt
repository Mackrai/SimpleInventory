import Dependency._

lazy val root = (project in file("."))
  .settings(
    name := "SimpleInventory",
    idePackagePrefix := Some("io.simple.inventory"),
    version := "0.1.0-SNAPSHOT",
    scalaVersion := "2.13.10",
    libraryDependencies ++= Seq(
      catsEffect,
      log4cats,
      logback,
      pureConfig
    ) ++ doobie ++ http4s ++ tapir
  )
