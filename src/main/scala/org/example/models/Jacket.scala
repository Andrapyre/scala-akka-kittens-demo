package org.example.models

import io.circe.{Decoder, Encoder}
import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}

final case class Jacket(
                         size: JacketSize,
                         color: JacketColor,
                         sex: JacketSex
                       )

object Jacket {
  implicit val JacketEncoder: Encoder[Jacket] = deriveEncoder
  implicit val JacketDecoder: Decoder[Jacket] = deriveDecoder
}
