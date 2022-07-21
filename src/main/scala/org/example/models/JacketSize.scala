package org.example.models

import io.circe.{Decoder, DecodingFailure, Encoder, Json}

sealed trait JacketSize {
  val value: String
}

object JacketSize {
  case object SMALL extends JacketSize {val value = "S"}
  case object MEDIUM extends JacketSize {val value = "M"}
  case object LARGE extends JacketSize {val value = "L"}
  case object XL extends JacketSize {val value = "XL"}

  implicit val JacketSizeEncoder: Encoder[JacketSize] = Encoder.apply(size => Json.fromString(size.value))

  implicit val JacketSizeDecoder: Decoder[JacketSize] = Decoder.apply[JacketSize](cursor => {
    for {
      rawString <- cursor.as[String]
      resultOption = rawString match {
        case SMALL.value => Some(SMALL)
        case MEDIUM.value => Some(MEDIUM)
        case LARGE.value => Some(LARGE)
        case XL.value => Some(XL)
        case _ => None
      }
      result <- resultOption.toRight(DecodingFailure(s"Couldn't decode jacket size from ${cursor.toString}", List.empty))
    } yield result
  })
}
