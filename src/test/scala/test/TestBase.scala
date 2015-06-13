package test

import org.scalatest._
import akka.testkit.TestKit
import akka.actor.ActorSystem

trait SpecBase extends FlatSpecLike with Matchers with OptionValues with Inside with Inspectors with BeforeAndAfter
trait FunBase extends FunSuiteLike with Matchers with OptionValues with Inside with Inspectors with BeforeAndAfter with BeforeAndAfterAll

class ActorTestKitSpecBase(system: ActorSystem) extends TestKit(system) with SpecBase
class ActorTestKitFunBase(system: ActorSystem) extends TestKit(system) with FunBase
