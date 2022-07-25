package org.example.service

import org.example.models.{Color, Jacket, JacketColor, JacketSex, JacketSize, Kitten, KittenColor, KittenSex, Sex}
import org.scalatest.freespec.AnyFreeSpec

class KittenJacketMatcherServiceSpec extends AnyFreeSpec {
  private val service = new KittenJacketMatcherService()

  private def testSize(sizeName: String, lowerRange: Double, upperRange: Double, jacketSize: JacketSize): Unit = {
    s"when jacket size is $sizeName" - {
      s"and kitten size is between ${lowerRange.toString} and ${upperRange.toString} cms" - {
        "should always be applied to kitten" in {
          val kitten1 = Kitten(lowerRange, KittenColor(Color.Blue), KittenSex(Sex.Female))
          val kitten2 = Kitten(upperRange, KittenColor(Color.Blue), KittenSex(Sex.Female))
          val jackets = Vector(Jacket(jacketSize, JacketColor(Color.Red), JacketSex(Sex.Female)))
          val result1 = service.getJacketsForKitten(kitten1, jackets)
          val result2 = service.getJacketsForKitten(kitten2, jackets)

          assert(result1 == jackets)
          assert(result2 == jackets)
        }
      }
      s"and kitten size is outside the range between ${lowerRange.toString} and ${upperRange.toString} cms" - {
        "should not be applied to kitten" in {
          val kitten1 = Kitten(lowerRange - 0.1, KittenColor(Color.Blue), KittenSex(Sex.Female))
          val kitten2 = Kitten(upperRange + 0.1, KittenColor(Color.Blue), KittenSex(Sex.Female))
          val jackets = Vector(Jacket(jacketSize, JacketColor(Color.Red), JacketSex(Sex.Female)))
          val result1 = service.getJacketsForKitten(kitten1, jackets)
          val result2 = service.getJacketsForKitten(kitten2, jackets)

          assert(result1 == Vector.empty)
          assert(result2 == Vector.empty)
        }
      }
    }
  }

  "#getJacketsForKitten" - {

    "when jacket color is red and size and sex are matching" - {
      "should always be applied to kitten" in {
        val kitten = Kitten(7.5, KittenColor(Color.Blue), KittenSex(Sex.Female))
        val jackets = Vector(Jacket(JacketSize.MEDIUM, JacketColor(Color.Red), JacketSex(Sex.Female)))
        val result = service.getJacketsForKitten(kitten, jackets)

        assert(result == jackets)
      }
    }
    "when jacket color is stars and size and sex are matching" - {
      "should always be applied to kitten" in {
        val kitten = Kitten(7.5, KittenColor(Color.Blue), KittenSex(Sex.Female))
        val jackets = Vector(Jacket(JacketSize.MEDIUM, JacketColor(Color.Stars), JacketSex(Sex.Female)))
        val result = service.getJacketsForKitten(kitten, jackets)

        assert(result == jackets)
      }
    }
    "when jacket color is blue and size and sex are matching" - {
      "when kitten color is pink or yellow" - {
        "should always be applied to kitten" in {
          val kitten1 = Kitten(7.5, KittenColor(Color.Pink), KittenSex(Sex.Female))
          val kitten2 = Kitten(7.5, KittenColor(Color.Pink), KittenSex(Sex.Female))
          val jackets = Vector(Jacket(JacketSize.MEDIUM, JacketColor(Color.Blue), JacketSex(Sex.Female)))
          val result1 = service.getJacketsForKitten(kitten1, jackets)
          val result2 = service.getJacketsForKitten(kitten2, jackets)

          assert(result1 == jackets)
          assert(result2 == jackets)
        }
      }
      "when kitten color is brown or blue" - {
        "should not be applied to kitten" in {
          val kitten1 = Kitten(7.5, KittenColor(Color.Brown), KittenSex(Sex.Female))
          val kitten2 = Kitten(7.5, KittenColor(Color.Blue), KittenSex(Sex.Female))
          val jackets = Vector(Jacket(JacketSize.MEDIUM, JacketColor(Color.Blue), JacketSex(Sex.Female)))
          val result1 = service.getJacketsForKitten(kitten1, jackets)
          val result2 = service.getJacketsForKitten(kitten2, jackets)

          assert(result1 == Vector.empty)
          assert(result2 == Vector.empty)
        }
      }
    }
    "when jacket sex is male" - {
      "and kitten sex is male with size and color matching" - {
        "should always be applied to kitten" in {
          val kitten = Kitten(7.5, KittenColor(Color.Blue), KittenSex(Sex.Male))
          val jackets = Vector(Jacket(JacketSize.MEDIUM, JacketColor(Color.Red), JacketSex(Sex.Male)))
          val result = service.getJacketsForKitten(kitten, jackets)

          assert(result == jackets)
        }
      }
      "and kitten sex is female with size and color matching" - {
        "should not be applied to kitten" in {
          val kitten = Kitten(7.5, KittenColor(Color.Blue), KittenSex(Sex.Female))
          val jackets = Vector(Jacket(JacketSize.MEDIUM, JacketColor(Color.Red), JacketSex(Sex.Male)))
          val result = service.getJacketsForKitten(kitten, jackets)

          assert(result == Vector.empty)
        }
      }
    }
    "when jacket sex is female" - {
      "and kitten sex is female with size and color matching" - {
        "should always be applied to kitten" in {
          val kitten = Kitten(7.5, KittenColor(Color.Blue), KittenSex(Sex.Female))
          val jackets = Vector(Jacket(JacketSize.MEDIUM, JacketColor(Color.Red), JacketSex(Sex.Female)))
          val result = service.getJacketsForKitten(kitten, jackets)

          assert(result == jackets)
        }
      }
      "and kitten sex is male with size and color matching" - {
        "should not be applied to kitten" in {
          val kitten = Kitten(7.5, KittenColor(Color.Blue), KittenSex(Sex.Male))
          val jackets = Vector(Jacket(JacketSize.MEDIUM, JacketColor(Color.Red), JacketSex(Sex.Female)))
          val result = service.getJacketsForKitten(kitten, jackets)

          assert(result == Vector.empty)
        }
      }
    }
    "when jacket sex is unisex" - {
      "and kitten sex is female with size and color matching" - {
        "should always be applied to kitten" in {
          val kitten1 = Kitten(7.5, KittenColor(Color.Blue), KittenSex(Sex.Female))
          val kitten2 = Kitten(7.5, KittenColor(Color.Blue), KittenSex(Sex.Male))
          val jackets = Vector(Jacket(JacketSize.MEDIUM, JacketColor(Color.Red), JacketSex(Sex.Unisex)))
          val result1 = service.getJacketsForKitten(kitten1, jackets)
          val result2 = service.getJacketsForKitten(kitten2, jackets)

          assert(result1 == jackets)
          assert(result2 == jackets)
        }
      }
    }

    testSize("small", 4, 7.5, JacketSize.SMALL)
    testSize("medium", 6.5, 10.5, JacketSize.MEDIUM)
    testSize("large", 9.5, 14, JacketSize.LARGE)
    testSize("xl", 13, 18, JacketSize.XL)

    "when jackets contain stars" - {
      "and all other attributes are matching" - {
        "jackets with stars should be sorted first" in {
          val kitten = Kitten(7.5, KittenColor(Color.Pink), KittenSex(Sex.Female))
          val jacket1 = Jacket(JacketSize.MEDIUM, JacketColor(Color.Red), JacketSex(Sex.Female))
          val jacket2 = Jacket(JacketSize.MEDIUM, JacketColor(Color.Blue), JacketSex(Sex.Female))
          val jacket3 = Jacket(JacketSize.SMALL, JacketColor(Color.Blue), JacketSex(Sex.Unisex))
          val jacket4 = Jacket(JacketSize.MEDIUM, JacketColor(Color.Stars), JacketSex(Sex.Female))
          val jacket5 = Jacket(JacketSize.SMALL, JacketColor(Color.Stars), JacketSex(Sex.Female))

          val jackets = Vector(jacket1, jacket2, jacket3, jacket4, jacket5)
          val result = service.getJacketsForKitten(kitten, jackets)

          val expectedJackets = Vector(jacket4, jacket5, jacket1, jacket2, jacket3)

          assert(result == expectedJackets)
        }
      }
    }
  }
}
