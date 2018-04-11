package services

import com.google.inject.ImplementedBy
import models.Sandwich

@ImplementedBy(classOf[RealSandwichService])
trait SandwichService {
  def sandwiches() : List[Sandwich]
}

class RealSandwichService extends SandwichService {
  //just an empty list
  override def sandwiches(): List[Sandwich] = List()
}
