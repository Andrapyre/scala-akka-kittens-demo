package org.example.models

sealed trait Sex

object Sex {
  case object Male extends Sex
  case object Female extends Sex
  case object Unisex extends Sex
}