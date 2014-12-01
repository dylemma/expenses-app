import java.sql.{ Date => SqlDate }
import java.sql.{ Timestamp => SqlTimestamp }
import play.api.db.slick.Config.driver.simple._
import models.Price
import org.joda.time.LocalDate
import org.joda.time.DateTime
import models.Receipt

package object db {

	implicit val priceColumnType =
		MappedColumnType.base[Price, Int](_.cents, Price(_))

	implicit val jodaLocalDateColumnType =
		MappedColumnType.base[LocalDate, SqlDate](
			{ ld => new SqlDate(ld.toDate.getTime) },
			{ sd => LocalDate.fromDateFields(sd) }
		)

	implicit val jodaDateTimeColumnType =
		MappedColumnType.base[DateTime, SqlTimestamp](
			{ dt => new SqlTimestamp(dt.getMillis) },
			{ st => new DateTime(st.getTime) }
		)
}