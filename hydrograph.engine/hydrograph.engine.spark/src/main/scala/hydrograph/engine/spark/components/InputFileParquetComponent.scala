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

import hydrograph.engine.core.component.entity.InputFileParquetEntity
import hydrograph.engine.core.custom.exceptions._
import hydrograph.engine.spark.components.base.InputComponentBase
import hydrograph.engine.spark.components.platform.BaseComponentParams
import hydrograph.engine.spark.components.utils.{SchemaCreator, SchemaUtils}
import org.apache.spark.sql.{AnalysisException, DataFrame}
import org.slf4j.{Logger, LoggerFactory}

import scala.collection.JavaConverters._
/**
  * The Class InputFileParquetComponent.
  *
  * @author Bitwise
  *
  */
class InputFileParquetComponent(iFileParquetEntity: InputFileParquetEntity, iComponentsParams: BaseComponentParams)
  extends InputComponentBase {
  private val LOG: Logger = LoggerFactory.getLogger(classOf[InputFileParquetComponent])

  override def finalize(): Unit = super.finalize()

  override def createComponent(): Map[String, DataFrame] = {
    val schemaField = SchemaCreator(iFileParquetEntity).makeSchema()
    try {

      val path: String = iFileParquetEntity.getPath

      val fieldList = iFileParquetEntity.getFieldsList.asScala
      fieldList.foreach { field => LOG.debug("Field name '" + field.getFieldName + "for Component " + iFileParquetEntity.getComponentId) }

      val df = try {
        iComponentsParams.getSparkSession().read.parquet(path)
      } catch {

        case ex: AnalysisException =>

          LOG.error("Exception in  in Input  File Parquet component - \nComponent Id:[\"" + iFileParquetEntity.getComponentId + "\"]" +
            "\nComponent Name:[\"" + iFileParquetEntity.getComponentName + "\"]\nBatch:[\"" + iFileParquetEntity.getBatch + "\"]\n" + "Error being : " + ex.getMessage(), ex)

          throw new PathNotFoundException(
            "\nException in  in Input  File Parquet component - \nComponent Id:[\"" + iFileParquetEntity.getComponentId + "\"]" +
              "\nComponent Name:[\"" + iFileParquetEntity.getComponentName + "\"]\nBatch:[\"" + iFileParquetEntity.getBatch + "\"]\n" + "Error being : " + ex.getMessage(), ex)
      }
      try {
        SchemaUtils().compareSchema(schemaField.toList, df.schema.toList)
      } catch {
        case ex: SchemaMismatchException =>

          LOG.error("Exception in  in Input  File Parquet component - \nComponent Id:[\"" + iFileParquetEntity.getComponentId + "\"]" +
            "\nComponent Name:[\"" + iFileParquetEntity.getComponentName + "\"]\nBatch:[\"" + iFileParquetEntity.getBatch + "\"]\n" + "Error being : " + ex.getMessage(), ex)

          throw new SchemaMismatchException(
            "\nException in Input  File Parquet component - \nComponent Id:[\"" + iFileParquetEntity.getComponentId + "\"]" +
              "\nComponent Name:[\"" + iFileParquetEntity.getComponentName + "\"]\nBatch:[\"" + iFileParquetEntity.getBatch + "\"]\n" + "Error being : " + ex.getMessage(), ex)
      }

      val key = iFileParquetEntity.getOutSocketList.get(0).getSocketId

      LOG.debug("Created Input File Parquet Component '" + iFileParquetEntity.getComponentId + "' in Batch" + iFileParquetEntity.getBatch
        + ", file path " + iFileParquetEntity.getPath)

      Map(key -> df)
    }
    catch {

      case ex: RuntimeException =>
        LOG.error("Exception in  in Input  File Parquet component - \nComponent Id:[\"" + iFileParquetEntity.getComponentId + "\"]" +
          "\nComponent Name:[\"" + iFileParquetEntity.getComponentName + "\"]\nBatch:[\"" + iFileParquetEntity.getBatch + "\"]\n" + "Error being : " + ex.getMessage(), ex)

        throw new RuntimeException(
          "\nException in Input  File Parquet component - \nComponent Id:[\"" + iFileParquetEntity.getComponentId + "\"]" +
            "\nComponent Name:[\"" + iFileParquetEntity.getComponentName + "\"]\nBatch:[\"" + iFileParquetEntity.getBatch + "\"]\n" + "Error being : " + ex.getMessage(), ex)
    }
  }
}
