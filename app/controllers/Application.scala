package controllers

import java.io.File

import org.slf4j.LoggerFactory

import com.typesafe.config.ConfigFactory

import slick.driver.H2Driver.api.Database
import slick.driver.H2Driver.api.Session

object Application extends App {

  // Make your application adaptable by reading values from your configuration file 
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
  
  log.info(conf.getString("example.logme"))
  
  
  // Get connected! Use a slick supported database
  // import slick.driver.MySQLDriver.api._
  import slick.driver.H2Driver.api._
  
  val url = conf.getString("db.test.url")
  val user = conf.getString("db.test.user")
  val pass = conf.getString("db.test.password")
  val driver = conf.getString("db.test.driver")
  
  lazy val db = Database.forURL(url, user, pass, driver = driver)
  implicit val session: Session = db.createSession;
  
  
}