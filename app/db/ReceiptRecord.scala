package db

import models.Receipt
import org.joda.time.DateTime
import play.api.db.slick.Config.driver.simple._
import models.Price
import org.joda.time.LocalDate

case class ReceiptRecord(
	id: Int,
	accountId: Int,
	submitDate: DateTime,
	editDate: DateTime,
	receipt: Receipt)

object Receipts {
	val maxLocationLength = 64
	val maxCategoryLength = 64
	val maxNoteLength = 512

	val table = TableQuery[Receipts]
}

class Receipts(tag: Tag) extends Table[ReceiptRecord](tag, "RECEIPTS") {
	def id = column[Int]("ID", O.PrimaryKey, O.AutoInc)
	def accountId = column[Int]("ACCOUNT_ID", O.NotNull)
	def submitDate = column[DateTime]("SUBMIT_DATE", O.NotNull)
	def editDate = column[DateTime]("EDIT_DATE", O.NotNull)

	def location = column[String]("LOCATION", O.NotNull, O.Length(Receipts.maxLocationLength, varying = true))
	def category = column[String]("CATEGORY", O.NotNull, O.Length(Receipts.maxCategoryLength, varying = true))
	def price = column[Price]("PRICE", O.NotNull)
	def receiptDate = column[LocalDate]("RECEIPT_DATE", O.NotNull)
	def note = column[String]("NOTE", O.NotNull, O.Length(Receipts.maxNoteLength, varying = true))

	def receipt = (location, category, price, receiptDate, note) <> ((Receipt.apply _).tupled, Receipt.unapply)
	def * = (id, accountId, submitDate, editDate, receipt) <> (ReceiptRecord.tupled, ReceiptRecord.unapply)
}