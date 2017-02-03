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


import hydrograph.engine.core.component.entity.UniqueSequenceEntity
import hydrograph.engine.spark.components.base.OperationComponentBase
import hydrograph.engine.spark.components.handler.OperationHelper
import hydrograph.engine.spark.components.platform.BaseComponentParams
import hydrograph.engine.spark.components.utils.{EncoderHelper, OperationSchemaCreator}
import hydrograph.engine.transformation.userfunctions.base.TransformBase
import org.apache.spark.sql.functions._
import org.apache.spark.sql.{Column, DataFrame, Row}
import org.slf4j.LoggerFactory

/**
  * Created by sandeepv on 12/28/2016.
  */
class UniqueSequenceComponent(uniqueSequenceEntity: UniqueSequenceEntity, baseComponentParams: BaseComponentParams) extends OperationComponentBase with OperationHelper[TransformBase] with Serializable {

  val LOG = LoggerFactory.getLogger(classOf[UniqueSequenceComponent])

  /**
    * These method create spark component for generate unique sequence
    *
    * @return Map[String, DataFrame]
    */
  override def createComponent(): Map[String, DataFrame] = {

    val op = OperationSchemaCreator[UniqueSequenceEntity](uniqueSequenceEntity, baseComponentParams, uniqueSequenceEntity.getOutSocketList().get(0))

    LOG.info("UniqueSequence Component Called with input Dataframe having Schema in the form of(Column_name,DataType,IsNullable): {}",
      baseComponentParams.getDataFrame.schema)

    val inputColumn = new Array[Column](op.getPassThroughFields().size)

    op.getPassThroughFields().zipWithIndex.foreach(
      f => {
        inputColumn(f._2) = column(f._1)
      })

    val rdd = baseComponentParams.getDataFrame().select(inputColumn: _*).
      rdd.zipWithIndex().map(indexedRow => {
      Row.fromSeq(indexedRow._1.toSeq :+ indexedRow._2.asInstanceOf[Long])
    })

    val df = baseComponentParams
    .getSparkSession()
    .sqlContext
    .createDataFrame(rdd, new EncoderHelper().getStructFields(baseComponentParams.getSchemaFields()))
     val key = uniqueSequenceEntity.getOutSocketList.get(0).getSocketId
     Map(key -> df)
  }
}

