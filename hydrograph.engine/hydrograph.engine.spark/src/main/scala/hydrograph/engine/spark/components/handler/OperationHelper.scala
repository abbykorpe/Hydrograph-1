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
package hydrograph.engine.spark.components.handler

import java.util.ArrayList

import hydrograph.engine.core.component.entity.elements.{MapField, Operation, PassThroughField}
import hydrograph.engine.expression.api.ValidationAPI
import hydrograph.engine.spark.core.reusablerow.{InputReusableRow, OutputReusableRow, RowToReusableMapper}
import hydrograph.engine.transformation.userfunctions.base.ReusableRow
import org.apache.spark.sql.Row
import org.apache.spark.sql.types.StructType
import scala.collection.JavaConverters.asScalaBufferConverter
import scala.reflect.ClassTag

case class SparkOperation[T](baseClassInstance: T, operationEntity: Operation, inputRow: InputReusableRow, outputRow:
OutputReusableRow, validatioinAPI: ValidationAPI, initalValue: String,operationOutFields:Array[String])

trait OperationHelper[T] {

  def initializeOperationList[U](operationList: java.util.List[Operation], inputSchema: StructType,
                                 outputSchema:StructType)(implicit ct: ClassTag[U]): List[SparkOperation[T]] = {

    def populateOperation(operationList: List[Operation]): List[SparkOperation[T]] =
      (operationList) match {
        case (List()) => List()
        case (x :: xs) if x.isExpressionPresent => {
          val tf = classLoader[T](ct.runtimeClass.getCanonicalName)
          SparkOperation[T](tf, x, getInputReusableRow(inputSchema, x), getOutputReusableRow(outputSchema, x), new ValidationAPI(x.getExpression, ""), x.getAccumulatorInitialValue,x.getOperationOutputFields) ::
            populateOperation(xs)
        }
        case (x :: xs) => {
          val tf = classLoader[T](x.getOperationClass)
          SparkOperation[T](tf, x, getInputReusableRow(inputSchema, x), getOutputReusableRow(outputSchema, x), null, null,null) ::
            populateOperation(xs)
        }
      }

    if (operationList != null) {
      populateOperation(operationList.asScala.toList)
    }
    else
      List()
  }

  def getInputReusableRow[U](inputSchema: StructType, x: Operation): InputReusableRow = {
    if (x.getOperationInputFields != null) InputReusableRow(null, new RowToReusableMapper(inputSchema, x.getOperationInputFields))
    else InputReusableRow(null, new RowToReusableMapper(inputSchema, Array[String]()))
  }

  def getOutputReusableRow[U](outputSchema: StructType, x: Operation): OutputReusableRow = {
    if (x.getOperationOutputFields != null) OutputReusableRow(null, new RowToReusableMapper(outputSchema, x.getOperationOutputFields))
    else null
  }

  def classLoader[T](className: String): T = {
    val clazz = Class.forName(className).getDeclaredConstructors
    clazz(0).setAccessible(true)
    clazz(0).newInstance().asInstanceOf[T]
  }

  implicit def arrayToList(arr: Array[String]): ArrayList[String] = {
    val lst = new ArrayList[String]()
if(arr != null)    arr.foreach(v => lst.add(v))
    lst
  }

  def getMapSourceFields(mapfields: List[MapField], inSocketId: String): Array[String] =
    mapfields.filter { x => x.getInSocketId.equals(inSocketId) }.map { x => x.getSourceName }.toArray[String]

  def getMapTargetFields(mapfields: List[MapField], inSocketId: String): Array[String] =
    mapfields.filter { x => x.getInSocketId.equals(inSocketId) }.map { x => x.getName }.toArray[String]

  def getPassthroughSourceFields(passthroughfields: List[PassThroughField], inSocketId: String): Array[String] =
    passthroughfields.filter { x => x.getInSocketId.equals(inSocketId) }.map { x => x.getName }.toArray[String]

  def getIndexes(firstSchema: StructType, secondSchema: StructType, fields: Array[String]): Array[(Int, Int)] =
    fields.map { field => (firstSchema.fieldIndex(field), secondSchema.fieldIndex(field)) }

  def getIndexes(firstSchema: StructType, fields: Array[String]): Array[(Int, Int)] =
    fields.zipWithIndex.map { field => (firstSchema.fieldIndex(field._1),field._2 ) }

  def getIndexes(firstSchema: StructType, secondSchema: StructType, firstFields: Array[String], secondFields: Array[String]): Array[(Int, Int)] =
    firstFields.zip(secondFields).map { pair => (firstSchema.fieldIndex(pair._1), secondSchema.fieldIndex(pair._2)) }

  def copyFields(input: ReusableRow, output: ReusableRow): Unit = {
    for (index <- 0 until input.getFieldNames.size()) {
      output.setField(index, input.getField(index))
    }
  }

  def copyFields(input: Row, output: Array[Any], indexes: Array[(Int, Int)]): Unit = {
    indexes.foreach(pair => output(pair._2) = input(pair._1))
  }


}
