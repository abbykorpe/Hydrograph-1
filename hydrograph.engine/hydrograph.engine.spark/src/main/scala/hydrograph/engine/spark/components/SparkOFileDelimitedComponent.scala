package hydrograph.engine.spark.components

import java.util


import hydrograph.engine.core.component.entity.OutputFileDelimitedEntity
import hydrograph.engine.core.component.entity.elements.SchemaField
import hydrograph.engine.spark.components.base.SparkFlow
import hydrograph.engine.spark.components.platform.BaseComponentParams
import org.apache.spark.sql.functions._
import org.apache.spark.sql.{Column, SaveMode}

import scala.collection.JavaConversions._
/**
  * Created by gurdits on 10/15/2016.
  */
class SparkOFileDelimitedComponent(outputFileDelimitedEntity: OutputFileDelimitedEntity,cp:
BaseComponentParams) extends SparkFlow {


  def createSchema(fields:util.List[SchemaField]): Array[Column] ={
    val schema=new Array[Column](fields.size())
    fields.zipWithIndex.foreach{ case(f,i)=> schema(i)=col(f.getFieldName)}
    schema
  }

  override def execute() = {


    cp.getDataFrame().select(createSchema(outputFileDelimitedEntity.getFieldsList):_*).write
      .option("delimiter", outputFileDelimitedEntity.getDelimiter)
      .option("quote", outputFileDelimitedEntity.getQuote)
      .option("header", outputFileDelimitedEntity.getHasHeader)
      .option("charset", outputFileDelimitedEntity.getCharset)
      .mode(SaveMode.Overwrite)
      .csv(outputFileDelimitedEntity.getPath)

  }
}
