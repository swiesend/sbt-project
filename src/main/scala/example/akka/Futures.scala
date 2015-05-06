package example.akka

import scala.concurrent.Future
import akka.actor.ActorSystem
import scala.concurrent.ExecutionContextExecutor

/**
 * Define your Futures in a scope where an `ExecutionContextExecutor` is available
 */
object MyLib {
  
  // we import from the system the default dispatcher
  // to have an `ExecutionContextExecutor` in scope
  //import system.dispatcher

  /**
   * Calculate pi by iterations with the Leibniz formula.
   * 
   * @see: https://en.wikipedia.org/wiki/Leibniz_formula_for_%CF%80
   */
  def calcPi(start: Int, elems: Int)(implicit ec: ExecutionContextExecutor): Future[Double] = Future {
    var acc = 0.0
    for (i <- start until (start + elems))
      acc += 4.0 * (1 - (i % 2) * 2) / (2 * i + 1)
    acc
  }

}