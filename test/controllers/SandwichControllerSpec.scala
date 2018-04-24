package controllers

import models.Sandwich
import play.api.inject.bind
import org.scalatestplus.play.PlaySpec
import org.scalatestplus.play.guice.GuiceOneAppPerSuite
import play.api.inject.guice.GuiceApplicationBuilder
import play.api.test.FakeRequest
import play.api.test.Helpers.{status, _}
import services.SandwichService
import scala.concurrent.ExecutionContext.Implicits.global

import scala.concurrent.Future

object FakeNoSandwichService extends SandwichService {
  override def sandwiches(): Future[List[Sandwich]] = Future(List())
}

object FakeSingleSandwichService extends SandwichService {
  override def sandwiches(): Future[List[Sandwich]] = Future(List(Sandwich("Ham", 1.55, "Very tasty")))
}

object FakeMultiSandwichService extends SandwichService {
  val ham = Sandwich("Ham", 1.55, "Very tasty")
  val cheese = Sandwich("Cheese", 2.55, "Cheese tastic")
  val egg = Sandwich("Egg", 1.15, "Fresh")
  override def sandwiches(): Future[List[Sandwich]] = Future(List(ham, cheese, egg))
}

class IntegrationSandwichService extends SandwichService {
  override def sandwiches(): Future[List[Sandwich]] = Future(List())
}

class SandwichControllerSpec extends PlaySpec with GuiceOneAppPerSuite {

  "SandwichController" should {
    "Have some basic information and be accessible at the correct route" in {
      val application = new GuiceApplicationBuilder().overrides(bind[SandwichService].to[IntegrationSandwichService]).build
      //Need to specify Host header to get through AllowedHostsFilter
      val request = FakeRequest(GET, "/sandwiches").withHeaders("Host" -> "localHost")
      val home = route(application, request).get

      //sanitation
      status(home) mustBe OK
      contentType(home) mustBe Some("text/html")
      contentAsString(home) must include("<title>Sandwiches</title>")
      contentAsString(home) must include("<h1>Have a look at today's sandwiches</h1>")
    }

    "give a helpful message when sold out" in {
      val controller = new SandwichController(FakeNoSandwichService)
      val result = controller.sandwiches().apply(FakeRequest())
      contentAsString(result) must include("Sorry, we're sold out")
    }

    "show a single sandwich when only one is available" in {
      val controller = new SandwichController(FakeSingleSandwichService)
      val result = controller.sandwiches().apply(FakeRequest())

      contentAsString(result) must not include ("<p>Sorry, we're sold out</p>")
      contentAsString(result) must include("Please choose a sandwich")
      contentAsString(result) must include("Ham")
      contentAsString(result) must include("Very tasty")
      contentAsString(result) must include("£1.55")
    }

    "show multiple sandwiches when more than one is available" in {
      val controller = new SandwichController(FakeMultiSandwichService)
      val result = controller.sandwiches().apply(FakeRequest())

      contentAsString(result) must not include("<p>Sorry, we're sold out</p>")
      contentAsString(result) must include("Ham")
      contentAsString(result) must include("Very tasty")
      contentAsString(result) must include("£1.55")
      contentAsString(result) must include("Cheese")
      contentAsString(result) must include("Cheese tastic")
      contentAsString(result) must include("£2.55")
      contentAsString(result) must include("Egg")
      contentAsString(result) must include("Fresh")
      contentAsString(result) must include("£1.15")
    }
  }
}