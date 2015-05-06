package test

import org.scalatest._
import akka.testkit.TestKit
import akka.actor.ActorSystem

trait SpecBase extends FlatSpecLike with Matchers with OptionValues with Inside with Inspectors with BeforeAndAfter
trait FunBase extends FunSuiteLike with Matchers with OptionValues with Inside with Inspectors with BeforeAndAfter with BeforeAndAfterAll

class SpecBaseActorTestKit(system: ActorSystem) extends TestKit(system) with SpecBase
class FunBaseActorTestKit(system: ActorSystem) extends TestKit(system) with FunBase
