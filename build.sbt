name := "conx"

version := "0.1"

scalaVersion := "2.9.0-1"

pollInterval := 300

libraryDependencies ++= Seq(
    "org.specs2" %% "specs2" % "1.5",
    // with Scala 2.9.0
    "org.specs2" %% "specs2-scalaz-core" % "6.0.RC2" % "test"
  )

testFrameworks += new TestFramework("org.specs2.runner.SpecsFramework")

resolvers ++= Seq(
  "snapshots" at "http://scala-tools.org/repo-snapshots",
  "releases" at "http://scala-tools.org/repo-releases"
)

// append -deprecation to the options passed to the Scala compiler
scalacOptions += "-deprecation"

scalacOptions += "-unchecked"
