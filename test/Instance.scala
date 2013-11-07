package test

import play.api.test.{FakeApplication,WithApplication}

abstract class Instance(app:FakeApplication) extends WithApplication(app) {

  def setupData
}