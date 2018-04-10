package modules

import java.util.Calendar

import com.google.inject.AbstractModule

class Module extends AbstractModule {
  def configure() = {

    val calendar = Calendar.getInstance()

    bind(classOf[Calendar]).toInstance(calendar)
  }
}
