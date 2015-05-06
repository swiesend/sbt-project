package example.akka

import com.typesafe.config.Config

import akka.actor.Actor
import akka.actor.ActorLogging

/**
 * Extend this class to include ActorLogging and make a configuration available.
 */
abstract class ActorBase(conf: Option[Config] = None) extends Actor with ActorLogging