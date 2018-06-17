addSbtPlugin("org.scalastyle" %% "scalastyle-sbt-plugin" % "0.5.0")

resolvers += "sonartype-releases" at "https://oss.sonatype.org/content/repositories/releases/"

libraryDependencies ++= Seq(
  "org.apache.velocity" % "velocity" % "1.7"
)