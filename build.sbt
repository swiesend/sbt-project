import sbt._
import Process._
import Keys._
import com.typesafe.config._
// add to activate play nature and source structure
//import play.Play.autoImport._
//import PlayKeys._

val conf = ConfigFactory.parseFile(new File("conf/application.conf")).resolve()

lazy val commonSettings = Seq(
  name := conf.getString("app.name"),
  organization := conf.getString("app.organization"),
  version := conf.getString("app.version"),
  scalaVersion := "2.11.4"
  /*scalacOptions += "-target:jvm-1.7"*/
)

lazy val root = (project in file(".")).settings(commonSettings: _*)
//  .enablePlugins(PlayScala)

//////////////////////////////
// Resolvers
//resolvers ++= Seq(
//  "Typesafe repository" at "https://repo.typesafe.com/typesafe/releases/"
//  "Typesafe Snapshots" at "https://repo.typesafe.com/typesafe/snapshots/"
//)

//////////////////////////////
// Dependencies
libraryDependencies ++= Seq(
  //groupID % otherID % otherRevision,
  "ch.qos.logback" % "logback-classic" % "1.1.3",
  "org.scalatest" % "scalatest_2.11" % "2.2.4",
  "com.typesafe" % "config" % "1.3.0-M2",
  "com.typesafe.akka" % "akka-actor_2.11" % "2.3.9",
  "com.typesafe.slick" % "slick_2.11" % "3.0.0-RC3"
//  "com.typesafe.play" % "play_2.11" % "2.3.8"
)

//////////////////////////////
// Eclipse settings

// download available sources and javadocs
// and make them accessible in Eclipse 
EclipseKeys.withSource := true
