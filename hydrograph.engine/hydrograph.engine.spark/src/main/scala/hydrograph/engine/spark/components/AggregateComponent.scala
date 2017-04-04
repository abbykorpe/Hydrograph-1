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
import hydrograph.engine.core.component.entity.elements.KeyField
import hydrograph.engine.core.component.utils.OperationUtils
import hydrograph.engine.core.custom.exceptions.{FieldNotFoundException, UserFunctionClassNotFoundException}
import hydrograph.engine.expression.userfunctions.AggregateForExpression
import hydrograph.engine.expression.utils.ExpressionWrapper
import hydrograph.engine.spark.components.base.OperationComponentBase
import hydrograph.engine.spark.components.handler.{OperationHelper, SparkOperation}
import hydrograph.engine.spark.components.platform.BaseComponentParams
import hydrograph.engine.spark.components.utils._
import hydrograph.engine.transformation.userfunctions.base.AggregateTransformBase
import org.apache.spark.sql.catalyst.encoders.RowEncoder
import org.apache.spark.sql.functions._
import org.apache.spark.sql.types.StructType
import org.apache.spark.sql.{Column, DataFrame, Row}
import org.slf4j.{Logger, LoggerFactory}

import scala.collection.JavaConverters._

/**
  * The Class AggregateComponent.
  *
  * @author Bitwise
  *
  */
class AggregateComponent(aggregateEntity: AggregateEntity, componentsParams: BaseComponentParams) extends
  OperationComponentBase with OperationHelper[AggregateTransformBase] with Serializable {

  val outSocketEntity = aggregateEntity.getOutSocketList.get(0)
  val inputSchema: StructType = componentsParams.getDataFrame().schema
  val outputFields = OperationUtils.getAllFields(aggregateEntity.getOutSocketList, inputSchema.map(_.name).asJava).asScala
    .toList
  val fieldsForOPeration = OperationUtils.getAllFieldsWithOperationFields(aggregateEntity, outputFields.toList.asJava)
  val outputSchema: StructType = EncoderHelper().getEncoder(outputFields, componentsParams.getSchemaFields())
  try {
    operationSchema = EncoderHelper().getEncoder(fieldsForOPeration.asScala.toList, componentsParams.getSchemaFields())
  } catch {
    case e: Exception =>
      LOG.error("\nException in Aggregate Component - \nComponent Id:[\"" + aggregateEntity.getComponentId + "\"]" +
        "\nComponent Name:[\"" + aggregateEntity.getComponentName + "\"]\nBatch:[\"" + aggregateEntity.getBatch + "\"]" + e.getMessage(),e)
      throw new SchemaMisMatchException("\nException in Aggregate Component - \nComponent Id:[\"" + aggregateEntity.getComponentId + "\"]" +
      "\nComponent Name:[\"" + aggregateEntity.getComponentName + "\"]\nBatch:[\"" + aggregateEntity.getBatch + "\"]" + e.getMessage(),e)
  }
  val inSocketId: String = aggregateEntity.getInSocketList.get(0).getInSocketId
  val mapFields = outSocketEntity.getMapFieldsList.asScala.toList
  val passthroughFields: Array[String] = OperationUtils.getPassThrougFields(outSocketEntity.getPassThroughFieldsList,
    inputSchema
      .map
      (_.name).asJava).asScala.toArray[String]
  val mapFieldIndexes = getIndexes(inputSchema, outputSchema, getMapSourceFields(mapFields, inSocketId), getMapTargetFields(mapFields, inSocketId))
  val passthroughIndexes = getIndexes(inputSchema, outputSchema, passthroughFields)
  val keyFields = if (aggregateEntity.getKeyFields == null) Array[String]() else aggregateEntity.getKeyFields.map(_
    .getName)
  private val LOG: Logger = LoggerFactory.getLogger(classOf[AggregateComponent])
  var operationSchema: StructType = null
  try {
    keyFieldsIndexes = getIndexes(inputSchema, keyFields)
  }
  catch {
    case e: Exception =>
      LOG.error("\nException in Aggregate Component - \nComponent Id:[\"" + aggregateEntity.getComponentId + "\"]" +
        "\nComponent Name:[\"" + aggregateEntity.getComponentName + "\"]\nBatch:[\"" + aggregateEntity.getBatch + "\"]" + e.getMessage(),e)
      throw new SchemaMisMatchException("\nException in Aggregate Component - \nComponent Id:[\"" + aggregateEntity.getComponentId + "\"]" +
      "\nComponent Name:[\"" + aggregateEntity.getComponentName + "\"]\nBatch:[\"" + aggregateEntity.getBatch + "\"]" + e.getMessage(),e)
  }
  var keyFieldsIndexes: Array[(Int, Int)] = null

  override def createComponent(): Map[String, DataFrame] = {

    LOG.trace("In method createComponent()")
    //    val op = OperationSchemaCreator[AggregateEntity](aggregateEntity, componentsParams, aggregateEntity.getOutSocketList().get(0))
    //    val fm = FieldManupulating(op.getOperationInputFields(), op.getOperationOutputFields(), op.getPassThroughFields(), op.getMapFields(), op.getOperationFields(), aggregateEntity.getKeyFields)


    val primaryKeys = if (aggregateEntity.getKeyFields == null) Array[KeyField]() else aggregateEntity.getKeyFields
    val secondaryKeys = if (aggregateEntity.getSecondaryKeyFields == null) Array[KeyField]() else aggregateEntity.getSecondaryKeyFields
    LOG.debug("Component Id: '" + aggregateEntity.getComponentId
      + "' Primary Keys: " + primaryKeys
      + " Secondary Keys: " + secondaryKeys)

    val sourceDf = componentsParams.getDataFrame()

    val repartitionedDf = if (primaryKeys.isEmpty) sourceDf.repartition(1) else sourceDf.repartition(primaryKeys.map { field => col(field.getName) }: _*)

    val sortedDf = repartitionedDf.sortWithinPartitions(populateSortKeys(primaryKeys ++ secondaryKeys): _*)

    val intermediateDf = sortedDf.mapPartitions(itr => {

      def compare(row: Row, previousRow: Row): Boolean = {
        keyFieldsIndexes.forall(i => row(i._1).equals(previousRow(i._1)))
      }

      var aggregateList: List[SparkOperation[AggregateTransformBase]] = null
      try {
        aggregateList = initializeOperationList[AggregateForExpression](aggregateEntity
          .getOperationsList, inputSchema, operationSchema)
      }
      catch {
        case e: UserFunctionClassNotFoundException =>
          LOG.error("\nException in Aggregate Component - \nComponent Id:[\"" + aggregateEntity.getComponentId + "\"]" +
            "\nComponent Name:[\"" + aggregateEntity.getComponentName + "\"]\nBatch:[\"" + aggregateEntity.getBatch + "\"]" + e.getMessage(),e)
          throw new UserFunctionClassNotFoundException(
          "\nException in Aggregate Component - \nComponent Id:[\"" + aggregateEntity.getComponentId + "\"]" +
            "\nComponent Name:[\"" + aggregateEntity.getComponentName + "\"]\nBatch:[\"" + aggregateEntity.getBatch + "\"]" + e.getMessage(),e)

        case e: FieldNotFoundException =>
          LOG.error("\nException in Aggregate Component - \nComponent Id:[\"" + aggregateEntity.getComponentId + "\"]" +
            "\nComponent Name:[\"" + aggregateEntity.getComponentName + "\"]\nBatch:[\"" + aggregateEntity.getBatch + "\"]" + e.getMessage(),e)
          throw new FieldNotFoundException(
          "\nException in Aggregate Component - \nComponent Id:[\"" + aggregateEntity.getComponentId + "\"]" +
            "\nComponent Name:[\"" + aggregateEntity.getComponentName + "\"]\nBatch:[\"" + aggregateEntity.getBatch + "\"]" + e.getMessage(),e)
      }

      aggregateList.foreach(sparkOperation => {
        sparkOperation.baseClassInstance match {
          case a: AggregateForExpression => {
            try {
              a.setValidationAPI(new ExpressionWrapper(sparkOperation.validatioinAPI, sparkOperation.initalValue))
              a.init()
              a.callPrepare(sparkOperation.fieldName, sparkOperation.fieldType)
            } catch {
              case e: Exception =>
                LOG.error("\nException in Aggregate Component - \nComponent Id:[\"" + aggregateEntity.getComponentId + "\"]" +
                  "\nComponent Name:[\"" + aggregateEntity.getComponentName + "\"]\nBatch:[\"" + aggregateEntity.getBatch + "\"]" + e.getMessage(),e)
                throw new RuntimeException("\nException in Aggregate Component - \nComponent Id:[\""
                + aggregateEntity.getComponentId + "\"]" + "\nComponent Name:[\"" + aggregateEntity.getComponentName
                + "\"]\nBatch:[\"" + aggregateEntity.getBatch + "\"]\nError being: " + e.getMessage(), e)
            }
          }
          case a: AggregateTransformBase => {
            try {
              a.prepare(sparkOperation.operationEntity.getOperationProperties, sparkOperation
                .operationEntity.getOperationInputFields, sparkOperation.operationEntity.getOperationOutputFields, keyFields)
            } catch {
              case e: Exception =>

                LOG.error("\nException in Aggregate Component - \nComponent Id:[\"" + aggregateEntity.getComponentId + "\"]" +
                  "\nComponent Name:[\"" + aggregateEntity.getComponentName + "\"]\nBatch:[\"" + aggregateEntity.getBatch + "\"]" + e.getMessage(),e)

                throw new RuntimeException("\nException in Aggregate Component - \nComponent Id:[\""
                + aggregateEntity.getComponentId + "\"]" + "\nComponent Name:[\"" + aggregateEntity.getComponentName
                + "\"]\nBatch:[\"" + aggregateEntity.getBatch + "\"]\nError being: " + e.getMessage(), e)
            }
          }
        }
      })
      var outRow: Array[Any] = null
      var previousRow: Row = null
      var tempOutRow: Array[Any] = null
      val outSize = operationSchema.size

      new Iterator[Row] {

        var isContinue: Boolean = true
        var isItrEmpty: Boolean = itr.isEmpty

        override def hasNext: Boolean = {
          isContinue && !isItrEmpty
        }

        override def next(): Row = {
          val isEndOfIterator: Boolean = itr.isEmpty
          val isPrevKeyNull: Boolean = previousRow == null

          if (isEndOfIterator) {
            aggregateList.foreach(agt => {
              try {
                agt.baseClassInstance.onCompleteGroup(agt.outputRow.setRow(tempOutRow))
              } catch {
                case e: Exception =>
                  LOG.error("\nException in Aggregate Component - \nComponent Id:[\"" + aggregateEntity.getComponentId + "\"]" +
                    "\nComponent Name:[\"" + aggregateEntity.getComponentName + "\"]\nBatch:[\"" + aggregateEntity.getBatch + "\"]" + e.getMessage(),e)

                  throw new RuntimeException("\nException in Aggregate Component - \nComponent Id:[\""
                  + aggregateEntity.getComponentId + "\"]" + "\nComponent Name:[\"" + aggregateEntity.getComponentName
                  + "\"]\nBatch:[\"" + aggregateEntity.getBatch + "\"]\nError being: " + e.getMessage(), e)
              }
            })
            isContinue = false;
            isItrEmpty = true;
            return Row.fromSeq(tempOutRow)
          }
          val row: Row = itr.next()
          val isPrevKeyDifferent: Boolean = {
            if (previousRow == null)
              true
            else !compare(row, previousRow)
          }
          previousRow = row

          if (isPrevKeyDifferent) {
            if (!isPrevKeyNull) {
              aggregateList.foreach(agt => {
                try {
                  agt.baseClassInstance.onCompleteGroup(agt.outputRow.setRow(tempOutRow))
                } catch {
                  case e: Exception =>
                    LOG.error("\nException in Aggregate Component - \nComponent Id:[\"" + aggregateEntity.getComponentId + "\"]" +
                      "\nComponent Name:[\"" + aggregateEntity.getComponentName + "\"]\nBatch:[\"" + aggregateEntity.getBatch + "\"]" + e.getMessage(),e)

                    throw new RuntimeException("\nException in Aggregate Component - \nComponent Id:[\""
                    + aggregateEntity.getComponentId + "\"]" + "\nComponent Name:[\"" + aggregateEntity.getComponentName
                    + "\"]\nBatch:[\"" + aggregateEntity.getBatch + "\"]\nError being: " + e.getMessage(), e)
                }
              })
            }
            outRow = tempOutRow
            tempOutRow = new Array[Any](outSize)
            copyFields(row, tempOutRow, mapFieldIndexes)
            copyFields(row, tempOutRow, passthroughIndexes)
          }

          aggregateList.foreach(agt => {
            try {
              agt.baseClassInstance.aggregate(agt.inputRow.setRow(row))
            } catch {
              case e: Exception =>
                LOG.error("\nException in Aggregate Component - \nComponent Id:[\"" + aggregateEntity.getComponentId + "\"]" +
                  "\nComponent Name:[\"" + aggregateEntity.getComponentName + "\"]\nBatch:[\"" + aggregateEntity.getBatch + "\"]" + e.getMessage(),e)
                throw new RuntimeException("\nException in Aggregate Component - \nComponent Id:[\""
                + aggregateEntity.getComponentId + "\"]" + "\nComponent Name:[\"" + aggregateEntity.getComponentName
                + "\"]\nBatch:[\"" + aggregateEntity.getBatch + "\"]\nError being: " + e.getMessage(), e)
            }
          })

          if (isPrevKeyDifferent && (!isPrevKeyNull))
            return Row.fromSeq(outRow)
          else
            next()
        }
      }

    })(RowEncoder(operationSchema))

    val key = aggregateEntity.getOutSocketList.get(0).getSocketId
    Map(key -> intermediateDf)

  }

  def populateSortKeys(keysArray: Array[KeyField]): Array[Column] = {
    LOG.trace("In method populateSortKeys()")
    keysArray.map { field => if (field.getSortOrder.toLowerCase() == "desc") col(field.getName).desc else col(field.getName) }
  }

}
