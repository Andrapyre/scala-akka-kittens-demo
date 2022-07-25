package org.example.models

import io.circe.{Decoder, DecodingFailure, Encoder, Json}

final case class JacketSex(value: Sex)

object JacketSex {
  private val maleJsonValue = "m"
  private val femaleJsonValue = "f"
  private val unisexJsonValue = "f"

  implicit val JacketSexEncoder: Encoder[JacketSex] = Encoder.apply(jacketSex => {
    val result = jacketSex.value match {
      case Sex.Male => maleJsonValue
      case Sex.Female => femaleJsonValue
      case Sex.Unisex => unisexJsonValue
    }
    Json.fromString(result)
  })

  implicit val JacketSexDecoder: Decoder[JacketSex] = Decoder.apply[JacketSex](cursor => {
    for {
      rawString <- cursor.as[String]
      resultOption = rawString match {
        case maleJsonValue => Some(JacketSex(Sex.Male))
        case femaleJsonValue => Some(JacketSex(Sex.Female))
        case unisexJsonValue => Some(JacketSex(Sex.Unisex))
        case _ => None
      }
      result <- resultOption.toRight(DecodingFailure(s"Couldn't decode jacket sex from ${cursor.toString}", List.empty))
    } yield result
  })
}
