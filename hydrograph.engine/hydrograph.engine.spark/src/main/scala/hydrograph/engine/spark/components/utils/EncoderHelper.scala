package hydrograph.engine.spark.components.utils

import hydrograph.engine.core.component.entity.elements.SchemaField
import org.apache.spark.sql.types.{DataType, DataTypes, StructField, StructType}

import scala.collection.mutable.ListBuffer

/**
  * Created by gurdits on 10/20/2016.
  */
class EncoderHelper extends Serializable{

  def getDummy(): StructType ={
    StructType(List(StructField("count1", DataTypes.IntegerType, true)
      , StructField("new_city", DataTypes.StringType, true)
      , StructField("id", DataTypes.IntegerType, true)
    ,StructField("name", DataTypes.StringType, true)))
  }

  def getDataType(dataType: String):DataType = {
    Class.forName(dataType).getSimpleName match {
      case "Integer" => DataTypes.IntegerType
      case "String" => DataTypes.StringType
      case "Long" => DataTypes.LongType
    }
  }


  def getStructFieldType(fieldName:String,schemaFields:Array[SchemaField]): DataType ={
    getDataType(schemaFields.filter(s=> s.getFieldName.equals(fieldName))(0).getFieldDataType)
  }

  def getEncoder(outFields:ListBuffer[String],schemaFields:Array[SchemaField]): StructType ={
    val structFields=new Array[StructField](outFields.size)
    outFields.zipWithIndex.foreach(f=>{
      structFields(f._2)=new StructField(f._1,getStructFieldType(f._1,schemaFields),true)
    })
    StructType(structFields)
  }

}
object EncoderHelper{
  def apply(): EncoderHelper ={
    new EncoderHelper()
  }
}
