package test.custom

import play.api.test.{FakeApplication,WithApplication}
import org.specs2.execute.{Result, AsResult}

class Instance(implicit app:FakeApplication)
extends test.Instance(app)
with test.NoSetup {

  override def around[T: AsResult](t: => T): Result = super.around {
    setupData
    t
  }
}