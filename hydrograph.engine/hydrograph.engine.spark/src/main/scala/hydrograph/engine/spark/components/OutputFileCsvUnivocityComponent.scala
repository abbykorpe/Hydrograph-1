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
package hydrograph.engine.spark.components

import hydrograph.engine.core.component.entity.OutputFileDelimitedEntity
import hydrograph.engine.core.custom.exceptions._
import hydrograph.engine.spark.components.base.SparkFlow
import hydrograph.engine.spark.components.platform.BaseComponentParams
import hydrograph.engine.spark.components.utils.{SchemaCreator, SchemaMisMatchException, SchemaUtils}
import org.apache.hadoop.fs.Path
import org.apache.spark.sql.{AnalysisException, SaveMode}
import org.slf4j.{Logger, LoggerFactory}

import scala.collection.JavaConverters._

/**
  * The Class OutputFileCsvWithDateFormatsComponent.
  *
  * @author Bitwise
  *
  */
class OutputFileCsvUnivocityComponent(outputFileDelimitedEntity: OutputFileDelimitedEntity, cp:
BaseComponentParams) extends SparkFlow with Serializable {
  private val LOG:Logger = LoggerFactory.getLogger(classOf[OutputFileCsvUnivocityComponent])

  override def execute() = {
    LOG.trace("In method execute()")
    val schemaCreator = SchemaCreator(outputFileDelimitedEntity)

   try {
     cp.getDataFrame().select(schemaCreator.createSchema(): _*).write
       .option("delimiter", outputFileDelimitedEntity.getDelimiter)
       .option("quote", outputFileDelimitedEntity.getQuote)
       .option("header", outputFileDelimitedEntity.getHasHeader)
       .option("charset", outputFileDelimitedEntity.getCharset)
       .option("strict", outputFileDelimitedEntity.isStrict)
       .option("safe", outputFileDelimitedEntity.getSafe)
       .option("dateFormats", schemaCreator.getDateFormats)
       .option("codec", SchemaUtils().getCodec(outputFileDelimitedEntity))
       .mode(if (outputFileDelimitedEntity.isOverWrite ) SaveMode.Overwrite else SaveMode.ErrorIfExists)
       .format("hydrograph.engine.spark.datasource.csvwithunivocity.CSVFileFormat")
       .save(checkPath(outputFileDelimitedEntity.getPath))
   } catch {
     case e: AnalysisException =>
       LOG.error("\nException in Output File Delimited Component - \nComponent Id:[\"" + outputFileDelimitedEntity.getComponentId + "\"]" +
         "\nComponent Name:[\"" + outputFileDelimitedEntity.getComponentName + "\"]\nBatch:[\"" + outputFileDelimitedEntity.getBatch + "\"]" + e.getMessage,e)
       throw new SchemaMisMatchException("\nException in Output File Delimited Component - \nComponent Id:[\"" + outputFileDelimitedEntity.getComponentId + "\"]" +
       "\nComponent Name:[\"" + outputFileDelimitedEntity.getComponentName + "\"]\nBatch:[\"" + outputFileDelimitedEntity.getBatch + "\"]\nError being: " + e.message,e)
     case e: DateFormatException =>
       LOG.error("\nException in Output File Delimited Component - \nComponent Id:[\"" + outputFileDelimitedEntity.getComponentId + "\"]" +
         "\nComponent Name:[\"" + outputFileDelimitedEntity.getComponentName + "\"]\nBatch:[\"" + outputFileDelimitedEntity.getBatch + "\"]" + e.getMessage,e)
       throw new DateFormatException("\nException in Output File Delimited Component - \nComponent Id:[\"" + outputFileDelimitedEntity.getComponentId + "\"]" +
       "\nComponent Name:[\"" + outputFileDelimitedEntity.getComponentName + "\"]\nBatch:[\"" + outputFileDelimitedEntity.getBatch + "\"]" + e.getMessage,e)
     case e: PathNotFoundException =>
       LOG.error("\nException in Output File Delimited Component - \nComponent Id:[\"" + outputFileDelimitedEntity.getComponentId + "\"]" +
         "\nComponent Name:[\"" + outputFileDelimitedEntity.getComponentName + "\"]\nBatch:[\"" + outputFileDelimitedEntity.getBatch + "\"]" + e.getMessage,e)
       throw new PathNotFoundException("\nException in Output File Delimited Component - \nComponent Id:[\"" + outputFileDelimitedEntity.getComponentId + "\"]" +
       "\nComponent Name:[\"" + outputFileDelimitedEntity.getComponentName + "\"]\nBatch:[\"" + outputFileDelimitedEntity.getBatch + "\"]" + e.getMessage,e)
     case e: BadDelimiterFoundException =>
       LOG.error("\nException in Output File Delimited Component - \nComponent Id:[\"" + outputFileDelimitedEntity.getComponentId + "\"]" +
         "\nComponent Name:[\"" + outputFileDelimitedEntity.getComponentName + "\"]\nBatch:[\"" + outputFileDelimitedEntity.getBatch + "\"]" + e.getMessage,e)
       throw new BadDelimiterFoundException("\nException in Output File Delimited Component - \nComponent Id:[\"" + outputFileDelimitedEntity.getComponentId + "\"]" +
       "\nComponent Name:[\"" + outputFileDelimitedEntity.getComponentName + "\"]\nBatch:[\"" + outputFileDelimitedEntity.getBatch + "\"]" + e.getMessage,e)
     case e: BadQuoteFoundException =>
       LOG.error("\nException in Output File Delimited Component - \nComponent Id:[\"" + outputFileDelimitedEntity.getComponentId + "\"]" +
         "\nComponent Name:[\"" + outputFileDelimitedEntity.getComponentName + "\"]\nBatch:[\"" + outputFileDelimitedEntity.getBatch + "\"]" + e.getMessage,e)
       throw new BadQuoteFoundException("\nException in Output File Delimited Component - \nComponent Id:[\"" + outputFileDelimitedEntity.getComponentId + "\"]" +
       "\nComponent Name:[\"" + outputFileDelimitedEntity.getComponentName + "\"]\nBatch:[\"" + outputFileDelimitedEntity.getBatch + "\"]" + e.getMessage,e)
     case e: BadArgumentException =>
       LOG.error("\nException in Output File Delimited Component - \nComponent Id:[\"" + outputFileDelimitedEntity.getComponentId + "\"]" +
         "\nComponent Name:[\"" + outputFileDelimitedEntity.getComponentName + "\"]\nBatch:[\"" + outputFileDelimitedEntity.getBatch + "\"]" + e.getMessage,e)
       throw new BadArgumentException("\nException in Output File Delimited Component - \nComponent Id:[\"" + outputFileDelimitedEntity.getComponentId + "\"]" +
       "\nComponent Name:[\"" + outputFileDelimitedEntity.getComponentName + "\"]\nBatch:[\"" + outputFileDelimitedEntity.getBatch + "\"]" + e.getMessage,e)

     case e: NumberFormatException =>
       LOG.error("\nException in Output File Delimited Component - \nComponent Id:[\"" + outputFileDelimitedEntity.getComponentId + "\"]" +
         "\nComponent Name:[\"" + outputFileDelimitedEntity.getComponentName + "\"]\nBatch:[\"" + outputFileDelimitedEntity.getBatch + "\"]" + e.getMessage,e)
       throw new NumberFormatException("\nException in Output File Delimited Component - \nComponent Id:[\"" + outputFileDelimitedEntity.getComponentId + "\"]" +
         "\nComponent Name:[\"" + outputFileDelimitedEntity.getComponentName + "\"]\nBatch:[\"" + outputFileDelimitedEntity.getBatch + "\"]" + e.getMessage)

     case e: Exception =>
       LOG.error("\nException in Output File Delimited Component - \nComponent Id:[\"" + outputFileDelimitedEntity.getComponentId + "\"]" +
       "\nComponent Name:[\"" + outputFileDelimitedEntity.getComponentName + "\"]\nBatch:[\"" + outputFileDelimitedEntity.getBatch + "\"]" + e.getMessage,e)
       throw new RuntimeException("\nException in Output File Delimited Component - \nComponent Id:[\"" + outputFileDelimitedEntity.getComponentId + "\"]" +
       "\nComponent Name:[\"" + outputFileDelimitedEntity.getComponentName + "\"]\nBatch:[\"" + outputFileDelimitedEntity.getBatch + "\"]" + e.getMessage,e)
   }
    LOG.info("Created Output File Delimited Component "+ outputFileDelimitedEntity.getComponentId
      + " in Batch "+ outputFileDelimitedEntity.getBatch +" with path " + outputFileDelimitedEntity.getPath)
    LOG.debug("Component Id: '"+ outputFileDelimitedEntity.getComponentId
      +"' in Batch: " + outputFileDelimitedEntity.getBatch
      + " having schema: [ " + outputFileDelimitedEntity.getFieldsList.asScala.mkString(",")
      + " ] with delimiter: " + outputFileDelimitedEntity.getDelimiter + " and quote: " + outputFileDelimitedEntity.getQuote
      + " strict as " + outputFileDelimitedEntity.isStrict + " safe as " + outputFileDelimitedEntity.getSafe
      + " at Path: " + outputFileDelimitedEntity.getPath)
  }

  def checkPath(path:String):String={

    if (path == null || path.equals("")){

      throw new PathNotFoundException("\nPath:[\"" + path + "\"]\nError being: " + "path option must be specified for Output File Delimited Component")
    }

    path
  }

}
