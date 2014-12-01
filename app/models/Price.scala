package models

import org.joda.time.LocalDate
import play.api.libs.json._
import play.api.libs.functional.syntax._
import play.api.data.validation.ValidationError
import java.lang.Float.{ valueOf => parseFloat }

/** Represents a "price" value, i.e. money.
  * Stores the price internally as cents, to avoid
  * rounding errors associated with floats.
  *
  * @param cents The number of cents in the price,
  * i.e. 1/100 of the number of dollars.
  */
case class Price(cents: Int) extends AnyVal {
	def dollarAmount = cents.toFloat / 100

	override def toString = {
		val d = cents / 100
		val c = cents % 100
		val cs = if (c < 10) "0" + c else c.toString
		s"$$$d.$cs"
	}
}

object Price {

	/** Reads price values as a String or Number containing the dollar value,
	  * e.g. `"123.40"` or `123.4`.
	  */
	implicit val priceRead: Reads[Price] = {
		val error = JsError(Seq(JsPath -> Seq(ValidationError("error.expected.price"))))

		Reads {
			case JsString(s) =>
				try {
					val cents = (parseFloat(s) * 100).toInt
					JsSuccess(Price(cents))
				} catch { case e: NumberFormatException => error }

			case JsNumber(num) => JsSuccess(Price((num * 100).toInt))

			case _ => error
		}
	}

	implicit val priceWrite = Writes[Price] { price =>
		JsNumber(BigDecimal(price.cents) / 100)
	}

}