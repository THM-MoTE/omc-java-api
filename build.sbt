import java.io.FileNotFoundException

//include resources-jar (from java's home directory) in classpath for using
//com.sun.corba.se -package
unmanagedJars in Compile += {
  val jhome = Resources.getJavaHome
  Resources.checkExists(jhome / "lib" / "resources.jar")
}

//configure antlr4
antlr4Settings
  //resulting package name
antlr4PackageName in Antlr4 := Some("omc.corba.parser")
  //src directory of .g4 files
(sourceDirectory in Antlr4) := file("src/main/antlr")

lazy val root = Project(id = "omc-java-api", base = file(".")).
  settings(
    organization := "de.thm.mote",
    name := "omc-java-api",
    version := "0.1",
    scalaVersion := "2.11.8",
    javacOptions ++= Seq("-source", "1.8")
  )

resolvers += Resolver.jcenterRepo

libraryDependencies += "org.slf4j" % "slf4j-api" % "1.7.21"
libraryDependencies += "org.testng" % "testng" % "6.9.12" % Test
autoScalaLibrary := false
