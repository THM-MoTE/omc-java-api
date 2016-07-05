lazy val root = (project in file(".")).
  settings(
    name := "omc-java-api",
    version := "0.1",
    scalaVersion := "2.11.8",
    javacOptions ++= Seq("-source", "1.8")
  )

libraryDependencies += "org.slf4j" % "slf4j-api" % "1.7.21"

autoScalaLibrary := false
