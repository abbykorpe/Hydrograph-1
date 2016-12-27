package hydrograph.engine.spark.components.platform

import hydrograph.engine.core.component.entity.elements.SchemaField
import org.apache.spark.sql.types.StructType
import org.apache.spark.sql.{DataFrame, SparkSession}

import scala.collection.mutable
import scala.collection.mutable.ListBuffer
import java.util.Set

/**
  * Created by gurdits on 10/17/2016.
  */
class BaseComponentParams() extends Serializable {


  val dataFrameList = new ListBuffer[DataFrame]()

  val schemaFieldList = new ListBuffer[Set[SchemaField]]()

  val dataFameMap=new mutable.HashMap[String,DataFrame]()
  
  val schemaFieldMap = new mutable.HashMap[String,Set[SchemaField]]()

  var sparkSession: SparkSession = null

  var schemaField:Array[SchemaField] = null

  def addSchemaFields(fields: Array[SchemaField]) = {
    schemaField = fields
  }

  def getSchemaFields(): Array[SchemaField] ={
    schemaField
  }

  def getSparkSession(): SparkSession = {
    sparkSession
  }

  def setSparkSession(ss: SparkSession): Unit = {
    sparkSession = ss
  }

  def addinputDataFrame(dataFrame: DataFrame): Unit = {
    dataFrameList += dataFrame
  }

  def addCompIDAndInputDataFrame(compID:String,dataFrame: DataFrame): Unit ={
    dataFameMap += (compID->dataFrame)
  }
  
    def addCompIDAndInputSchema(compID:String,schema: Set[SchemaField]): Unit ={
    schemaFieldMap += (compID->schema)
  }


  def getDataFrame(): DataFrame = {
    dataFrameList.head
  }

  def getDataFrameList(): ListBuffer[DataFrame] = {
    dataFrameList
  }

  def getDataFrameMap(): mutable.HashMap[String,DataFrame] ={
    dataFameMap
  }
  
    def getSchemaFieldMap(): mutable.HashMap[String,Set[SchemaField]] ={
    schemaFieldMap
  }

  def addInputSchema(schema: Set[SchemaField]): Unit = {
    schemaFieldList += schema
  }
  
  def getSchemaFieldList(): ListBuffer[Set[SchemaField]] = {
    schemaFieldList
  }
  
}
