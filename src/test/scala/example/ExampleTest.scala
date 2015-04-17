package example

import org.scalatest.BaseFun
import org.scalatest.BaseSpec

class FunctionTest extends BaseFun {

  test("you name it") {
    assert(true)
  }

}

class Specification extends BaseSpec {

  case class Person(name: String, var age: Int)
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