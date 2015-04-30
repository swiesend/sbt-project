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
  scalaVersion := "2.11.6",
  /*scalacOptions += "-target:jvm-1.7",*/  
  target <<= baseDirectory / "target",
  sourceDirectory in Compile <<= baseDirectory / "app",
  sourceDirectory in Test <<= baseDirectory / "test",
  /*confDirectory <<= baseDirectory / "conf",*/
  resourceDirectory in Compile <<= baseDirectory / "conf",
  scalaSource in Compile <<= baseDirectory / "app",
  scalaSource in Test <<= baseDirectory / "test",
  javaSource in Compile <<= baseDirectory / "app",
  javaSource in Test <<= baseDirectory / "test"
  /*distDirectory <<= baseDirectory / "dist"*/
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
  "com.typesafe.slick" % "slick_2.11" % "3.0.0-RC3",
//  "com.typesafe.play" % "play_2.11" % "2.3.8"
  "com.h2database" % "h2" % "1.4.187",
  "mysql" % "mysql-connector-java" % "5.1.35"
)

//////////////////////////////
// Eclipse settings

// download available sources and javadocs
// and make them auto-accessible in Eclipse 
EclipseKeys.withSource := true
