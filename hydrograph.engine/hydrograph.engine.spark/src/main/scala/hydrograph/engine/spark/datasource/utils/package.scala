package hydrograph.engine.spark.datasource

import java.text.SimpleDateFormat
import java.util.{TimeZone, Locale}

package object utils {
  def simpleDateFormat(dateFormat: String): SimpleDateFormat = if (!(dateFormat).equalsIgnoreCase("null")){
    val date = new SimpleDateFormat(dateFormat)
    date.setLenient(false)
    date.setTimeZone(TimeZone.getDefault)
    date
  } else null
}