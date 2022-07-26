package org.example.controllers

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.{Route, StandardRoute}
import akka.http.scaladsl.testkit.ScalatestRouteTest
import io.circe.Decoder
import io.circe.generic.semiauto.deriveDecoder
import org.example.clients.{JacketsClient, KittensClient}
import org.example.models.{Color, Jacket, JacketColor, JacketSex, JacketSize, JacketsDto, Kitten, KittenColor, KittenSex, Sex}
import org.example.service.KittenJacketMatcherService
import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito.when
import org.scalatest.freespec.AnyFreeSpec
import org.scalatest.matchers.should.Matchers
import org.scalatestplus.mockito.MockitoSugar
import de.heikoseeberger.akkahttpcirce.FailFastCirceSupport._
import org.example.models.ApplicationError.ExternalApiError

import scala.concurrent.{ExecutionContext, ExecutionContextExecutor, Future}

class KittenMatcherControllerSpec extends AnyFreeSpec with MockitoSugar with Matchers with ScalatestRouteTest {
  implicit val ec: ExecutionContextExecutor = ExecutionContext.global

  private def generateTestRoute (controllerMethod: String => StandardRoute): Route = {
    get {
      concat(
        path("kitten_jackets" / Remaining) { path =>
          controllerMethod(path)
        },
      )
    }
  }

  private val service = new KittenJacketMatcherService()

  implicit val JacketsDtoDecoder: Decoder[JacketsDto] = deriveDecoder

  "#matchKittenToJackets" - {
    "when all clients return successfully" - {
      "and jackets match the kitten" - {
        "status code should be 200 and matching jackets should be returned correctly" in {
          val jacketsClientMock = mock[JacketsClient]
          val kittensClientMock = mock[KittensClient]

          val jackets = Vector(Jacket(JacketSize.MEDIUM, JacketColor(Color.Red), JacketSex(Sex.Male)))
          when(jacketsClientMock.getAllJackets)
            .thenReturn(Future.successful(jackets))

          when(kittensClientMock.getKittenById(any[String]))
            .thenReturn(Future.successful(Some(Kitten(7.0, KittenColor(Color.Pink), KittenSex(Sex.Male)))))

          val controller = new KittenMatcherController(kittensClientMock, jacketsClientMock, service)
          val route = generateTestRoute(controller.matchKittenToJackets)

          Get("/kitten_jackets/ben") ~> route ~> check {
            response.status shouldBe StatusCodes.OK
            responseAs[JacketsDto] shouldBe JacketsDto(jackets, 1)
          }
        }
      }
      "and jackets don't match the kitten" - {
        "status code should be 200 and no jackets should be returned" in {
          val jacketsClientMock = mock[JacketsClient]
          val kittensClientMock = mock[KittensClient]

          val jackets = Vector(Jacket(JacketSize.XL, JacketColor(Color.Red), JacketSex(Sex.Male)))
          when(jacketsClientMock.getAllJackets)
            .thenReturn(Future.successful(jackets))

          when(kittensClientMock.getKittenById(any[String]))
            .thenReturn(Future.successful(Some(Kitten(7.0, KittenColor(Color.Pink), KittenSex(Sex.Male)))))

          val controller = new KittenMatcherController(kittensClientMock, jacketsClientMock, service)
          val route = generateTestRoute(controller.matchKittenToJackets)

          Get("/kitten_jackets/ben") ~> route ~> check {
            response.status shouldBe StatusCodes.OK
            responseAs[JacketsDto] shouldBe JacketsDto(Vector.empty, 0)
          }
        }
      }
      "and kitten is not found" - {
        "status code should be 404" in {
          val jacketsClientMock = mock[JacketsClient]
          val kittensClientMock = mock[KittensClient]

          val jackets = Vector(Jacket(JacketSize.XL, JacketColor(Color.Red), JacketSex(Sex.Male)))
          when(jacketsClientMock.getAllJackets)
            .thenReturn(Future.successful(jackets))

          when(kittensClientMock.getKittenById(any[String]))
            .thenReturn(Future.successful(None))

          val controller = new KittenMatcherController(kittensClientMock, jacketsClientMock, service)
          val route = generateTestRoute(controller.matchKittenToJackets)

          Get("/kitten_jackets/ben") ~> route ~> check {
            response.status shouldBe StatusCodes.NotFound
          }
        }
      }
      "and no jackets are returned from jackets api" - {
        "status code should be 200 and no jackets should be returned" in {
          val jacketsClientMock = mock[JacketsClient]
          val kittensClientMock = mock[KittensClient]

          val jackets = Vector.empty
          when(jacketsClientMock.getAllJackets)
            .thenReturn(Future.successful(jackets))

          when(kittensClientMock.getKittenById(any[String]))
            .thenReturn(Future.successful(Some(Kitten(7.0, KittenColor(Color.Pink), KittenSex(Sex.Male)))))

          val controller = new KittenMatcherController(kittensClientMock, jacketsClientMock, service)
          val route = generateTestRoute(controller.matchKittenToJackets)

          Get("/kitten_jackets/ben") ~> route ~> check {
            response.status shouldBe StatusCodes.OK
            responseAs[JacketsDto] shouldBe JacketsDto(Vector.empty, 0)
          }
        }
      }
    }
    "when kittens api client fails" - {
      "status code should be 500" in {
        val jacketsClientMock = mock[JacketsClient]
        val kittensClientMock = mock[KittensClient]

        val jackets = Vector(Jacket(JacketSize.XL, JacketColor(Color.Red), JacketSex(Sex.Male)))
        when(jacketsClientMock.getAllJackets)
          .thenReturn(Future.successful(jackets))

        when(kittensClientMock.getKittenById(any[String]))
          .thenReturn(Future.failed(ExternalApiError(new Exception("Client broke"))))

        val controller = new KittenMatcherController(kittensClientMock, jacketsClientMock, service)
        val route = generateTestRoute(controller.matchKittenToJackets)

        Get("/kitten_jackets/ben") ~> route ~> check {
          response.status shouldBe StatusCodes.InternalServerError
        }
      }
    }
    "when jackets api client fails" - {
      "status code should be 500" in {
        val jacketsClientMock = mock[JacketsClient]
        val kittensClientMock = mock[KittensClient]

        when(jacketsClientMock.getAllJackets)
          .thenReturn(Future.failed(ExternalApiError(new Exception("Client broke"))))

        when(kittensClientMock.getKittenById(any[String]))
          .thenReturn(Future.successful(Some(Kitten(7.0, KittenColor(Color.Pink), KittenSex(Sex.Male)))))

        val controller = new KittenMatcherController(kittensClientMock, jacketsClientMock, service)
        val route = generateTestRoute(controller.matchKittenToJackets)

        Get("/kitten_jackets/ben") ~> route ~> check {
          response.status shouldBe StatusCodes.InternalServerError
        }
      }
    }
  }
}
