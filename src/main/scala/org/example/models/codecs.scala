package org.example.models

import io.circe.{Decoder, DecodingFailure, HCursor, Json}

object codecs {
  def decoderColorScheme(cursor: HCursor, colors: Vector[Color]): Decoder.Result[Color] = {
    for {
      rawString <- cursor.as[String]
      colorOption = colors.find(color => color.name == rawString)
      result <- colorOption.toRight(DecodingFailure(s"Couldn't decode color from ${cursor.toString}", List.empty))
    } yield result
  }

  def encodeColor(color: Color): Json = Json.fromString(color.name)
}
