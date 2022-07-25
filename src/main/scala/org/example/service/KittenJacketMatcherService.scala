package org.example.service

import org.example.models.{Color, Jacket, JacketColor, JacketSize, Kitten, KittenColor, Sex}

class KittenJacketMatcherService {
  def getJacketsForKitten(kitten: Kitten, jackets: Vector[Jacket]): Vector[Jacket] = {
    val jacketsFilteredByColor = filterJacketsByKittenColor(kitten.color, jackets)
    val jacketsFilteredBySex = filterJacketsBySex(kitten, jacketsFilteredByColor)
    val jacketsFilteredBySize = jacketsFilteredBySex.flatMap(jacket => filterJacketByKittenSize(jacket, kitten.height))
    sortJackets(jacketsFilteredBySize)
  }

  private def filterJacketsBySex(kitten: Kitten, jackets: Vector[Jacket]): Vector[Jacket] = {
    jackets.flatMap { jacket =>
      jacket.sex.value match {
        case kitten.sex.value => Some(jacket)
        case Sex.Unisex => Some(jacket)
        case _ => None
      }
    }
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
    jackets.sortWith((jacket1, jacket2) => {
      if (jacket1.color.value == Color.Stars && jacket2.color.value == Color.Stars) false
      else if (jacket1.color.value == Color.Stars) true
      else false
    })
  }
}
