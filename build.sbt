import sbt._
import Process._
import Keys._

lazy val commonSettings = Seq(
  name := "hello",
  organization := "de.swiesend",
  version := "0.1.0",
  scalaVersion := "2.11.4",
  scalacOptions += "-target:jvm-1.7"
)

lazy val root = (project in file(".")).
  settings(commonSettings: _*)
//  settings(commonSettings: _*).
//  settings(
//    name := "hello"
//  )

libraryDependencies ++= Seq(
  //groupID % otherID % otherRevision,
  "ch.qos.logback" % "logback-classic" % "1.1.3",
  "org.scalatest" % "scalatest_2.11" % "2.2.4",
  "com.typesafe.akka" % "akka-actor_2.11" % "2.3.9",
  "com.typesafe.play" % "play_2.11" % "2.4.0-M3",
  "com.typesafe.slick" % "slick_2.11" % "3.0.0-RC3"
)
