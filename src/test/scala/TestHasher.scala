import com.qinxuzhou.Hasher
import org.scalatest.{FlatSpec, Matchers}

class TestHasher extends FlatSpec with Matchers {

  "sha256Hash" should "hash correctly with sha256" in {
    Hasher.sha256Hash("Rusty is a cowboy!") should equal("e0baa113fe0dd65bc88b4144c6b12c89f362a3617644315b48b36f668f7fa1d6")
  }

}
