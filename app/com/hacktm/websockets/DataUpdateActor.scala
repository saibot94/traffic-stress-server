package com.hacktm.websockets

import javax.inject.Inject

import akka.actor.{Actor, ActorRef, ActorSystem, PoisonPill, Props}
import akka.stream.Materializer
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.libs.json.Json

/**
  * Created by darkg on 27-May-17.
  */

object DataUpdateActor {
  def props(manager: ActorRef, out: ActorRef, id: Int) = Props(new DataUpdateActor(manager, out, id))

}

class DataUpdateActor(manager: ActorRef, out: ActorRef, id: Int) extends Actor {
  manager ! TrackWebsocket(id, this)
  val stressCalculatorActor: ActorRef = this.context.actorOf(StressCalculatorActor.props(self, id))

  def receive: PartialFunction[Any, Unit] = {
    case NotifyData(_, data) =>
      out ! Json.toJson(data)

    case x: StressPayload =>
      out ! Json.toJson(x)
  }

  def close: Unit = {
    self ! PoisonPill
  }
}
