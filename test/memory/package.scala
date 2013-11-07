package test

import play.api.test.{FakeApplication,WithApplication}
import play.api.test.Helpers.inMemoryDatabase

package object memory {

  implicit def app = FakeApplication(additionalConfiguration = inMemoryDatabase("test"))
}