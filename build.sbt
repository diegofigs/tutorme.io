name := """tutorme"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava)

scalaVersion := "2.11.7"

libraryDependencies ++= Seq(
  javaJdbc,
  javaJpa,
  cache,
  javaWs,
  evolutions
)

libraryDependencies += "org.postgresql" % "postgresql" % "9.4-1201-jdbc41"
libraryDependencies += "io.jsonwebtoken" % "jjwt" % "0.7.0"
libraryDependencies += "org.hibernate" % "hibernate-entitymanager" % "5.1.0.Final"
libraryDependencies += "com.amazonaws" % "aws-java-sdk" % "1.11.66"
libraryDependencies += "com.typesafe.play" %% "play-mailer" % "5.0.0"
