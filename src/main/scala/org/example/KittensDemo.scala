package org.example

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import org.example.clients.{JacketsClient, KittensClient}
import org.example.config.ApplicationConfig
import org.example.controllers.KittenMatcherController
import org.example.service.KittenJacketMatcherService
import spray.json.{JsArray, JsNumber, JsObject, JsString}

import scala.concurrent.{ExecutionContext, ExecutionContextExecutor}

/**
 * An very basic HTTP stub service that is a part of the Kittens Demo.
 *
 * Supported endpoints:
 *  - /jackets - returns the list of all available jackets
 *  - /kittens/{id} - returns the kitten with the given id. Available ids: ["percy", "lily", "sonya", "ben"]
 *
 * How to run:
 *  - sbt run (or `PORT=8888 sbt run` if you want to set a custom port)
 *  - open http://localhost:8080/jackets
 */
object KittensDemo {

  def main(args: Array[String]): Unit = {
    implicit val system: ActorSystem = akka.actor.ActorSystem("KittensDemo")
    implicit val ec: ExecutionContextExecutor = ExecutionContext.global
    import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._

    val config = ApplicationConfig()
    val kittensApiClient = new KittensClient(config.externalServices.kittensApiHost)
    val jacketsApiClient = new JacketsClient(config.externalServices.jacketsApiHost)

    val kittenJacketMatcherService = new KittenJacketMatcherService()
    val kittenMatcherController = new KittenMatcherController(kittensApiClient, jacketsApiClient, kittenJacketMatcherService)

    val route: Route =
      get {
        concat(
          path("kitten_jackets" / Remaining) { path =>
            kittenMatcherController.matchKittenToJackets(path, config.kittens.routes.defaultRequestTimeout)
          },
          path("kittens" / Remaining) {
            case "percy" => complete(
              StatusCodes.OK,
              JsObject("height" -> JsNumber(5.12), "color" -> JsString("brown"), "sex" -> JsString("male"))
            )
            case "lily" => complete(
              StatusCodes.OK,
              JsObject("height" -> JsNumber(10.05), "color" -> JsString("pink"), "sex" -> JsString("female"))
            )
            case "sonya" => complete(
              StatusCodes.OK,
              JsObject("height" -> JsNumber(7), "color" -> JsString("yellow"), "sex" -> JsString("female"))
            )
            case "ben" => complete(
              StatusCodes.OK,
              JsObject("height" -> JsNumber(13.32), "color" -> JsString("blue"), "sex" -> JsString("male"))
            )
            case _ => complete(StatusCodes.NotFound, "Kitten not found")
          },
          path("jackets") {
            complete(StatusCodes.OK, JsArray(
              JsObject("size" -> JsString("S"), "color" -> JsString("red"), "sex" -> JsString("male")),
              JsObject("size" -> JsString("M"), "color" -> JsString("blue"), "sex" -> JsString("female")),
              JsObject("size" -> JsString("L"), "color" -> JsString("yellow"), "sex" -> JsString("unisex")),
              JsObject("size" -> JsString("XL"), "color" -> JsString("stars"), "sex" -> JsString("male")),
              JsObject("size" -> JsString("M"), "color" -> JsString("stars"), "sex" -> JsString("female")),
              JsObject("size" -> JsString("L"), "color" -> JsString("red"), "sex" -> JsString("unisex")),
            ))
          },
        )
      }
    Http().newServerAt("0.0.0.0", config.kittens.port).bind(route)
  }
}
