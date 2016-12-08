package hydrograph.engine.testing.wrapper

import java.lang.reflect.Type

import org.apache.spark.sql.functions._
import org.apache.spark.sql.types.{DataType, DataTypes, StructField, StructType}
import org.apache.spark.sql.{Column, DataFrame, Row, SparkSession}

import scala.collection.JavaConverters._

/**
  * Created by gurdits on 12/3/2016.
  */

case class Bucket(fields: Fields,dataFrame: DataFrame){

  def result():Array[Row]= {
    val inputColumn = new Array[Column](fields.structType.fieldNames.size)
    fields.structType.fieldNames.zipWithIndex.foreach(f => {
      inputColumn(f._2) = col(f._1)
    })
    dataFrame.select(inputColumn:_*).collect()
  }


}

class Fields(val fields:List[String],val structType:StructType){

  def applyTypes(dataTypes:List[Type]): Fields ={
    Fields(this.fields,dataTypes)
  }

}
object Fields {
  def apply(fields:List[String]):Fields = new Fields(fields,applyDefaultType(fields))

  def apply(fields:List[String],dataTypes:List[Type]):Fields = new Fields(fields,StructType(fieldsCreator(fields,
    dataTypes)))

  private def applyDefaultType(fields:List[String]): StructType ={
    def create(fields:List[String]):List[StructField]=fields match{
      case List()=>List()
      case x::xs=> StructField(x,DataTypes.StringType) :: create(xs)
    }
    StructType(create(fields))
  }

  private def fieldsCreator(fields:List[String],dataTypes:List[Type]):
  List[StructField] =
    (fields,dataTypes) match {
      case (x,y) if x.size != y.size =>  throw new RuntimeException("fields and dataTypes are not equal")
      case (List(),List()) => List()
      case (f::fx,d :: dx) => StructField(f,getDataType(d)) :: fieldsCreator(fx,dx)
    }

  private def getDataType(dataType: Type):DataType = {
    dataType.getTypeName.split("\\.").last match {
      case "Integer" => DataTypes.IntegerType
      case "String" => DataTypes.StringType
      case "Long" => DataTypes.LongType
    }
  }
}

 class DataBuilder(val fields: Fields, val data: List[Row]) {

  def this(fields: Fields) = {
    this(fields, List())
  }

  def addData(row: List[Any]): DataBuilder = {
    verifyDataWithField(row)
     new DataBuilder(fields,Row.fromSeq(row) :: data)
  }

  private def verifyDataWithField(row: List[Any]): Unit = {
    if (row.size != fields.structType.size)
      throw new RuntimeException("Fields and Data should be same size")
  }

  def build(): DataFrame = {

    val sparkSession = SparkSession.builder()
      .master("local")
      .config("spark.sql.shuffle.partitions", "1")
      .config("spark.sql.warehouse.dir", "file:///tmp")
      .getOrCreate()

    sparkSession.createDataFrame(data.asJava,fields.structType)
  }
}
