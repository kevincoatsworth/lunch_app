package controllers

import play.api.mvc.{Action, Controller}

class WelcomeController extends Controller {
  def welcome() = Action {
    Ok.as(HTML)
  }
}
