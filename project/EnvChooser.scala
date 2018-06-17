import sbt.{complete, inputKey}
import complete.Parser
import complete.DefaultParsers._

/**
  * Created by wermuth on 1/10/18.
  */
package object mycustom {

  sealed trait EnvCmd {
    def input: String
  }


  case class TestCmd(cinput: String) extends EnvCmd {
    def input: String = s"TestCmd $cinput"
  }

  case class UatCmd(cinput: String) extends EnvCmd {
    def input: String = s"UatCmd $cinput"
  }

  private val parameterExtractor: Parser[String] = {
    val ext = any.* map (_.mkString)
    ext.examples(Set("FIRST","SECOND"), check = true)
  }

  private val parserForTst: Parser[TestCmd] = {
    parseFromEnv("tst") map TestCmd
  }

  private val parserForUat: Parser[UatCmd] = {
    parseFromEnv("uat") map UatCmd
  }

  def parseFromEnv(envName: String): Parser[String] = {
    Space ~> literal(envName) ~> Space ~> parameterExtractor
  }

  val inputCmdParser: Parser[EnvCmd] = parserForTst | parserForUat
}