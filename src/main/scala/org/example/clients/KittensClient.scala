package org.example.clients

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.{HttpMethods, HttpRequest, StatusCodes, Uri}
import akka.http.scaladsl.unmarshalling.Unmarshal
import org.example.models.Kitten
import de.heikoseeberger.akkahttpcirce.FailFastCirceSupport._
import org.example.models.ApplicationError.ExternalApiError

import scala.concurrent.{ExecutionContext, Future}

class KittensClient(hostUrl: String)(implicit val actorSystem: ActorSystem, ec: ExecutionContext) {

  def getKittenById(id: String): Future[Option[Kitten]] = {
    val uri = Uri(s"$hostUrl/kittens/$id")
    val request = HttpRequest(HttpMethods.GET, uri)
    for {
      response <- Http().singleRequest(request)
      kittenOption <- response.status match {
        case StatusCodes.OK => Unmarshal(response.entity).to[Kitten].map(body => Some(body))
        case StatusCodes.NotFound => Future.successful(None)
        case _ => Future.failed(ExternalApiError(new Exception(s"Could not retrieve data from kittens api. Status: ${response.status}")))
      }
    } yield kittenOption
  }
}
