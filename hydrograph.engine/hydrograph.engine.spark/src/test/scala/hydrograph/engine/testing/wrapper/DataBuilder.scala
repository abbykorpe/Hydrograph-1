package hydrograph.engine.testing.wrapper

import org.apache.spark.sql.functions._
import org.apache.spark.sql.{Column, DataFrame, SparkSession, Row}
import org.apache.spark.sql.types.{DataTypes, StructField, StructType}

import scala.collection.JavaConverters._
import scala.collection.mutable.ListBuffer

/**
  * Created by gurdits on 12/3/2016.
  */

case class Bucket(structType: StructType,dataFrame: DataFrame){

  def result():Array[Row]= {
    val inputColumn = new Array[Column](structType.fieldNames.size)
    structType.fieldNames.zipWithIndex.foreach(f => {
      inputColumn(f._2) = col(f._1)
    })
    dataFrame.select(inputColumn:_*).collect()
  }


}

 class DataBuilder(val fields: StructType, val data: List[Row]) {

  def this(fields: StructType) = {
    this(fields, List())
  }

  def addData(row: List[Any]): DataBuilder = {
    verifyDataWithField(row)
     new DataBuilder(fields,Row.fromSeq(row) :: data)
  }

  private def verifyDataWithField(row: List[Any]): Unit = {
    if (row.size != fields.size)
      throw new RuntimeException("Fields and Data should be same size")
  }

  def build(): DataFrame = {

    val sparkSession = SparkSession.builder()
      .master("local")
      .config("spark.sql.shuffle.partitions", "1")
      .config("spark.sql.warehouse.dir", "file:///tmp")
      .getOrCreate()

    sparkSession.createDataFrame(data.asJava,StructType(fields))
  }
}
