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

import hydrograph.engine.core.component.entity.AggregateEntity
import hydrograph.engine.core.component.utils.OperationUtils
import hydrograph.engine.expression.userfunctions.AggregateUDAFForExpression
import hydrograph.engine.expression.utils.ExpressionWrapper
import hydrograph.engine.spark.components.base.OperationComponentBase
import hydrograph.engine.spark.components.handler.{OperationHelper, SparkOperation}
import hydrograph.engine.spark.components.platform.BaseComponentParams
import hydrograph.engine.spark.components.utils._
import hydrograph.engine.spark.operation.handler.AggregateCustomHandler
import hydrograph.engine.transformation.userfunctions.base.AggregatorTransformBase
import org.apache.spark.sql.functions._
import org.apache.spark.sql.types.StructType
import org.slf4j.{Logger, LoggerFactory}
import org.apache.spark.sql.{DataFrame, _}

import scala.collection.JavaConverters._

/**
  * Created by bitwise on 10/24/2016.
  */
class AggregateComponentNew(aggregateEntity: AggregateEntity, componentsParams: BaseComponentParams) extends
  OperationComponentBase with OperationHelper[AggregatorTransformBase] with Serializable {

  val outSocketEntity = aggregateEntity.getOutSocketList.get(0)
  val inputSchema: StructType = componentsParams.getDataFrame().schema
  val outputFields = OperationUtils.getAllFields(aggregateEntity.getOutSocketList, inputSchema.map(_.name).asJava).asScala
    .toList
  val fieldsForOPeration = OperationUtils.getAllFieldsWithOperationFields(aggregateEntity, outputFields.toList.asJava)
  val operationSchema: StructType = EncoderHelper().getEncoder(fieldsForOPeration.asScala.toList, componentsParams.getSchemaFields())
  val outputSchema: StructType = EncoderHelper().getEncoder(outputFields, componentsParams.getSchemaFields())
  val inSocketId: String = aggregateEntity.getInSocketList.get(0).getInSocketId
  val mapFields = outSocketEntity.getMapFieldsList.asScala.toList
  val passthroughFields: Array[String] = OperationUtils.getPassThrougFields(outSocketEntity.getPassThroughFieldsList,
    inputSchema
      .map
      (_.name).asJava).asScala.toArray[String]

  val keyFields = if (aggregateEntity.getKeyFields == null) Array[String]() else aggregateEntity.getKeyFields.map(_
    .getName)

  val key = aggregateEntity.getOutSocketList.get(0).getSocketId
  val compID = aggregateEntity.getComponentId
  private val LOG: Logger = LoggerFactory.getLogger(classOf[AggregateComponent])

  override def createComponent(): Map[String, DataFrame] = {

    LOG.trace("In method createComponent()")

    val sourceDf = componentsParams.getDataFrame()

    //Initialize Aggregarte to call prepare Method
    val aggregateList: List[SparkOperation[AggregatorTransformBase]] = initializeOperationList[AggregateUDAFForExpression](aggregateEntity
      .getOperationsList,
      inputSchema,
      operationSchema)

    //init for expressions
//    aggregateList.foreach(sparkOperation => {
//      sparkOperation.baseClassInstance match {
//        //For Expression Editor call extra methods
//        case a: AggregateUDAFForExpression =>
//          a.setValidationAPI(new ExpressionWrapper(sparkOperation.validatioinAPI, sparkOperation.initalValue))
//          a.init()
//          a.callPrepare(sparkOperation.fieldName, sparkOperation.fieldType)
//      }
//    })

    val aggUdafList: List[Column] = aggregateList.map(aggOperation => {
      val inputSchema = EncoderHelper().getEncoder(aggOperation.operationEntity.getOperationInputFields.toList, componentsParams.getSchemaFieldList()(0).asScala.toArray)
      val outputSchema = EncoderHelper().getEncoder(aggOperation.operationEntity.getOperationFields)
      val cols = aggOperation.operationEntity.getOperationInputFields.toList.map(e => col(e))

      (new AggregateCustomHandler(aggOperation.baseClassInstance, inputSchema, outputSchema, true).apply(cols: _*)).as(compID + aggOperation.operationEntity.getOperationId + "_agg")
    }
    )

    val passthroughList = passthroughFields.map(field => first(field).as(field)).toList
    val mapList = mapFields.map(field => first(field.getSourceName).as(field.getName)).toList

    val finalList = aggUdafList ++ passthroughList ++ mapList

    val groupedDF = sourceDf.groupBy(keyFields.map(col(_)):_* )
    var aggregatedDf = groupedDF.agg(finalList.head, finalList.tail: _*)

    outSocketEntity.getOperationFieldList.asScala.foreach(operationField => {
      aggregatedDf = aggregatedDf.withColumn(operationField.getName, col(compID + operationField.getOperationId + "_agg." + operationField.getName))
    })
    aggregateEntity.getOperationsList.asScala.foreach(operation => {
      aggregatedDf = aggregatedDf.drop(col(compID + operation.getOperationId + "_agg"))
    })

    Map(key -> aggregatedDf)

  }


}