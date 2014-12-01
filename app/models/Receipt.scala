package models

import org.joda.time.LocalDate
import play.api.libs.json._
import play.api.libs.functional.syntax._
import play.api.data.validation.ValidationError
import java.lang.Float.{ valueOf => parseFloat }

case class Receipt(location: String, category: String, price: Price, date: LocalDate, note: String)

object Receipt {

	implicit val receiptReads: Reads[Receipt] = (
		(JsPath \ "location").read[String] and
		(JsPath \ "category").read[String] and
		(JsPath \ "price").read[Price] and
		(JsPath \ "date").read[LocalDate] and
		(JsPath \ "note").readNullable[String].map(_ getOrElse "")
	)(Receipt.apply _)

	implicit val receiptWrites: Writes[Receipt] = (
		(JsPath \ "location").write[String] and
		(JsPath \ "category").write[String] and
		(JsPath \ "price").write[Price] and
		(JsPath \ "date").write[LocalDate] and
		(JsPath \ "note").write[String]
	)(unlift(Receipt.unapply))
}