package controllers

import org.scalatestplus.play.PlaySpec
import org.scalatestplus.play.guice.GuiceOneAppPerSuite
import play.api.test.FakeRequest
import play.api.test.Helpers.{status, _}

class WelcomeControllerSpec extends PlaySpec with GuiceOneAppPerSuite {
    "WelcomeController GET" should {
      "return a successful response" in {
        val controller = new WelcomeController
        val result = controller.welcome().apply(FakeRequest())
        status(result) mustBe OK
      }
    }

  "respond to the /welcome url" in {
    //Need to specific Host header to get through AllowedHostFilter
    val request = FakeRequest(GET, "/welcome").withHeaders("Host" -> "localHost")
    val home = route(app, request).get
    status(home) mustBe OK
  }

  "return some html" in {
    val controller = new WelcomeController
    val result = controller.welcome().apply(FakeRequest())
    contentType(result) mustBe Some("text/html")
  }
}
