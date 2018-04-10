package controllers

import org.scalatestplus.play.PlaySpec
import org.scalatestplus.play.guice.GuiceOneAppPerSuite
import play.api.test.FakeRequest
import play.api.test.Helpers.{status, _}
import services.GreetingService

object FakeMorningGreeter extends GreetingService {
  override def greeting: String = "Good morning!"
}

object FakeAfternoonGreeter extends GreetingService {
  override def greeting: String = "Good afternoon!"
}

class WelcomeControllerSpec extends PlaySpec with GuiceOneAppPerSuite {
  "WelcomeController GET" should {
    "return a successful response" in {
      val controller = new WelcomeController(FakeMorningGreeter)
      val result = controller.welcome().apply(FakeRequest())
      status(result) mustBe OK
    }

    "respond to the /welcome url" in {
      //Need to specific Host header to get through AllowedHostFilter
      val request = FakeRequest(GET, "/welcome").withHeaders("Host" -> "localHost")
      val home = route(app, request).get
      status(home) mustBe OK
    }

    "return some html" in {
      val controller = new WelcomeController(FakeMorningGreeter)
      val result = controller.welcome().apply(FakeRequest())
      contentType(result) mustBe Some("text/html")
    }

    "say good morning and have a title" in {
      val controller = new WelcomeController(FakeMorningGreeter)
      val result = controller.welcome().apply(FakeRequest())
      contentAsString(result) must include ("<h1>Good morning!</h1>")
      contentAsString(result) must include ("<title>Welcome!</title>")
    }

    "say good afternoon when it's the afternoon and have a title" in {
      val controller = new WelcomeController(FakeAfternoonGreeter)
      val result = controller.welcome().apply(FakeRequest())
      contentAsString(result) must not include "<h1>Good morning!</h1>"
      contentAsString(result) must include ("<h1>Good afternoon!</h1>")
      contentAsString(result) must include ("<title>Welcome!</title>")
    }
  }
}