package controller

import java.io.File
import scala.concurrent.Await
import scala.concurrent.duration.DurationInt
import scala.language.postfixOps
import org.slf4j.LoggerFactory
import com.typesafe.config.ConfigFactory
import akka.actor.ActorSystem
import akka.actor.Inbox
import akka.actor.PoisonPill
import akka.actor.Props
import akka.actor.Status
import akka.actor.actorRef2Scala
import example.akka.Day
import example.akka.Next
import example.akka.MyLib
import example.akka.WeekDay._
import example.akka.WeekDayActor
import slick.driver.H2Driver.api.Database
import slick.jdbc.meta.MTable
import scala.concurrent.duration.Duration

/**
 * This is a basic sbt-project template with some examples on how to use
 * the libraries which are included.
 */
object Application extends App {

  println("""
  //
  // Logging - with Logback
  //
  """)

  //
  // @see: http://logback.qos.ch/manual/configuration.html
  //
  // 1. Logback tries to find a file called "logback.groovy" in the classpath.
  //
  // 2. If no such file is found, logback tries to find a file called "logback-test.xml" in the classpath.
  //
  // 3. If no such file is found, it checks for the file "logback.xml" in the classpath..
  //
  // ...
  //  
  //  If you are using Maven and if you place the "logback-test.xml" under the "src/test/resources" folder, 
  //  Maven will ensure that it won't be included in the artifact produced. Thus, you can use a different 
  //  configuration file, namely "logback-test.xml" during testing, and another file, namely, "logback.xml", 
  //  in production. The same principle applies by analogy for Ant. 
  //

  // The following line prints startup information from logback if anything should go amiss
  import ch.qos.logback.core.util.StatusPrinter
  import ch.qos.logback.classic.LoggerContext
  StatusPrinter.print((LoggerFactory.getILoggerFactory).asInstanceOf[LoggerContext])

  // You can also define your custom logger with logback without Actors.
  // Within akka actors you can access the `log` from the `ActorLogging` trait.
  def logger = LoggerFactory.getLogger(this.getClass)
  logger.debug("test message")
  logger.info("test message")
  logger.warn("test message")
  logger.error("test message")

  println("""

  //
  // Config
  //
  """)

  // Make your application adaptable by reading values from a configuration file.

  // Load the config file `application.conf` from the `resourceDirectory` in our `build.sbt` definition.
  val conf = ConfigFactory.load()

  // Or load a configuration from a specific file:
  val myConf = ConfigFactory.parseFile(new File("src/test/resources/application.conf")).resolve()

  // Once the configuration is loaded you can access their values by their Scope/Path
  val one: Int = conf.getInt("scope.one")
  val two: String = conf.getString("scope.two")

  val decAsNumber: Number = conf.getNumber("scope.decimal")
  val decAsDouble: Double = conf.getDouble("scope.decimal")
  val decAsString: String = conf.getString("scope.decimal")

  println("Loaded values from the `Config` configuration are typesafe: ")
  println(s" * sum of `Number` as `Float` (${decAsNumber.floatValue}): " + (decAsNumber.floatValue + decAsNumber.floatValue))
  println(s" * sum of `Double` ($decAsDouble): " + (decAsDouble + decAsDouble))
  println(s" * sum of `String` ('$decAsString'): " + (decAsString + decAsString))

  println("""

  //
  // Akka - Actors and Futures
  //
  """)

  // This project ships some examples in the `example.akka` package.

  // You just have to initialize one actor system for all
  // actors and futures during the application lifetime.
  val system = ActorSystem("mySystem")

  try {
    // Use actors to manage state! If you want concurrency use better
    // the akka futures:
    // @see: https://www.chrisstucchio.com/blog/2013/actors_vs_futures.html

    //  How to create an actor:
    //
    //  system.actorOf(Props[MyActor])
    //  system.actorOf(Props(new ActorWithArgs("arg1"))) // careful, see below
    //  system.actorOf(Props(classOf[ActorWithArgs], "arg1", "arg2"))
    //
    //  @see: http://doc.akka.io/docs/akka/snapshot/scala/actors.html#Props

    val weekday = system.actorOf(Props(classOf[WeekDayActor], Mon), "weekday")

    // Create an "actor-in-a-box"
    val inbox = Inbox.create(system)

    // Ask the 'weekday' for its current status
    // Reply should go to the "actor-in-a-box"
    inbox.send(weekday, Status)

    // Wait 5 seconds for the reply with the 'greeting' message
    val day1 = inbox.receive(5 seconds).asInstanceOf[WeekDay]
    println(day1) //prints: Mon

    // Tell the 'weekday' to change its day
    weekday ! Day(Sat)
    weekday ! Next // change to next day
    inbox.send(weekday, Status)
    val day2 = inbox.receive(5 seconds).asInstanceOf[WeekDay]
    println(day2) //prints: Sun

    // kill an actor with the pre-defined PoisonPill
    weekday ! PoisonPill

    //
    // Concurrency - with futures
    //

    // If you like to manage concurrent events use the akka futures!
    // @see: https://www.chrisstucchio.com/blog/2013/actors_vs_futures.html

    // We import the default dispatcher from the system to have an 
    // `ExecutionContext` in scope. Without an `ExecutionContext`
    // we can't run the akka futures.
    import system.dispatcher

    val fut1 = MyLib.calcPi(0, 500000) //uses implicitly the `ExecutionContext`
    val fut2 = MyLib.calcPi(500000, 500000)(system.dispatcher) //uses explicitly the `ExecutionContext`
    val fut3 = MyLib.calcPi(1000000, 500000)
    val fut4 = MyLib.calcPi(1500000, 500000)

    // Don't put the futures in the for-comprehension! Then they won't get
    // executed concurrent but serially only.
    val pi: Double = Await.result(for {
      r1 <- fut1
      r2 <- fut2
      r3 <- fut3
      r4 <- fut4
    } yield (r1 + r2 + r3 + r4), 5 seconds)

    println(s"$pi (approximation)")
    import scala.math.Pi
    println(scala.math.Pi + "  (scala.math.Pi)")

    // system.awaitTermination(5 seconds)
  } finally system.shutdown() // Don't forget to shutdown your actor system.

  // Logging is concurrent.
  // to not display the shutdown message from the actor-system after the println from slick
  Thread.sleep(100)

  println("""

  //
  // Slick - Database access
  //
  """)

  // Checkout the `example.slick` package for more slick examples!
  import slick.driver.H2Driver.api._

  val db = Database.forConfig("h2mem1")
  try {

    // Access your database from within the try

    import example.slick.Suppliers
    import example.slick.Schema.createTablesIfNotExistent
    
    val suppliers = TableQuery[Suppliers]

    Await.result(createTablesIfNotExistent(db, suppliers), Duration.Inf)

    Thread.sleep(100)
    println
    println("My new created schema: ")
    println(Await.result(db.run(MTable.getTables), Duration.Inf))

    // and don't forget to close your connection.

  } finally db.close

}