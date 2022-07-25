package org.example.clients

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.{HttpMethods, HttpRequest, Uri}
import akka.http.scaladsl.unmarshalling.Unmarshal
import org.example.models.Jacket
import de.heikoseeberger.akkahttpcirce.FailFastCirceSupport._

import scala.concurrent.{ExecutionContext, Future}

class JacketsClient(hostUrl: String)(implicit val actorSystem: ActorSystem, ec: ExecutionContext) {

  def getAllJackets: Future[Vector[Jacket]] = {
    val uri = Uri(s"$hostUrl/jackets")
    val request = HttpRequest(HttpMethods.GET, uri)
    for {
      response <- Http().singleRequest(request)
      jackets <- Unmarshal(response.entity).to[Vector[Jacket]]
    } yield jackets
  }
}
