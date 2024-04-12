name := """Crud operation"""
organization := "com.pradeep"

version := "1.0-SNAPSHOT"

sbtVersion := "1.9.8"

lazy val root = (project in file(".")).enablePlugins(PlayJava)
libraryDependencies += javaForms
scalaVersion := "2.13.13"

libraryDependencies += guice

// PostgreSQL driver
libraryDependencies += "org.postgresql" % "postgresql" % "42.2.24"

// Hibernate ORM framework
libraryDependencies += "org.hibernate" % "hibernate-core" % "5.6.4.Final"

// JSON Web Token library
libraryDependencies += "io.jsonwebtoken" % "jjwt-api" % "0.11.2"
libraryDependencies += "io.jsonwebtoken" % "jjwt-impl" % "0.11.2"
libraryDependencies += "io.jsonwebtoken" % "jjwt-jackson" % "0.11.2"


// Play Slick for integrating Slick with Play Framework
libraryDependencies += "com.typesafe.play" %% "play-slick" % "5.2.0"

// SLF4J API
libraryDependencies += "org.slf4j" % "slf4j-api" % "1.7.32"

// Logback for logging
libraryDependencies += "ch.qos.logback" % "logback-classic" % "1.4.14"

//libraryDependencies ++= Seq(
//  // Your other dependencies
//  "io.jsonwebtoken" % "jjwt-impl" % "0.12.4" exclude("com.fasterxml.jackson.core", "jackson-databind"),
//  "io.jsonwebtoken" % "jjwt-api" % "0.12.3" exclude("com.fasterxml.jackson.core", "jackson-databind"),
//  // Other dependencies
//)
