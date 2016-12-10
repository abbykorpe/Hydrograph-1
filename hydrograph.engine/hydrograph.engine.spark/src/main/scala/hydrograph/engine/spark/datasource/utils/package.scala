package hydrograph.engine.spark.datasource
import java.text.SimpleDateFormat

package object utils {
  def simpleDateFormat(dateFormat: String): SimpleDateFormat = if (!(dateFormat).equalsIgnoreCase("null")) new SimpleDateFormat(dateFormat) else null
}