package example.akka

import com.typesafe.config.Config
import akka.event.Logging.LogLevel
import akka.event.Logging
import org.slf4j.LoggerFactory
import akka.actor.Props
import akka.actor.PoisonPill
import akka.actor.Status

object WeekDay extends Enumeration {
  type WeekDay = Value
  val Mon, Tue, Wed, Thu, Fri, Sat, Sun = Value
}
import WeekDay._

// introduce useful messages
case class Day(day: WeekDay)
case object Next

/**
 * Actors are useful to manage state. This example actor manages the
 * day of a week.
 */
class WeekDayActor(today: WeekDay = Mon) extends ActorBase {

  // your state you like to encapsulate
  var day = today

  def receive = {
    case Day(d) => day = d
    case Next => {
      day = WeekDay((day.id + 1) % WeekDay.maxId)
    }
    case Status => sender ! day
    case _ => log.error("unexpected message")
  }

}


//  // use local variables and values which only you can access
//  var i: Int = 0
//
//  // define your behavior
//  override def preStart = {
//    context.actorOf(Props[PingPongActor], "child-1")
//    context.actorOf(Props[PingPongActor], "child-2")
//  }
//
//  def receive = {
//
//    // receive messages of type `Any`
//    case Ping => {
//      // do your thing
//      i += 1
//
//      // use the `implicit context` to answer your sender
//      sender ! Pong(i)
//
//      // or inform your children
//      context.children.foreach { _ ! Pong(i) }
//    }
//
//    // access the messages in a match like syntax
//    case Pong(amount) => {
//      if (amount <= 10) {
//        println(self + ": " + amount)
//        sender ! Ping
//      } else {
//        // kill your self
//        self ! PoisonPill
//      }
//    }
//
//    case _ => {
//      log.error("unknown message")
//    }
//  }
