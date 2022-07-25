package org.example.controllers

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.StandardRoute
import org.example.clients.{JacketsClient, KittensClient}
import org.example.models.JacketsDto
import org.example.service.KittenJacketMatcherService
import akka.http.scaladsl.server.Directives.complete
import com.typesafe.scalalogging.LazyLogging
import io.circe.syntax.EncoderOps
import de.heikoseeberger.akkahttpcirce.FailFastCirceSupport._

import scala.concurrent.duration.DurationInt
import scala.concurrent.{Await, ExecutionContext, Future}

class KittenMatcherController(kittenClient: KittensClient, jacketsClient: JacketsClient, kittenJacketMatcher: KittenJacketMatcherService)(implicit val ec: ExecutionContext) extends LazyLogging {
  def matchKittenToJackets(kittenId: String): StandardRoute = {
    val response = for {
      kittenOption <- kittenClient.getKittenById(kittenId)
      res <- kittenOption.fold(Future.successful(complete(StatusCodes.NotFound))) { kitten =>
        for {
          jackets <- jacketsClient.getAllJackets
          matchingJackets = kittenJacketMatcher.getJacketsForKitten(kitten, jackets)
          result = JacketsDto(matchingJackets, matchingJackets.length)
        } yield complete(StatusCodes.OK, result.asJson)
      }
    } yield res

    response.recover(err => handleFailure(new Exception(s"Execution failed for kitten id: $kittenId", err)))

    Await.result(response, 3.seconds)
  }

  private def handleFailure(err: Throwable): StandardRoute = {
    logger.error(err.getMessage, err)
    complete(StatusCodes.InternalServerError)
  }
}
