package test

import play.api.test.{FakeApplication,WithApplication}

package object custom {

  val config = Map(
      "db.default.driver" -> "org.h2.Driver",
      "db.default.url" -> "jdbc:h2:mem:play;MODE=MYSQL", // ;DB_CLOSE_DELAY=-1 keeps h2 open between tests
      "evolutionplugin" -> "enabled",
      "applyEvolutions.default" -> "true",
      "applyDownEvolutions.default" -> "true"
    )

  implicit def app = FakeApplication(additionalConfiguration = config)
}