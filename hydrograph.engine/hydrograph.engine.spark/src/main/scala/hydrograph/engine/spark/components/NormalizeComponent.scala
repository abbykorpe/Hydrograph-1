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

import java.util

import hydrograph.engine.core.component.entity.NormalizeEntity
import hydrograph.engine.core.component.entity.elements.Operation
import hydrograph.engine.core.component.utils.OperationUtils
import hydrograph.engine.expression.userfunctions.NormalizeForExpression
import hydrograph.engine.expression.utils.ExpressionWrapper
import hydrograph.engine.spark.components.base.OperationComponentBase
import hydrograph.engine.spark.components.handler.{OperationHelper, SparkOperation}
import hydrograph.engine.spark.components.platform.BaseComponentParams
import hydrograph.engine.spark.components.utils.EncoderHelper
import hydrograph.engine.transformation.userfunctions.base.{NormalizeTransformBase, OutputDispatcher}
import org.apache.spark.sql.{DataFrame, Row}
import org.apache.spark.sql.catalyst.encoders.RowEncoder
import org.apache.spark.sql.types.StructType
import org.slf4j.{Logger, LoggerFactory}

import scala.collection.JavaConversions.seqAsJavaList
import scala.collection.JavaConverters.{asScalaBufferConverter, seqAsJavaListConverter}
import scala.collection.mutable.ListBuffer

/**
  * Created by bitwise on 10/18/2016.
  */
class NormalizeComponent(normalizeEntity: NormalizeEntity, componentsParams: BaseComponentParams) 
 extends OperationComponentBase with Serializable with OperationHelper[NormalizeTransformBase] {

  private val LOG:Logger = LoggerFactory.getLogger(classOf[NormalizeComponent])

  val outSocketEntity = normalizeEntity.getOutSocketList.get(0)
  val inputSchema: StructType = componentsParams.getDataFrame().schema
  val outputFields = OperationUtils.getAllFields(normalizeEntity.getOutSocketList, inputSchema.map(_.name).asJava).asScala
    .toList
  val fieldsForOperation = OperationUtils.getAllFieldsWithOperationFields(normalizeEntity, outputFields.toList.asJava)
  val operationSchema: StructType = EncoderHelper().getEncoder(fieldsForOperation.asScala.toList, componentsParams.getSchemaFields())
  val outputSchema: StructType = EncoderHelper().getEncoder(outputFields, componentsParams.getSchemaFields())
  val inSocketId: String = normalizeEntity.getInSocketList.get(0).getInSocketId
  val mapFields = outSocketEntity.getMapFieldsList.asScala.toList
  val passthroughFields: Array[String] = OperationUtils.getPassThrougFields(outSocketEntity.getPassThroughFieldsList,
    inputSchema
      .map
      (_.name).asJava).asScala.toArray[String]
  val mapFieldIndexes = getIndexes(inputSchema, outputSchema, getMapSourceFields(mapFields, inSocketId), getMapTargetFields(mapFields, inSocketId))
  val passthroughIndexes = getIndexes(inputSchema, outputSchema, passthroughFields)

  private def getAllInputFieldsForExpr(getOperationsList: util.List[Operation], list: List[String]): List[String] = {
    LOG.trace("In method getAllInputFieldsForExpr()")
    val list1 = getOperationsList.asScala.toList.flatMap(e => e.getOperationInputFields)
    list1
  }

  private def getAllOutputFieldsForExpr(getOperationsList: util.List[Operation], list: List[String]): List[String] = {
    LOG.trace("In method getAllOutputFieldsForExpr()")
    val list1 = getOperationsList.asScala.toList.flatMap(e => e.getOperationOutputFields)
    list1
  }

  private def unique[A](ls: List[A]) = {
    LOG.trace("In method unique()")
    def loop(set: Set[A], ls: List[A]): List[A] = ls match {
      case hd :: tail if set contains hd => loop(set, tail)
      case hd :: tail => hd :: loop(set + hd, tail)
      case Nil => Nil
    }
    loop(Set(), ls)
  }
  
  private def extractAllInputPositions(inputFields: List[String]): List[Int] = Seq(0 to (inputFields.length - 1)).toList.flatten

  private def extractAllOutputPositions(outputFields: List[String]): List[Int] = Seq(0 to (outputFields.length - 1)).toList.flatten

  override def createComponent(): Map[String, DataFrame] = {
    LOG.trace("In method createComponent()")

    val outRow = new Array[Any](outputFields.size)
    var outputDispatcher: NormalizeOutputCollector = null

    val df = componentsParams.getDataFrame.mapPartitions(itr => {

      val normalizeList: List[SparkOperation[NormalizeTransformBase]] = initializeOperationList[NormalizeForExpression](normalizeEntity.getOperationsList, inputSchema, outputSchema)

      normalizeList.foreach {
        sparkOperation =>
          sparkOperation.baseClassInstance match {
            //For Expression Editor call extra method setValidationAPI
            case n: NormalizeForExpression => n.setValidationAPI(sparkOperation.validatioinAPI)
              n.callPrepare(sparkOperation.fieldName,sparkOperation.fieldType)
            case n: NormalizeTransformBase=> n.prepare(sparkOperation.operationEntity.getOperationProperties)
          }
      }

      val it = itr.flatMap(row => {

        copyFields(row, outRow, mapFieldIndexes)
        copyFields(row, outRow, passthroughIndexes)

        outputDispatcher = new NormalizeOutputCollector(outRow)

        normalizeList.foreach { nr =>
          //Calling Transform Method
          try{
            nr.baseClassInstance.Normalize(nr.inputRow.setRow(row), nr.outputRow.setRow(outRow),outputDispatcher)
          } catch {
            case e:Exception => throw new RuntimeException("Error in Normalize Component:[\""+normalizeEntity.getComponentId+"\"] for "+e.getMessage)
          }

          if (itr.isEmpty) {
            LOG.info("Calling cleanup() method of " + nr.baseClassInstance.getClass.toString + " class.")
            nr.baseClassInstance.cleanup()
          }
        }
        outputDispatcher.getOutRows
      })
      it
    })(RowEncoder(outputSchema))

    val key = normalizeEntity.getOutSocketList.get(0).getSocketId
      Map(key -> df)
  }
}

class NormalizeOutputCollector(outRow: Array[Any]) extends OutputDispatcher {

  private val list = new ListBuffer[Row]()
  private val LOG:Logger = LoggerFactory.getLogger(classOf[NormalizeOutputCollector])

  override def sendOutput(): Unit = {
    LOG.trace("In method sendOutput()")
    val clonedRow = outRow.clone()
    list += Row.fromSeq(clonedRow)
  }

  def initialize: Unit = {
    LOG.trace("In method initialize()")
    list.clear()
  }

  def getOutRows: ListBuffer[Row] = {
    LOG.trace("In method getOutRows()")
    list
  }
}
