package controllers

import java.io.File
import org.slf4j.LoggerFactory
import com.typesafe.config.ConfigFactory

object Application extends App {

  val conf = ConfigFactory.parseFile(new File("conf/application.conf")).resolve()

  val one: Int = conf.getInt("scope.one")
  val two: String = conf.getString("scope.two")
  val number: Number = conf.getNumber("scope.decimal")
  val double: Double = conf.getDouble("scope.decimal")

  // The following line prints startup information from logback if anything should go amiss
  // import ch.qos.logback.core.util.StatusPrinter
  // import ch.qos.logback.classic.LoggerContext
  // StatusPrinter.print((LoggerFactory.getILoggerFactory).asInstanceOf[LoggerContext])
  def log = LoggerFactory.getLogger(this.getClass)

  log.info(conf.getString("scope.logme"))
}