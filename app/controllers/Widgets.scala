package controllers

import models.Widget

import play.api._
import play.api.mvc._
import play.api.libs.json._
import play.api.data._
import play.api.data.Forms._

import scala.concurrent.{Future,ExecutionContext}
import ExecutionContext.Implicits.global


object Widgets extends Controller {

  val nameForm = Form(
    "name" -> text
  )

  /*
   * GET     /widget/:id
   */
  def get(id:Long) = Action.async {
    Widget.getById(id) map {
      case Some(widget:Widget) => Ok(Json.toJson(widget))
      case _ => NotFound
    }
  }

  /*
   * GET     /find/widget/:name
   */
  def find(name:String) = Action.async {
    Widget.findByName(name) map {
      case widgets:List[Widget] => Ok(Json.toJson(widgets))
      case _ => InternalServerError
    }
  }

  /*
   * POST    /widget
   */
  def create = Action.async {
    implicit request => 

    val bound = nameForm.bindFromRequest()

    bound match {
      case bound if bound.hasErrors => Future { BadRequest }
      case bound => {
        val name = bound.get

        Widget.create(name) map {
          case Some(widget:Widget) => Created(Json.toJson(widget))
          case _ => InternalServerError
        }
      }
    }
  }

  /*
   * PUT     /widget/:id
   */
  def update(id:Long) = Action.async {
    implicit request => 

    val name = nameForm.bindFromRequest().get

    Widget.updateEmail(id,name) map {
      case 0 => NotFound
      case _ => Accepted
    }
  }

  /*
   * DELETE  /widget/:id
   */
  def delete(id:Long) = Action.async {
    Widget.delete(id) map {
      case 0 => NotFound
      case _ => Accepted
    }
  }
}
