package test

import models.Widget

import org.specs2.mutable._

import play.api.Logger
import play.api.libs.json.Json
import play.api.test.{FakeRequest,WithApplication}
import play.api.test.Helpers._

trait WidgetSpec[W<:WithApplication] extends Specification {

  def getInstance:W

  def getWidget(expected:Option[Widget] = None) = {

    val request = FakeRequest(GET, "/widget/1")

    val response = route(request).get

    expected match {
      case Some(widget:Widget) => {

        status(response) mustEqual OK

        contentType(response) must beSome("application/json")
      }
      case _ => {

        status(response) mustEqual 404

        contentType(response) must beNone
      }
    }
  }

  def findWidget = {

    val request = FakeRequest(GET, "/find/widget/NotInDatabase")

    val response = route(request).get

    status(response) mustEqual OK

    contentType(response) must beSome("application/json")
  }

  def postWidget = {

    val request = FakeRequest(POST, "/widget")

    val data = Json.obj("name" -> "NewWidget")

    val response = route(request,data).get

    status(response) mustEqual 201

    contentType(response) must beSome("application/json")

    val content = contentAsString(response) 

    val json =
    try {
      Json.parse(content)
    } catch {
      case e:Exception => failure("Response was not properly formatted JSON")
    }

    Widget.parse(json) match {
      case Some(widget:Widget) => {
        widget.id mustEqual 1L
        widget.name mustEqual "NewWidget"
      }
      case _ => failure("Response should be a widget")
    }
  }

  def putWidget(expected:Option[Widget] = None) = {

    val request = FakeRequest(PUT, "/widget/1")

    val data = Json.obj("name" -> "DifferentWidget")

    val response = route(request,data).get

    expected match {
      case Some(widget:Widget) => {
        status(response) mustEqual 202

        contentType(response) must beSome("application/json")

        val content = contentAsString(response) 

        val json =
        try {
          Json.parse(content)
        } catch {
          case e:Exception => failure("Response was not properly formatted JSON")
        }

        Widget.parse(json) match {
          case Some(widget:Widget) => {
            widget.id mustEqual 1L
            widget.name mustEqual "DifferentWidget"
          }
          case _ => failure("Response should be a widget")
        }
      }
      case _ => {

        status(response) mustEqual 404

        contentType(response) must beNone
      }
    }

  }

  def deleteWidget(expected:Option[Widget] = None) = {

    val request = FakeRequest(DELETE, "/widget/1")

    val response = route(request).get

    expected match {
      case Some(widget:Widget) => {

        status(response) mustEqual 202

        contentType(response) must beSome("application/json")

        val content = contentAsString(response) 

        val json =
        try {
          Json.parse(content)
        } catch {
          case e:Exception => failure("Response was not properly formatted JSON")
        }

        Widget.parse(json) match {
          case Some(widget:Widget) => {
            widget.id mustEqual 1L
            widget.name mustEqual "DifferentWidget"
          }
          case _ => failure("Response should be a widget")
        }
      }
      case _ => {

        status(response) mustEqual 404

        contentType(response) must beNone
      }
    }
  }

  "GET /widget/:id" should {
    "get a widget" in getInstance { getWidget() }
  }

  "GET /find/widget/:name" should {
    "return 0 widgets" in getInstance { findWidget }
  }

  "POST /widget" should {
    "create a widget and return it" in getInstance { postWidget }
  }
}
