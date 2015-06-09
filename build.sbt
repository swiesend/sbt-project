import com.typesafe.config._

//
// Settings
//

val conf = ConfigFactory.parseFile(new File("src/main/resources/application.conf")).resolve()

lazy val commonSettings = Seq(
    name := conf.getString("app.name"),
    organization := conf.getString("app.organization"),
    version := conf.getString("app.version"),
    mainClass in Compile := Some("controller.Application"),
    //  scalacOptions += "-target:jvm-1.7",
    scalaVersion := "2.11.6"
)

lazy val pathSettings = Seq(
  resourceDirectory in Compile <<= baseDirectory / "src/main/resources"
)

lazy val root = (project in file("."))
  .settings(commonSettings: _*)
  .settings(pathSettings: _*)
// https://www.playframework.com/documentation/2.4.x/NewApplication#Create-a-new-application-without-Activator
// .enablePlugins(PlayScala)


//
// Dependencies
//
               
libraryDependencies ++= Seq(
    // groupID % otherID % otherRevision
    // groupID % otherID_2.11 % otherRevision
    // groupID %% otherID % otherRevision
    "ch.qos.logback" % "logback-classic" % "1.1.3",
    "org.scalatest" %% "scalatest" % "2.2.5",
    "com.typesafe" % "config" % "1.3.0",
    "com.typesafe.akka" %% "akka-actor" % "2.3.11",
    "com.typesafe.akka" %% "akka-testkit" % "2.3.11",
    "com.typesafe.akka" %% "akka-slf4j" % "2.3.11",
    "com.typesafe.slick" %% "slick" % "3.0.0",
    "com.h2database" % "h2" % "1.4.187",
    "mysql" % "mysql-connector-java" % "5.1.35"
)

//
// Eclipse
//

// download available sources and java-docs and make them auto-accessible in Eclipse 
EclipseKeys.withSource := true
