package controllers

import com.google.inject.Inject
import play.api.mvc.{Action, Controller}
import services.SandwichService

class SandwichController @Inject()(sandwichService: SandwichService)extends Controller {
  def sandwiches() = Action {
    val sandwiches = sandwichService.sandwiches
    Ok(views.html.sandwiches(sandwiches))
  }
}