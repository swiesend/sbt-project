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

  case class Person(name: String, var age: Int) {
    // ...
  }
  
  val joe = Person("Joe", 27)
   
  "Persons" should "have an age" in {
      assert(joe.age !== null)
      assert(joe.age > 0)
  }
  
  it can "turn older" in {
      joe.age = 28
      assert(joe.age == 28)
  }
  
} 