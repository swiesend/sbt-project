import com.typesafe.config._


//
// Settings
//

// load configuration file to obtain from there some values
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
  resourceDirectory in Compile := baseDirectory.value / "src/main/resources",
  resourceDirectory in Test := baseDirectory.value / "src/test/resources"
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

//The following example shows how to add the resources folders:
EclipseKeys.createSrc := EclipseCreateSrc.Default + EclipseCreateSrc.Resource


//
// Assembly
// 

// run assembly task by `sbt assembly` in your console
// execute generated jar with `java -jar path/to/file.jar`

// For example the name of the jar can be set as follows in build.sbt:
// assemblyJarName in assembly := "something.jar"

// To skip the test during assembly,
test in assembly := {}

// To set an explicit main class,
// mainClass in assembly := Some("controller.Application")