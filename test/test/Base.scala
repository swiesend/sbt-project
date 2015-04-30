package test

import org.scalatest._

abstract class BaseSpec extends FlatSpec with Matchers with OptionValues with Inside with Inspectors with BeforeAndAfter

abstract class BaseFun extends FunSuite with Matchers with OptionValues with Inside with Inspectors with BeforeAndAfter with BeforeAndAfterAll