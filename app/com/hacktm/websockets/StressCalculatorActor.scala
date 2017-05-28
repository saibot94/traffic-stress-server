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
      val futureCarPos = carJsons.take(700)
      val currentCarPos = carJsons.drop(700)
      val distances = currentCarPos.zip(futureCarPos).map {
        case (past, present) =>
          println("Calculating stress")

          val xDiff = past.trafficXPos.zip(present.trafficXPos).map { case (l1, l2) => (l1 - l2) * (l1 - l2) }
          val yDiff = past.trafficYPos.zip(present.trafficYPos).map { case (l1, l2) => (l1 - l2) * (l1 - l2) }
          val dist = xDiff.zip(yDiff).map { case (x, y) => Math.sqrt(x + y) }
          val speedXDiff = past.trafficXSpeed.zip(present.trafficXSpeed).map { case (l1, l2) => Math.abs(l1 - l2) }
          val speedYDiff = past.trafficYSpeed.zip(present.trafficYSpeed).map { case (l1, l2) => Math.abs(l1 - l2) }
          val mySpeedDiff = Math.abs(past.speed - present.speed)

          println(s"Computed distances $dist")
          (dist, mySpeedDiff)
      }.toSeq

      val stressFactorSpeed = distances.map(_._2).sum / distances.length
      val stressFactor: Int = if (stressFactorSpeed < 10) {
        10
      } else if (stressFactorSpeed < 20) {
        15
      } else if (stressFactorSpeed < 35) {
        25
      } else if (stressFactorSpeed < 50) {
        40
      } else {
        65
      }
      //      distances._1.map(dist => dist.count(d => d <= 2.0))
      out ! StressPayload("stress", stressFactor)
  }
}
