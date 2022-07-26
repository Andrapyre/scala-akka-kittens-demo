package org.example.models

trait ApplicationError extends Exception

object ApplicationError {
  final case class GenericInternalServerError(ex: Exception) extends ApplicationError
  final case class ExternalApiError(ex: Exception) extends ApplicationError
}
