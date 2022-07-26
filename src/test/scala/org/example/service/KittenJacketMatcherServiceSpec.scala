package org.example.service

import org.example.models.{Color, Jacket, JacketColor, JacketSex, JacketSize, Kitten, KittenColor, KittenSex, Sex}
import org.scalatest.freespec.AnyFreeSpec

class KittenJacketMatcherServiceSpec extends AnyFreeSpec {
  private val service = new KittenJacketMatcherService()

  private def testSize(sizeName: String, lowerRange: Double, upperRange: Double, jacketSize: JacketSize): Unit = {
    s"when jacket size is $sizeName" - {
      s"and kitten size is between ${lowerRange.toString} and ${upperRange.toString} cms" - {
        "jacket should be applied to kitten" in {
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
        "jacket should not be applied to kitten" in {
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

  private def testKittenJacketColorCombination(kittenColor: Color, jacketColor: Color, shouldResultBeEmpty: Boolean = false): Unit = {
    val kitten = Kitten(7.5, KittenColor(kittenColor), KittenSex(Sex.Female))
    val jackets = Vector(Jacket(JacketSize.MEDIUM, JacketColor(jacketColor), JacketSex(Sex.Female)))
    val result = service.getJacketsForKitten(kitten, jackets)
    if (shouldResultBeEmpty) {
      assert(result == Vector.empty)
    } else {
      assert(result == jackets)
    }
  }

  private def testKittenJacketSexCombination(kittenSex: Sex, jacketSex: Sex, shouldResultBeEmpty: Boolean = false): Unit = {
    val kitten = Kitten(7.5, KittenColor(Color.Blue), KittenSex(kittenSex))
    val jackets = Vector(Jacket(JacketSize.MEDIUM, JacketColor(Color.Red), JacketSex(jacketSex)))
    val result = service.getJacketsForKitten(kitten, jackets)
    if (shouldResultBeEmpty) {
      assert(result == Vector.empty)
    } else {
      assert(result == jackets)
    }
  }
  
  "#getJacketsForKitten" - {

    "when jacket color is red and size and sex are matching" - {
      "jacket should be applied to kitten" in {
        testKittenJacketColorCombination(Color.Blue, Color.Red)
        testKittenJacketColorCombination(Color.Brown, Color.Red)
        testKittenJacketColorCombination(Color.Pink, Color.Red)
        testKittenJacketColorCombination(Color.Yellow, Color.Red)
      }
    }
    "when jacket color is stars and size and sex are matching" - {
      "jacket should be applied to kitten" in {
        testKittenJacketColorCombination(Color.Blue, Color.Stars)
        testKittenJacketColorCombination(Color.Brown, Color.Stars)
        testKittenJacketColorCombination(Color.Pink, Color.Stars)
        testKittenJacketColorCombination(Color.Yellow, Color.Stars)
      }
    }
    "when jacket color is blue and size and sex are matching" - {
      "when kitten color is pink or yellow" - {
        "jacket should be applied to kitten" in {
          testKittenJacketColorCombination(Color.Pink, Color.Blue)
          testKittenJacketColorCombination(Color.Yellow, Color.Blue)
        }
      }
      "when kitten color is brown or blue" - {
        "jacket should not be applied to kitten" in {
          testKittenJacketColorCombination(Color.Brown, Color.Blue, shouldResultBeEmpty = true)
          testKittenJacketColorCombination(Color.Blue, Color.Blue, shouldResultBeEmpty = true)
        }
      }
    }
    "when jacket color is yellow and size and sex are matching" - {
      "when kitten color is brown or pink" - {
        "jacket should be applied to kitten" in {
          testKittenJacketColorCombination(Color.Brown, Color.Yellow)
          testKittenJacketColorCombination(Color.Pink, Color.Yellow)
        }
      }
      "when kitten color is blue or yellow" - {
        "jacket should not be applied to kitten" in {
          testKittenJacketColorCombination(Color.Blue, Color.Yellow, shouldResultBeEmpty = true)
          testKittenJacketColorCombination(Color.Yellow, Color.Yellow, shouldResultBeEmpty = true)
        }
      }
    }
    "when jacket sex is male" - {
      "and kitten sex is male with size and color matching" - {
        "jacket should be applied to kitten" in {
          testKittenJacketSexCombination(Sex.Male, Sex.Male)
        }
      }
      "and kitten sex is female with size and color matching" - {
        "jacket should not be applied to kitten" in {
          testKittenJacketSexCombination(Sex.Female, Sex.Male, shouldResultBeEmpty = true)
        }
      }
    }
    "when jacket sex is female" - {
      "and kitten sex is female with size and color matching" - {
        "jacket should be applied to kitten" in {
          testKittenJacketSexCombination(Sex.Female, Sex.Female)
        }
      }
      "and kitten sex is male with size and color matching" - {
        "jacket should not be applied to kitten" in {
          testKittenJacketSexCombination(Sex.Male, Sex.Female, shouldResultBeEmpty = true)
        }
      }
    }
    "when jacket sex is unisex" - {
      "and kitten sex is female with size and color matching" - {
        "jacket should be applied to kitten" in {
          testKittenJacketSexCombination(Sex.Male, Sex.Unisex)
          testKittenJacketSexCombination(Sex.Female, Sex.Unisex)
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
