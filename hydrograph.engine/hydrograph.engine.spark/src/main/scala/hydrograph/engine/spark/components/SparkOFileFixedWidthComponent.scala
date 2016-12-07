package hydrograph.engine.spark.components

import java.util

import hydrograph.engine.core.component.entity.{OutputFileFixedWidthEntity, OutputFileDelimitedEntity}
import hydrograph.engine.core.component.entity.elements.SchemaField
import hydrograph.engine.spark.components.base.SparkFlow
import hydrograph.engine.spark.components.platform.BaseComponentParams
import org.apache.spark.sql.functions._
import org.apache.spark.sql.{Column, SaveMode}
import scala.collection.JavaConversions._
import scala.collection.JavaConverters._
/**
  * Created by gurdits on 10/15/2016.
  */
class SparkOFileFixedWidthComponent(outputFileFixedWidthEntity: OutputFileFixedWidthEntity, cp:
BaseComponentParams) extends SparkFlow {


  def createSchema(fields:util.List[SchemaField]): Array[Column] ={
    val schema=new Array[Column](fields.size())
    fields.zipWithIndex.foreach{ case(f,i)=> schema(i)=col(f.getFieldName)}
    schema
  }

  override def execute() = {

    val fieldsLen=new Array[Int](outputFileFixedWidthEntity.getFieldsList.size())

    outputFileFixedWidthEntity.getFieldsList().asScala.zipWithIndex.foreach{ case(s,i)=>
      fieldsLen(i)=s.getFieldLength
    }

    cp.getDataFrame().select(createSchema(outputFileFixedWidthEntity.getFieldsList):_*).write
      .option("charset", outputFileFixedWidthEntity.getCharset)
      .option("length",fieldsLen.mkString(","))
      .mode(SaveMode.Overwrite)
      .format("hydrograph.engine.spark.fixedwidth.datasource")
      .save(outputFileFixedWidthEntity.getPath)

  }
}
