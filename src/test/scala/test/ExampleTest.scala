package test

import test._

/**
 * Test functionalities easily with the FunBase
 */
class FunctionTest extends FunBase {

  test("you name it") {
    assert(true)
  }

}

/**
 * For test driven development write your specifications first and implement then.
 */
class Specification extends SpecBase {

  case class Person(name: String, var age: Int = 0)

  val joe = Person("Joe", 27)
  val unknown = Person("Unknown")

  "Persons" should "have a name" in {
    assert(joe.name !== null)
    assert(joe.name === "Joe")

    assert(unknown.name !== null)
    assert(unknown.name.toLowerCase === "unknown")
  }

  it should "have an age" in {
    assert(joe.age !== null)
    assert(joe.age > 0)

    assert(unknown.age !== null)
    assert(unknown.age === 0)
  }

  it can "turn older" in {
    joe.age += 1
    assert(joe.age == 28)
  }

} 