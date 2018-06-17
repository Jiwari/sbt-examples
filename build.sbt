import sbt.Def

name := "sbt-fluff"

version := "1.0"

scalaVersion := "2.12.1"

libraryDependencies ++= Seq(
  "org.scalatest" %% "scalatest" % "3.0.4" % "test",
  "com.typesafe" % "config" % "1.3.2"
)

lazy val prepareTest = taskKey[Unit]("Pre test")
lazy val prepareUATTest = taskKey[Unit]("Pre UAT Test")
lazy val changeAppConf = taskKey[File]("Changes the application.conf file")

prepareTest in Test := {
  val log = streams.value.log
  log.info("Pre test! Sleeping..")
  Thread.sleep(2000)
  log.info("Sleep done!")
  System.setProperty("app.conf.name", "tst.application.conf")
}

changeAppConf in Test := {
  val log = streams.value.log
  log.info("Configuring application.conf!")
  Thread.sleep(2000)
  log.info("Done configuring application.conf!")
  val sourceName = System.getProperty("app.conf.name")
  val newAppConf = (resourceDirectory in Test).value / sourceName
  val appConf = (resourceDirectory in Test).value / "application.conf"
  IO.copyDirectory(newAppConf, appConf, overwrite = true)
  newAppConf
}


org.scalastyle.sbt.ScalastylePlugin.Settings

prepareUATTest in Test := {
  val log = streams.value.log
  log.info("Pre UAT test! Sleeping")
  Thread.sleep(2000)
  log.info("Sleep done!")
  System.setProperty("app.conf.name", "uat.application.conf")
}
lazy val uatTest = taskKey[String]("UAT Test")

lazy val tstTest = taskKey[String]("Test Test")

uatTest in Test := {
  val log = streams.value.log
  log.info("UAT Tests running")

  preparation(Test, prepareUATTest).value
  "UAT Test"
}

def logInfo(toBeLogged: String): Def.Initialize[Task[Unit]] = Def.task {
  val log = streams.value.log
  log.info(toBeLogged)
}

def preparation(scope: Configuration, preparationTask: TaskKey[Unit]): Def.Initialize[Task[Unit]] = Def.task {
  (test in scope)
    .dependsOn(changeAppConf in scope)
    .dependsOn(preparationTask in scope).value
}

tstTest in Test := {
  logInfo("TST Tests running").value
  preparation(Test, prepareTest).value

  "TST Test"
}

lazy val envConfig = inputKey[Unit]("")

envConfig := {
  val resParse: mycustom.EnvCmd = mycustom.inputCmdParser.parsed

  logInfo(resParse.input)
}

val scalastyleReport = taskKey[File]("")

scalastyleReport := {
  val ignored = org.scalastyle.sbt.PluginKeys.scalastyle.toTask("").value
  val file = ScalastyleReport.report(target.value / "html-test-report",
    "scalastyle-report.html",
    (baseDirectory in ThisBuild).value / "project/scalastyle-report.html",
    target.value / "scalastyle-result.xml")
  logInfo("created report " + file.getAbsolutePath)
  file
}

