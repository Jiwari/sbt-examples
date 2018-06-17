
import sbt.complete.DefaultParsers.{Digit, Space, chars, token}

lazy val myParseHelp = Help(
  Seq(
    "myParse <version>" ->
      "Do a parse"
  ),
  Map(
    "myParse" ->
      """Collects the release number informed and do a parse.
        |If invalid version number is informed, the parse will fail.
      """.stripMargin
  )
)

val MyParse = Command.apply("myParse", myParseHelp) {
  state =>
    val version = (Digit ~ chars(".0123456789").*) map {
      case (first, rest) => (first +: rest).mkString
    }

    val complete = (chars("v") ~ token(version, "<version number>")) map {
      case (v, number) => v + number
    }

    Space ~> complete
} {
  (state, d) => "clean" :: state
    // The command 'all' will run tasks in parallel. Ex: all test it
}

commands += MyParse