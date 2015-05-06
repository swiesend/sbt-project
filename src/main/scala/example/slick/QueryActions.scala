/**
 * activator-hello-slick; branch: slick-3.0
 *
 * https://github.com/typesafehub/activator-hello-slick/tree/slick-3.0
 *
 * Copyright 2013-2015 Typesafe, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package example.slick

import scala.concurrent.Await
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration

import slick.driver.H2Driver.api._

// Demonstrates various ways of reading data
object QueryActions extends App {

  type MyPair = (Int, String)
  //case class MyPair(key: Int, value: String)

  // A simple dictionary table with keys and values
  class Dict(tag: Tag) extends Table[MyPair](tag, "INT_DICT") {
    def key = column[Int]("KEY", O.PrimaryKey)
    def value = column[String]("VALUE")
    def * = (key, value)
  }

  val dict = TableQuery[Dict]

  val db = Database.forConfig("h2mem1")
  try {

    // Define a pre-compiled parameterized query for reading all key/value
    // pairs up to a given key.
    val upTo = Compiled { k: Rep[Int] =>
      dict.filter(_.key <= k).sortBy(_.key)
    }

    // A second pre-compiled query which returns a Set[String]
    val upToSet = upTo.map(_.andThen(_.to[Set]))

    Await.result(db.run(DBIO.seq(

      // Create the dictionary table and insert some data
      dict.schema.create,
      dict ++= Seq(1 -> "a", 2 -> "b", 3 -> "c", 4 -> "d", 5 -> "e"),

      upTo(3).result.map { r =>
        println("Seq (Vector) of k/v pairs up to 3")
        println("- " + r)
      },

      upToSet(3).result.map { r =>
        println("Set of k/v pairs up to 3")
        println("- " + r)
      },

      dict.map(_.key).to[Array].result.map { r =>
        println("All keys in an unboxed Array[Int]")
        println("- " + r)
        println("  " + r.deep.mkString(", "))
      },

      upTo(3).result.head.map { r =>
        println("Only get the first result; failing if there is none")
        println("- " + r)
      },

      upTo(3).result.headOption.map { r =>
        println("Get the first result as an Option; or None")
        println("- " + r)
      })), Duration.Inf)

    // The Publisher captures a Database plus a DBIO action.
    // The action does not run until you consume the stream.
    val publisher = db.stream(upTo(3).result)

    println("Stream k/v pairs up to 3 via Reactive Streams")
    Await.result(publisher.foreach { v =>
      println("- " + v)
    }, Duration.Inf)

  } finally db.close
}
