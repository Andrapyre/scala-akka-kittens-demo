package org.example.models

import io.circe.{Decoder, Encoder}
import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}

final case class Kitten(
                          height: Double,
                          color: KittenColor,
                          sex: KittenSex
)

object Kitten {
  implicit val KittenEncoder: Encoder[Kitten] = deriveEncoder
  implicit val KittenDecoder: Decoder[Kitten] = deriveDecoder
}
