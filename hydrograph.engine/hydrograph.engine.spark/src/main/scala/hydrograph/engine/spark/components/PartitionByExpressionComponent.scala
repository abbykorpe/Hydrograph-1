/** *****************************************************************************
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
  * ******************************************************************************/
package hydrograph.engine.spark.components

import hydrograph.engine.core.component.entity.PartitionByExpressionEntity
import hydrograph.engine.core.custom.exceptions.{FieldNotFoundException, UserFunctionClassNotFoundException}
import hydrograph.engine.expression.userfunctions.PartitionByExpressionForExpression
import hydrograph.engine.spark.components.base.OperationComponentBase
import hydrograph.engine.spark.components.handler.OperationHelper
import hydrograph.engine.spark.components.platform.BaseComponentParams
import hydrograph.engine.spark.components.utils.SchemaMisMatchException
import hydrograph.engine.transformation.userfunctions.base.CustomPartitionExpression
import org.apache.spark.sql.types.StructType
import org.apache.spark.sql.{DataFrame, Row}
import org.slf4j.{Logger, LoggerFactory}

import scala.collection.JavaConverters._

/**
  * The Class PartitionByExpressionComponent.
  *
  * @author Bitwise
  *
  */
class PartitionByExpressionComponent(partitionByExpressionEntity: PartitionByExpressionEntity, componentsParams: BaseComponentParams) extends
  OperationComponentBase with OperationHelper[CustomPartitionExpression] with Serializable {

  private val LOG: Logger = LoggerFactory.getLogger(classOf[PartitionByExpressionComponent])

  override def createComponent(): Map[String, DataFrame] = {
    LOG.trace("In method createComponent()")

    val inputSchema: StructType = componentsParams.getDataFrame.schema
    val outputSchema: StructType = inputSchema

    val numberOfPartitions = partitionByExpressionEntity.getNumPartitions.toInt

    LOG.info("Created PartitionByExpression Component '" + partitionByExpressionEntity.getComponentId
      + "' in Batch " + partitionByExpressionEntity.getBatch
      + " with output socket [" + partitionByExpressionEntity.getOutSocketList.asScala.map(e => e.getSocketId).mkString(",")
      + "] operation [operationClass : " + partitionByExpressionEntity.getOperationsList.get(0).getOperationClass
      + ", operationInputField : " + partitionByExpressionEntity.getOperationsList.get(0).getOperationInputFields.toList.mkString(",")
      + "]")

    var map: Map[String, DataFrame] = Map()
    try {

      val partitionByExpressionSparkOperation = initializeOperationList[PartitionByExpressionForExpression](partitionByExpressionEntity.getOperationsList,
        inputSchema, outputSchema).head

      val opProps = partitionByExpressionSparkOperation.operationEntity.getOperationProperties

      LOG.info("Operation Properties: " + opProps)
      if(opProps!=null) CustomPartitionExpression.properties.putAll(opProps)

      val partitionByExpressionClass = partitionByExpressionSparkOperation.baseClassInstance
      partitionByExpressionClass match {
        case t: PartitionByExpressionForExpression => t.setValidationAPI(partitionByExpressionSparkOperation.validatioinAPI)
          t.callPrepare(partitionByExpressionSparkOperation.fieldName,partitionByExpressionSparkOperation.fieldType)
        case _ =>
      }

      partitionByExpressionEntity.getOutSocketList.asScala.foreach { outSocket =>
        val socketId = outSocket.getSocketId
        val isDataForCurrentOutSocket = (row: Row) => partitionByExpressionClass.getPartition(partitionByExpressionSparkOperation.inputRow.setRow(row),
          numberOfPartitions).equals(socketId)
        val df = componentsParams.getDataFrame.filter(isDataForCurrentOutSocket)
        map += (socketId -> df)
      }

      map
    } catch {
      case e: UserFunctionClassNotFoundException => throw new UserFunctionClassNotFoundException("\nException in Partition By Expression Component - "
        + "\nComponent Id:[\"" + partitionByExpressionEntity.getComponentId + "\"]" + "\nComponent Name:[\""
        + partitionByExpressionEntity.getComponentName + "\"]\nBatch:[\"" + partitionByExpressionEntity.getBatch + "\"]" + e.getMessage(), e)
      case e: FieldNotFoundException => throw new FieldNotFoundException("\nException in Partition By Expression Component - "
        + "\nComponent Id:[\"" + partitionByExpressionEntity.getComponentId + "\"]" + "\nComponent Name:[\""
        + partitionByExpressionEntity.getComponentName + "\"]\nBatch:[\"" + partitionByExpressionEntity.getBatch + "\"]" + e.getMessage(), e)
      case e: Exception => throw new RuntimeException("\nException in Partition By Expression Component - "
        + "\nComponent Id:[\"" + partitionByExpressionEntity.getComponentId + "\"]" + "\nComponent Name:[\""
        + partitionByExpressionEntity.getComponentName + "\"]\nBatch:[\"" + partitionByExpressionEntity.getBatch + "\"]" + e.getMessage(), e)
    }
  }
}
