package models

import anorm._
import anorm.SqlParser._

import play.api.Logger
import play.api.db.DB
import play.api.Play.current
import play.api.libs.json._

import java.util.Date
import scala.concurrent.{Await, Future, ExecutionContext}
import scala.concurrent.duration._
import ExecutionContext.Implicits.global

case class Widget(
  id       : Long,
  name     : String,
  created  : Date = new Date()
)

object Widget extends ((
  Long,
  String,
  Date
) => Widget) {

  implicit val jsonFormat = Json.format[Widget]

  val widgets =
    long("id") ~
    str("name") ~
    date("created") map {
      case     id~name~created =>
        Widget(id,name,created)
    }

  def parse(json:JsValue) = {
    try {
      Some(Json.fromJson(json).get)
    } catch {
      case e:Exception => None
    }
  }

  def getById(id:Long) = Future {
    DB.withConnection { implicit connection =>
      SQL(
        """
          SELECT
            id,
            name,
            created
          FROM widget
          WHERE id = {id};
        """
      ).on(
        'id -> id
      ).as(widgets.singleOpt)
    }
  }

  def findByName(name:String) = Future {
    DB.withConnection { implicit connection =>
      SQL(
        """
          SELECT
            id,
            name,
            created
          FROM widget
          WHERE name = {name};
        """
      ).on(
        'name -> name
      ).as(widgets *)
    }
  }

  def updateEmail(id:Long,name:String) = Future {
    DB.withConnection { implicit connection =>
      SQL(
        """
          UPDATE widget
          SET name = {name}
          WHERE id = {id};
        """
      ).on(
        'id    -> id,
        'name -> name
      ).executeUpdate()
    }
  }

  def delete(id:Long) = Future {
    DB.withConnection { implicit connection =>
      SQL(
        """
          DELETE
          FROM widget
          WHERE id = {id};
        """
      ).on(
        'id -> id
      ).executeUpdate()
    }
  }

  def countAll = Future {
    DB.withConnection { implicit connection =>
      val result = SQL(
        """
          SELECT COUNT(1) count
          FROM widget;
        """
      ).apply()

      try {
        Some(result.head[Long]("count"))
      } catch {
        case e:Exception => None
      }
    }
  }

  def create(name:String) = {

    val created           = new Date()

    Future {
      DB.withConnection { implicit connection =>
        SQL(
          """
            INSERT INTO widget (
              name,
              created
            ) VALUES (
              {name},
              {created}
            );
          """
        ).on(
          'name    -> name,
          'created -> created
        ).executeInsert()
      }
    } map {
      case Some(id:Long) => {
        Some(Widget(
          id,
          name,
          created
        ))
      }
      case _ => {
        Logger.error("Widget wasn't created")
        None
      }
    }
  }

  def initializeDatabase {

    val list = data.Name.list

    for (name <- list)
      create(name)  
  }
}
