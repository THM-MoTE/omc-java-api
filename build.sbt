//include resources-jar (from java's home directory) in classpath for using
//com.sun.corba.se -package
 unmanagedJars in Runtime +=
    file(System.getenv("JAVA_HOME")) / "jre" / "lib" / "resources.jar"

lazy val root = Project(id = "omc-java-api", base = file(".")).
  settings(
    name := "omc-java-api",
    version := "0.1",
    scalaVersion := "2.11.8",
    javacOptions ++= Seq("-source", "1.8")
  )

libraryDependencies += "org.slf4j" % "slf4j-api" % "1.7.21"
libraryDependencies += "com.novocode" % "junit-interface" % "0.11" % "test"
autoScalaLibrary := false
