package hydrograph.engine.spark.datasource.utils

import java.math.BigDecimal
import java.sql.{Date, Timestamp}
import java.text.NumberFormat
import java.util.Locale
import org.apache.spark.sql.types.{DateType, StringType, _}
import scala.util.Try


/**
  * Utility functions for type casting
 */

object TypeCast {


  def castingInputData(value: Any, castType: DataType, nullable: Boolean = true, nullValue:String, treatEmptyValuesAsNulls:Boolean=true, inDateFormat:String) : Any= {

     if (value == nullValue && nullable || (value == nullValue && treatEmptyValuesAsNulls)) {
        null
      } else {

        castType match {

          case _: ByteType => value.toString.toByte
          case _: ShortType => value.toString.toShort
          case _: IntegerType => value.toString.toInt
          case _: LongType => value.toString.toLong
          case _: FloatType => Try(value.toString.toFloat)
            .getOrElse(NumberFormat.getInstance(Locale.getDefault).parse(value.toString).floatValue())
          case _: DoubleType => Try(value.toString.toDouble)
            .getOrElse(NumberFormat.getInstance(Locale.getDefault).parse(value.toString).doubleValue())
          case _: BooleanType => value.toString.toBoolean
          case _: DecimalType => new BigDecimal(value.toString.replaceAll(",",""))
          case _: TimestampType  => new Timestamp(simpleDateFormat(inDateFormat).parse(value.toString).getTime)
          case _: DateType =>  new Date(simpleDateFormat(inDateFormat).parse(value.toString).getTime)
          case _: StringType => value
          case _ => throw new RuntimeException(s"Unsupported type: ${castType.typeName}")
        }
    }
  }

  def castingOutputData(value: Any, castType: DataType, outDateFormat:String) : AnyRef= {

    castType match {
        case _: TimestampType => if (value == null) "" else  simpleDateFormat(outDateFormat).format(new Date(value.asInstanceOf[Timestamp].getTime))
        case _: DateType => if (value == null) "" else  simpleDateFormat(outDateFormat).format(value)
        case _ => value.asInstanceOf[AnyRef]
      }
    }
}
