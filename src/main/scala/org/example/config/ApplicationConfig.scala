package org.example.config

import org.example.config.ApplicationConfig.{ExternalServicesConfig, KittensConfig}
import pureconfig.ConfigSource
import pureconfig.generic.auto._

import scala.concurrent.duration.Duration

final case class ApplicationConfig(kittens: KittensConfig, externalServices: ExternalServicesConfig)

object ApplicationConfig {
  final case class KittensConfig(port: Int, routes: RoutesConfig)
  final case class RoutesConfig(defaultRequestTimeout: Duration)
  final case class ExternalServicesConfig(kittensApiHost: String, jacketsApiHost: String)

  def apply(): ApplicationConfig =
    ConfigSource.default.loadOrThrow[ApplicationConfig]
}