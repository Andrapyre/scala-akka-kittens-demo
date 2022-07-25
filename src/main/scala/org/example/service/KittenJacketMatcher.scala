package org.example.service

import org.example.models.{Color, Jacket, JacketColor, JacketSize, Kitten, KittenColor}

class KittenJacketMatcher {
  def getJacketsForKitten(kitten: Kitten, jackets: Vector[Jacket]): Vector[Jacket] = {
    val jacketsFilteredByColor = filterJacketsByKittenColor(kitten.color, jackets)
    val jacketsFilteredBySize = jacketsFilteredByColor.flatMap(jacket => filterJacketByKittenSize(jacket, kitten.height))
    sortJackets(jacketsFilteredBySize)
  }

  private def filterJacketsByKittenColor(kittenColor: KittenColor, jackets: Vector[Jacket]): Vector[Jacket] = {
    jackets.flatMap(jacket => {
      jacket.color.value match {
        case Color.Red => Some(jacket)
        case Color.Stars => Some(jacket)
        case Color.Blue => {
          if (kittenColor.value == Color.Pink || kittenColor.value == Color.Yellow) {
            Some(jacket)
          } else None
        }
        case Color.Yellow => {
          if (kittenColor.value == Color.Brown || kittenColor.value == Color.Pink) {
            Some(jacket)
          } else None
        }
      }
    })
  }

  private def filterJacketByKittenSize(jacket: Jacket, kittenSize: Double): Option[Jacket] = {
    jacket.size match {
      case JacketSize.SMALL => if (kittenSize >= 4 && kittenSize <= 7.5) Some(jacket) else None
      case JacketSize.MEDIUM => if (kittenSize >= 6.5 && kittenSize <= 10.5) Some(jacket) else None
      case JacketSize.LARGE => if (kittenSize >= 9.5 && kittenSize <= 14) Some(jacket) else None
      case JacketSize.XL => if (kittenSize >= 13 && kittenSize <= 18) Some(jacket) else None
    }
  }

  private def sortJackets(jackets: Vector[Jacket]): Vector[Jacket] = {
    jackets.sortWith((jacket1, _) => {
      if (jacket1.color.value == Color.Stars) true else false
    })
  }
}
