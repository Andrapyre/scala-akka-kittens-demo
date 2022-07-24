package org.example.service

import org.example.models.{Jacket, Kitten, KittenColor, Color}

class KittenJacketMatcher {
  def getJacketsForKitten(kitten: Kitten, jackets: Vector[Jacket]): Vector[Jacket] = {
    val jacketsFilteredByColor = filterJacketsByKittenColor(kitten.color, jackets)
    val jacketsFilteredBySize = filterJacketsByKittenSize(kitten.height, jacketsFilteredByColor)
    sortJackets(jacketsFilteredBySize)
  }

  private def filterJacketsByKittenColor(kittenColor: KittenColor, jackets: Vector[Jacket]): Vector[Jacket] = {
    jackets.flatMap(jacket => {
      jacket.color.color match {
        case Color.Red => Some(jacket)
        case Color.Stars => Some(jacket)
        case Color.Blue => {
          if (kittenColor.color == Color.Pink || kittenColor.color == Color.Yellow) {
            Some(jacket)
          } else None
        }
        case Color.Yellow => {
          if (kittenColor.color == Color.Brown || kittenColor.color == Color.Pink) {
            Some(jacket)
          } else None
        }
      }
    })
  }

  private def filterJacketsByKittenSize(kittenSize: Double, jackets: Vector[Jacket]): Vector[Jacket] = {

  }

  private def sortJackets(jackets: Vector[Jacket]): Vector[Jacket] = {

  }
}
