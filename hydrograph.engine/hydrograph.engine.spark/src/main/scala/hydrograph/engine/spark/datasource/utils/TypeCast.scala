/*******************************************************************************
 * Copyright 2017 Capital One Services, LLC and Bitwise, Inc.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package hydrograph.engine.spark.datasource.utils

import java.math.BigDecimal
import java.sql.{Date, Timestamp}
import java.text.{NumberFormat, SimpleDateFormat}
import java.util.{Locale, TimeZone}

import org.apache.spark.sql.types.{DateType, StringType, _}
import org.json4s.ParserUtil.ParseException

import scala.util.Try


/**
  * Utility functions for type casting
 */

object TypeCast {

 /* private def simpleDateFormat(dateFormat: String): SimpleDateFormat = if (!(dateFormat).equalsIgnoreCase("null")) {
    val date = new SimpleDateFormat(dateFormat, Locale.getDefault)
    date.setLenient(false)
    date.setTimeZone(TimeZone.getDefault)
    date
  } else null*/
@throws(classOf[Exception])
  def inputValue(value: Any, castType: DataType, nullable: Boolean = true, nullValue:String, treatEmptyValuesAsNulls:Boolean=true, dateFormat: SimpleDateFormat) : Any= {

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
          case _: TimestampType  => {
            try{
              new Timestamp(dateFormat.parse(value.toString).getTime)
            }catch{
              case e:ParseException => throw new RuntimeException(e.getMessage)
            }

          }
          case _: DateType =>  {
            try{
              new Date(dateFormat.parse(value.toString).getTime)
            }catch{
              case e:Exception => {
                throw new RuntimeException(", Error being -> "+e.getMessage)
              }
            }
          }
          case _: StringType => value
          case _ => throw new RuntimeException(s"Unsupported type: ${castType.typeName}")
        }
    }
  }

  def outputValue(value: Any, castType: DataType, dateFormat: SimpleDateFormat) : AnyRef= {

    castType match {
      case _: TimestampType => if (value == null) "" else if (!dateFormat.equals("null")) dateFormat.format(new Date(value.asInstanceOf[Timestamp].getTime)) else value.toString
      case _: DateType => if (value == null) "" else if (!dateFormat.equals("null")) dateFormat.format(value) else value.toString
      case _ => value.asInstanceOf[AnyRef]
    }
  }
}
