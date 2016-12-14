package hydrograph.engine.spark.fixedwidth.datasource

import hydrograph.engine.spark.datasource.utils.{TextFile, TypeCast}
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.sources.{BaseRelation, TableScan}
import org.apache.spark.sql.types.StructType
import org.apache.spark.sql.{Row, SQLContext}
import org.slf4j.{Logger, LoggerFactory}

class FixedWidthRelation(path: String, charset: String, fieldslength: String,
                         strict:Boolean, safe:Boolean, dateFormats:Array[String], userSchema: StructType)
                        (@transient val sqlContext: SQLContext)
  extends BaseRelation with TableScan with Serializable {
  private val LOG:Logger = LoggerFactory.getLogger(classOf[FixedWidthRelation])
  override def schema: StructType = {
    if (this.userSchema == null)
      throw new RuntimeException
    userSchema
  }

  override def buildScan(): RDD[Row] = {
    val fileRDD = TextFile.withCharset(sqlContext.sparkContext,path,charset)
    val schemafields=schema.fields

   val rowRDD =  fileRDD.mapPartitions(iterator => {
      iterator.map( row => {
        if (strict && (row.length != toIntLength(fieldslength).sum)){
          LOG.error("Input row does not have enough length to parse all fields. Input length is "
            + row.length()
            + ". Sum of length of all fields is "
            + toIntLength(fieldslength).sum
            + "\nRow being parsed: " + row)
          throw new RuntimeException("Input row does not have enough length to parse all fields. Input length is "
            + row.length()
            + ". Sum of length of all fields is "
            + toIntLength(fieldslength).sum
            + "\nRow being parsed: " + row)
        }
        def getValue(fieldsLength: List[Int], acc: Int,counter:Int): List[Any] = fieldsLength match {
          case List() => List()
          case x :: xs =>
            try {
              val fieldValue =
                TypeCast.castingInputData(row.substring(acc, acc + x), schemafields(counter).dataType,
                  schemafields(counter).nullable, "", true, dateFormats(counter))

              List(fieldValue) ++ getValue(xs, acc + x,counter+1)
            } catch {
              case e:StringIndexOutOfBoundsException =>
                LOG.error("Field "+ schemafields(counter).name +" does not have enough " +
                  "length as specified in the schema. Field value is : '" + row.substring(acc)
                  + "' which has length '" + row.substring(acc).length + "' but length specified " +
                  "in the schema is " + x)
                throw new RuntimeException("Field "+ schemafields(counter).name +" does not have enough " +
                "length as specified in the schema. Field value is : '" + row.substring(acc)
                + "' which has length '" + row.substring(acc).length + "' but length specified " +
                "in the schema is " + x)
              case e:Exception =>
                LOG.error("Field "+ schemafields(counter).name +" does not have enough " +
                  "length as specified in the schema. Field value is : '" + row.substring(acc)
                  + "' which has length '" + row.substring(acc).length + "' but length specified " +
                  "in the schema is " + x)
                  throw new RuntimeException("Field "+ schemafields(counter).name +" has " +
                    "value "+ row.substring(acc, acc + x) + " cannot be coerced to "
                  + schemafields(counter).dataType )
            }
        }
        Row.fromSeq(getValue(toIntLength(fieldslength), 0,0))
      })
    })
    LOG.info("Fixed Width Input File successfully read at path " + path )
    rowRDD
  }

  private def toIntLength(fieldsLen: String): List[Int] = {
    val len = fieldsLen.split(",")
    len.map(x => x.toInt).toList
  }

}