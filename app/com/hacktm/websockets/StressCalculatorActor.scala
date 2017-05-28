package com.hacktm.websockets

import akka.actor.{Actor, ActorRef, Props}
import com.hacktm.dto.CarJsonDTO
import play.api.libs.json.{Json, Reads, Writes}

/**
  * Created by darkg on 28-May-17.
  */

import play.api.libs.concurrent.Execution.Implicits.defaultContext

case class CalculateStressNotification(carJsons: List[CarJsonDTO])
case class StressPayload(event: String, payload: Int)
object StressPayload {
  implicit val readsCarJson: Reads[StressPayload] = Json.reads[StressPayload]
  implicit val writesCarJson: Writes[StressPayload] = Json.writes[StressPayload]

}
object StressCalculatorActor {
  def props(out: ActorRef, id: Int) = Props(new StressCalculatorActor(out, id))

}

class StressCalculatorActor(out: ActorRef, id: Int) extends Actor {

  def receive = {
    case CalculateStressNotification(carJsons) =>
      val futureCarPos = carJsons.drop(100)
      val distances  = carJsons.zip(futureCarPos).par.map {
        case  (past, present) =>
          println("Calculating stress")
          val xDiff = past.trafficXPos.zip(present.trafficXPos).map { case (l1, l2) => (l1 - l2) * (l1 - l2)}
          val yDiff = past.trafficYPos.zip(present.trafficYPos).map { case (l1, l2) => (l1 - l2) * (l1 - l2)}
          val dist = xDiff.zip(yDiff).map { case (x,y) => Math.sqrt(x+y) }
          println(s"Computed distances $dist")
          dist
      }.toSeq

      out ! StressPayload("stress", distances.map(dist => dist.count(d => d <= 2.0)).sum)
  }
}
