import java.io.FileNotFoundException

//include resources-jar (from java's home directory) in classpath for using
//com.sun.corba.se -package
unmanagedJars in Compile += {
  val javaHome = System.getenv("JAVA_HOME")
  if(javaHome == null)
    throw new FileNotFoundException("$JAVA_HOME is undefined. Setup a environment variable JAVA_HOME")
  val rscJar = file(javaHome) / "jre" / "lib" / "resources.jar"
  if(rscJar.exists()) rscJar
  else throw new FileNotFoundException(s"Can't find needed resource: $rscJar")
 }

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
