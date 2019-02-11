import Dependencies._

ThisBuild / scalaVersion     := "2.12.8"
ThisBuild / version          := "0.1.0"
ThisBuild / organization     := "com.voraparshva"
ThisBuild / organizationName := "voraparshva"

lazy val root = (project in file("."))
  .settings(
    name := "cohort-analysis",
    libraryDependencies ++= Seq(
    	scalaTest % Test,
    	"org.typelevel" %% "cats-core" % "1.6.0"
    )
  )

// See https://www.scala-sbt.org/1.x/docs/Using-Sonatype.html for instructions on how to publish to Sonatype.
