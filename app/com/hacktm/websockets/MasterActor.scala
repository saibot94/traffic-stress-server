package com.hacktm.websockets

import akka.actor.{Actor, ActorRef, Props}
import com.hacktm.dto.CarJsonDTO
import play.api.libs.json.JsValue

import scala.collection.mutable
import scala.collection.mutable.ListBuffer

/**
  * Created by darkg on 27-May-17.
  */

case class NotifyData(id: Int, data: List[CarJsonDTO])
case class TrackWebsocket(id: Int, actor: DataUpdateActor)

object MasterActor {
  val props = Props(new MasterActor)
}
class MasterActor extends Actor {
  val splitSize = 200
  var registeredActors: mutable.HashMap[Int, ListBuffer[DataUpdateActor]] = mutable.HashMap()

  override def receive: Receive = {
    case NotifyData(id, data) =>
      registeredActors.get(id) match {
        case None =>
          println("WARN: Could not find any socket to send data to")
        case Some(lb) =>
          lb.foreach {
            actor =>
              var split = data.splitAt(splitSize)
              while(split._2.nonEmpty) {
                actor.self ! NotifyData(id, split._1)
                split = split._2.splitAt(splitSize)
              }
              actor.stressCalculatorActor ! CalculateStressNotification(data)

          }
      }

    case TrackWebsocket(id, actorRef) =>
      registeredActors.get(id) match {
        case None =>
          val lb = new ListBuffer[DataUpdateActor]()
          lb += actorRef
          registeredActors += id -> lb
        case Some(lb) =>
          lb += actorRef
      }


  }
}
