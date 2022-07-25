package org.example.models

import io.circe.generic.semiauto.deriveEncoder
import io.circe.Encoder

final case class JacketsDto(items: Vector[Jacket], total: Int)

object JacketsDto {
  implicit val JacketsDtoEncoder: Encoder[JacketsDto] = deriveEncoder
}
