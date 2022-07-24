package org.example.models

sealed trait Color {
  val name: String
}
object Color {
  case object Red extends Color {val name = "red"}
  case object Blue extends Color{val name = "blue"}
  case object Stars extends Color{val name = "stars"}
  case object Yellow extends Color{val name = "yellow"}
  case object Brown extends Color{val name = "brown"}
  case object Pink extends Color{val name = "pink"}
}
