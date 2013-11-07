package test.default

import org.specs2.runner.JUnitRunner
import org.junit.runner.RunWith

@RunWith(classOf[JUnitRunner])
class WidgetSpec extends test.WidgetSpec[Instance] {

  def getInstance = new Instance

}