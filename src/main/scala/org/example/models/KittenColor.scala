package org.example.models

import io.circe.{Decoder, Encoder}
import codecs.{decoderColorScheme, encodeColor}

final case class KittenColor(color: Color)

object KittenColor {
  implicit val KittenColorEncoder: Encoder[KittenColor] = Encoder.apply(kittenColor => encodeColor(kittenColor.color))
  implicit val KittenColorDecoder: Decoder[KittenColor] = Decoder.apply[KittenColor](cursor => {
    for {
      color <- decoderColorScheme(cursor, Vector(Color.Brown, Color.Pink, Color.Blue, Color.Yellow))
    } yield KittenColor(color)
  })
}
