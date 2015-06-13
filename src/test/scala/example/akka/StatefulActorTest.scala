package example.akka

import akka.actor.ActorSystem
import akka.actor.Status
import akka.testkit.TestActorRef
import akka.testkit.TestProbe
import example.akka.WeekDay._
import test.ActorTestKitFunBase

class WeekDayActorTest extends ActorTestKitFunBase(ActorSystem("test-system")) {

  val probe = TestProbe()

  // Creation of the TestActorRef

  test("WeekDay uninitialized") {
    val weekday = TestActorRef(new WeekDayActor())

    probe.send(weekday, Status)

    probe.expectMsg(Mon)
  }

  test("WeekDay initialized") {
    val weekday = TestActorRef(new WeekDayActor(Sun))

    probe.send(weekday, Status)

    import example.akka.WeekDay._
    probe.expectMsg(Sun)
  }

}