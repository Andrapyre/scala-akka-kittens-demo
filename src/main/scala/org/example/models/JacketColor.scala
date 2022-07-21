package org.example.models

import io.circe.{Decoder, Encoder}
import codecs.{decoderColorScheme, encodeColor}

final case class JacketColor(color: Color)

object JacketColor {
  implicit val JacketColorEncoder: Encoder[JacketColor] = Encoder.apply(kittenColor => encodeColor(kittenColor.color))
  implicit val JacketColorDecoder: Decoder[JacketColor] = Decoder.apply[JacketColor](cursor => {
    for {
      color <- decoderColorScheme(cursor, Vector(Color.Red, Color.Blue, Color.Yellow, Color.Stars))
    } yield JacketColor(color)
  })
}