package controllers

import javax.inject._

import akka.actor.ActorSystem
import akka.stream.Materializer
import play.api.Configuration
import play.api.libs.json.{JsValue, Json}
import play.api.mvc.{Action, Controller}
import play.modules.reactivemongo.{MongoController, ReactiveMongoApi, ReactiveMongoComponents}
import reactivemongo.api.collections.bson.BSONCollection
import reactivemongo.bson.BSONDocument
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import com.hacktm.dto.CarJsonDTO._
import HistoricalDataDTO._
import scala.concurrent.Future

/**
  * Created by darkg on 28-May-17.
  */

class HistoricalDataController @Inject()(implicit system: ActorSystem,
                                         materializer: Materializer,
                                         val reactiveMongoApi: ReactiveMongoApi,
                                         configuration: Configuration) extends Controller
  with MongoController with ReactiveMongoComponents {

  def carDataCollection: Future[BSONCollection] = database.map {
    db => db.collection[BSONCollection](configuration.getString("db.collection").getOrElse("cardata"))
  }

  def getHistoricalData(timestamp: Long) = Action.async {
    implicit request =>
      carDataCollection.flatMap {
        col =>
          val query = BSONDocument("timestamp" -> BSONDocument("$lte" -> timestamp))
          val carData = col.find(query).sort(BSONDocument("timestamp" -> -1)).cursor[BSONDocument]().
            collect[List](30)

          carData.map { data =>
            Ok(Json.toJson(data.map { cacat =>
              HistoricalDataDTO(
                cacat.getAs[Double]("speed").get,
                cacat.getAs[Double]("rpm").get
              )
            }))
          }
      }
  }

}
