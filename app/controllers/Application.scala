package controllers

import play.api._
import play.api.mvc._
import play.twirl.api.Html
import models.Receipt
import play.api.libs.json.JsSuccess
import play.api.libs.json.Json
import play.api.libs.json.JsError
import play.api.db.slick.Config.driver.simple._
import play.api.db.slick.DB
import play.api.Play.current
import _root_.db.Receipts
import _root_.db.ReceiptRecord
import org.joda.time.DateTime

object Application extends Controller {

	def index = Action {
		Ok(views.html.index("Your new application is ready."))
	}

	def submit = Action {
		Ok(views.html.submit())
	}

	def postReceipt = Action(parse.json) { request =>
		request.body.validate[Receipt] match {
			case JsSuccess(receipt, _) =>
				println(s"postReceipt: $receipt")

				DB.withTransaction { implicit session =>
					val now = DateTime.now
					val record = ReceiptRecord(0, accountId = 0, now, now, receipt)
					Receipts.table.insert(record)
				}

				Ok(Json.toJson(receipt))
			case JsError(_) =>
				BadRequest("JSON had bad values")
		}
	}

	def listReceipts = Action {
		DB.withSession { implicit session =>
			val records = Receipts.table.map(_.receipt).list
			Ok(Json.toJson(records))
		}
	}
}