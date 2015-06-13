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
    case _ => {
      log.error("Unexpected message! Going to kill myself by taking a `PoisonPill`")
      // kill your self
      self ! PoisonPill
    }
  }

}
