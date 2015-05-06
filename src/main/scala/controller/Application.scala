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

/**
 * This is a basic sbt-project template with some examples on how to use
 * the libraries which are included.
 */
object Application extends App {

  //
  // Logging - with Logback
  //

  // The following line prints startup information from logback if anything should go amiss
  // import ch.qos.logback.core.util.StatusPrinter
  // import ch.qos.logback.classic.LoggerContext
  // StatusPrinter.print((LoggerFactory.getILoggerFactory).asInstanceOf[LoggerContext])

  // You can also define your custom logger with logback without Actors.
  // Within akka actors you can access the `log` from the `ActorLogging` trait.
  def logger = LoggerFactory.getLogger(this.getClass)
  logger.info("test")

  //
  // Config
  //

  // Make your application adaptable by reading values from a configuration file.

  // Load the config file from the `resourceDirectory` in our  build definition. 
  // This is "src/main/resources" by default, but here "conf/" because we defined
  // another path in the `build.sbt`.
  val conf = ConfigFactory.load()

  // Or load a configuration from a specific file:
  val myConf = ConfigFactory.parseFile(new File("src/main/resources/application.conf")).resolve()

  // Once the configuration is loaded you can access their values by their Scope/Path
  val one: Int = conf.getInt("scope.one")
  val two: String = conf.getString("scope.two")

  val decAsNumber: Number = conf.getNumber("scope.decimal")
  val decAsDouble: Double = conf.getDouble("scope.decimal")
  val decAsString: String = conf.getString("scope.decimal")

  println(decAsNumber.floatValue + decAsNumber.floatValue)
  println(decAsDouble + decAsDouble)
  println(decAsString + decAsString)

  //
  // Akka - Actors and Futures
  //

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

    val fut1 = MyLib.calcPi(0, 500000) //implicit `ExecutionContext`
    val fut2 = MyLib.calcPi(500000, 500000)(system.dispatcher) //explicit
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

    // Don't forget to shut your actor system down.
    // system.awaitTermination(5 seconds)
  } finally system.shutdown()

  //
  // Slick - Database access
  //

  import slick.driver.H2Driver.api._

  val db = Database.forConfig("h2mem1")
  try {
    // Access your database from within the try 
    // and don't forget to close your connection.

    // Checkout the example.slick package!

  } finally db.close

}