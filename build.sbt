scalaVersion := "2.12.6"

libraryDependencies += "org.scalatest" %% "scalatest" % "3.0.4" % "test"

scalacOptions ++= Seq("-unchecked", "-deprecation", "-language:implicitConversions", "-feature")