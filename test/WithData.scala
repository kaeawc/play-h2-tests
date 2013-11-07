package test

trait WithData extends Instance {

  def setupData {
    models.Widget.initializeDatabase
  }
}