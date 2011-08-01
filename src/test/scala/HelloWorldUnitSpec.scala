package examples

import org.specs2.mutable._

class GritUnitSpec extends Specification {

  "The grid" should {
    "be empty" in {
      "Hello world" must have size(11)
    }
    "start with 'Hello'" in {
      "Hello world" must startWith("Hello")
    }
    "end with 'world'" in {
      "Hello world" must endWith("world")
    }
  }

}
