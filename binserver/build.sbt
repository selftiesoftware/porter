name := "siigna-server"

version := "preAlpha"

organization := "com.siigna"

scalaVersion := "2.9.2"

scalaSource in (Compile, run) <<= (baseDirectory in Compile)(_ / "src" / "main" / "scala")

scalaSource in Test <<= (baseDirectory in Compile)(_ / "src" / "test" / "scala")

mainClass in (Compile, run) := Some("com.siigna.server.SiignaServer")

resolvers += "Siigna" at "http://rls.siigna.com/base"
//resolvers += "Siigna" at "../rls/base"

fork in run := true

initialCommands in console := "import com.siigna.util.server"

libraryDependencies ++= Seq(
  "com.siigna" %% "siigna-main" % "preAlpha",
  "com.siigna" %% "siigna-module" % "preAlpha",
  "postgresql" % "postgresql" % "9.1-901.jdbc4",
  "org.scalatest" %% "scalatest" % "1.8" % "test",
  "org.clapper" %% "grizzled-slf4j" % "0.6.9",
  "ch.qos.logback" % "logback-classic" % "1.0.1"
)
