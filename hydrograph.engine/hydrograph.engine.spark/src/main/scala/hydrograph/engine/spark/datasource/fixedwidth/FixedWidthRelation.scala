package hydrograph.engine.spark.datasource.fixedwidth

import java.text.SimpleDateFormat

import hydrograph.engine.spark.datasource.utils.{TextFile, TypeCast}
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.sources.{BaseRelation, TableScan}
import org.apache.spark.sql.types.StructType
import org.apache.spark.sql.{Row, SQLContext}
import org.slf4j.{Logger, LoggerFactory}

class FixedWidthRelation(path: String, charset: String, fieldslength: String,
                         strict:Boolean, safe:Boolean, dateFormats: List[SimpleDateFormat], userSchema: StructType)
                        (@transient val sqlContext: SQLContext)
  extends BaseRelation with TableScan with Serializable {


  private val LOG:Logger = LoggerFactory.getLogger(classOf[FixedWidthRelation])
  val fieldlength = fieldslength.split(",").map(_.toInt).toList


  val lengthForSubString: List[(Int, Int)] ={
    def create(acc:Int,fieldlengths:List[Int]):List[(Int, Int)] = fieldlengths match{
      case List()=>List()
      case x::xs => (acc,x+acc) :: create(x+acc,xs)
    }
    create(0,fieldlength)
  }

  override def schema: StructType = {
    if (this.userSchema == null)
      throw new RuntimeException
    userSchema
  }

  override def buildScan(): RDD[Row] = {
    val fileRDD = TextFile.withCharset(sqlContext.sparkContext,path,charset)
    val schemaFields=schema.fields


    fileRDD.map( row => {
      if (strict && (row.length != fieldlength.sum)){
        LOG.error("Input row does not have enough length to parse all fields. Input length is "
          + row.length()
          + ". Sum of length of all fields is "
          + fieldlength.sum
          + "\nRow being parsed: " + row)
        throw new RuntimeException("Input row does not have enough length to parse all fields. Input length is "
          + row.length()
          + ". Sum of length of all fields is "
          + fieldlength.sum
          + "\nRow being parsed: " + row)
      }

      val tokenArray = new Array[Any](schemaFields.length)
      var index = 0
      while (index < schemaFields.length) {
        val field = schemaFields(index)
        try {
          tokenArray(index) = TypeCast.inputValue(row.substring(lengthForSubString(index)._1, lengthForSubString(index)._2), field
            .dataType, field
            .nullable, "", true, dateFormats(index))
          index = index + 1
        }
        catch {
          case e:StringIndexOutOfBoundsException =>
            LOG.error("Field "+ field.name +" does not have enough " +
              "length as specified in the schema. Field value is : '" + row.substring(lengthForSubString(index)._1)
              + "' which has length '" + row.substring(lengthForSubString(index)._1).length + "' but length specified " +
              "in the schema is " + fieldlength(index))
            throw new RuntimeException("Field "+ field.name +" does not have enough " +
              "length as specified in the schema. Field value is : '" + row.substring(lengthForSubString(index)._1)
              + "' which has length '" + row.substring(lengthForSubString(index)._1).length + "' but length specified " +
              "in the schema is " + fieldlength(index))
          case e:Exception =>
            LOG.error("Field "+ field.name +" does not have enough " +
              "length as specified in the schema. Field value is : '" + row.substring(lengthForSubString(index)._1)
              + "' which has length '" + row.substring(lengthForSubString(index)._1).length + "' but length specified " +
              "in the schema is " + fieldlength(index))
            throw new RuntimeException("Field "+ field.name +" has " +
              "value "+ row.substring(lengthForSubString(index)._1, lengthForSubString(index)._2) + " cannot be coerced to "
              + field.dataType )
        }
      }
      Row.fromSeq(tokenArray)
    })
  }



}