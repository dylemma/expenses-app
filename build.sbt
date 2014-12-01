name := """expenses-app"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.1"

libraryDependencies ++= Seq(
  jdbc,
  anorm,
  cache,
  ws,
  "org.webjars" %% "webjars-play" % "2.3.0-2",
  "org.webjars" % "bootstrap" % "3.1.1-2",
  "org.webjars" % "baconjs" % "0.7.18",
  "org.webjars" % "underscorejs" % "1.7.0",
  "com.typesafe.play" %% "play-slick" % "0.8.0"
)
