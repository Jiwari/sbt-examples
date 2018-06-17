import com.typesafe.config.ConfigFactory
import org.scalatest.{FlatSpec, Matchers}

/**
  * Created by wermuth on 1/8/18.
  */
class FluffingTest extends FlatSpec with Matchers {

  "My test" should "do something" in {
    val conf = ConfigFactory.load()
    println("Printing the mapping")

    println("env.db.url: " + conf.getString("env.db.url"))
    println("env.db.port: " + conf.getInt("env.db.port"))
    println("env.db.sid: " + conf.getString("env.db.sid"))
    println("env.url: " + conf.getString("env.url"))

    1 should be (1)
  }

  it should "blah" in {
    2 should be (2)
  }

}
