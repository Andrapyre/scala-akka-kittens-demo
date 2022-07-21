package org.example.models

import io.circe.{Decoder, DecodingFailure, Encoder, Json}

final case class KittenSex(sex: Sex)

object KittenSex {
  private val maleJsonValue = "male"
  private val femaleJsonValue = "female"

  implicit val KittenSexEncoder: Encoder[KittenSex] = Encoder.apply(kittenSex => {
    val result = kittenSex.sex match {
      case Sex.Male => maleJsonValue
      case Sex.Female => femaleJsonValue
    }
    Json.fromString(result)
  })

  implicit val KittenSexDecoder: Decoder[KittenSex] = Decoder.apply[KittenSex](cursor => {
    for {
      rawString <- cursor.as[String]
      resultOption = rawString match {
        case maleJsonValue => Some(KittenSex(Sex.Male))
        case femaleJsonValue => Some(KittenSex(Sex.Female))
        case _ => None
      }
      result <- resultOption.toRight(DecodingFailure(s"Couldn't decode kitten sex from ${cursor.toString}", List.empty))
    } yield result
  })
}
