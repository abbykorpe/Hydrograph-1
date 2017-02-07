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
import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.types._
import org.apache.spark.sql.types.StructField
import org.apache.spark.sql.types.StructType
import hydrograph.engine.spark.components.base.SparkFlow
import hydrograph.engine.core.component.entity.OutputFileAvroEntity
import hydrograph.engine.spark.components.platform.BaseComponentParams
import org.slf4j.LoggerFactory
import org.slf4j.Logger
import org.apache.spark.sql.{ Column, SaveMode }
import org.slf4j.{ Logger, LoggerFactory }
import scala.collection.JavaConversions._
import java.util
import org.apache.spark.sql.functions._
import hydrograph.engine.core.component.entity.elements.SchemaField
import hydrograph.engine.core.constants.Constants
import hydrograph.engine.spark.datasource.avro.CustomSparkToAvro

class OutputFileAvroComponent(outputFileAvroEntity: OutputFileAvroEntity, baseComponentParams: BaseComponentParams) extends SparkFlow{
  private val LOG: Logger = LoggerFactory.getLogger(classOf[OutputFileAvroComponent])
  
   private def createSchema(fields: util.List[SchemaField]): Array[Column] = {
    CustomSparkToAvro.setInputFields(fields.size())
    val schema = new Array[Column](fields.size())
    fields.zipWithIndex.foreach {
      case (f, i) =>
        schema(i) = col(f.getFieldName)
          setPrecisonScale(f)
        }
    LOG.debug("Schema created for Output File Avro Component : " + schema.mkString)
    schema
  }

  override def execute() = {
    val filePathToWrite = outputFileAvroEntity.getPath()
    try {
      val df = baseComponentParams.getDataFrame()
      df.select(createSchema(outputFileAvroEntity.getFieldsList): _*).write.mode( if (outputFileAvroEntity.isOverWrite) SaveMode.Overwrite else SaveMode.Append ).format("hydrograph.engine.spark.datasource.avro").save(filePathToWrite)
      LOG.debug("Created Output File Avro Component '" + outputFileAvroEntity.getComponentId + "' in Batch" + outputFileAvroEntity.getBatch
        + ", file path " + outputFileAvroEntity.getPath)
    } catch {
      case e: Exception =>
        LOG.error("Error in Output File Avro Component " + outputFileAvroEntity.getComponentId, e)
        throw new RuntimeException("Error in Output File Avro Component "
          + outputFileAvroEntity.getComponentId, e)
    }
  }
  private def setPrecisonScale(field: SchemaField) = {
    CustomSparkToAvro.setPrecison(field.getFieldPrecision)
    CustomSparkToAvro.setScale(field.getFieldScale)
  }
}