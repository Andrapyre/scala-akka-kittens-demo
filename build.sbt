lazy val akkaHttpVersion = "10.2.9"
lazy val akkaVersion    = "2.6.19"
lazy val akkaHttpCirceVersion = "1.39.2"
lazy val circeVersion = "0.14.2"
lazy val scalaTestVersion = "3.1.4"
lazy val scalaTestPlusMockitoVersion = "3.2.10.0"
lazy val scalaLoggingVersion = "3.9.4"
lazy val logbackVersion = "1.2.3"

// Run in a separate JVM, to make sure sbt waits until all threads have
// finished before returning.
// If you want to keep the application running while executing other
// sbt tasks, consider https://github.com/spray/sbt-revolver/
fork := true

lazy val root = (project in file(".")).
  settings(
    inThisBuild(List(
      organization    := "org.example",
      scalaVersion    := "2.13.4"
    )),
    name := "KittensDemo",
    libraryDependencies ++= Seq(
      "com.typesafe.akka" %% "akka-http"                % akkaHttpVersion,
      "com.typesafe.akka" %% "akka-http-spray-json"     % akkaHttpVersion,
      "com.typesafe.akka" %% "akka-actor-typed"         % akkaVersion,
      "com.typesafe.akka" %% "akka-stream"              % akkaVersion,
      "ch.qos.logback"    % "logback-classic"           % logbackVersion,

      "com.typesafe.akka" %% "akka-http-testkit"        % akkaHttpVersion             % Test,
      "com.typesafe.akka" %% "akka-actor-testkit-typed" % akkaVersion                 % Test,
      "org.scalatest"     %% "scalatest"                % scalaTestVersion            % Test,
      "org.scalatestplus" %% "mockito-3-4"              % scalaTestPlusMockitoVersion % Test,

      "io.circe" %% "circe-core"                % circeVersion,
      "io.circe" %% "circe-parser"              % circeVersion,
      "io.circe" %% "circe-generic"             % circeVersion,
      "de.heikoseeberger" %% "akka-http-circe"  % akkaHttpCirceVersion,

      "com.typesafe.scala-logging" %% "scala-logging"   % scalaLoggingVersion
    )
  )

