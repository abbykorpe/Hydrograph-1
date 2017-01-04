package hydrograph.engine.spark.components


import hydrograph.engine.core.component.entity.UniqueSequenceEntity
import hydrograph.engine.spark.components.base.OperationComponentBase
import hydrograph.engine.spark.components.platform.BaseComponentParams
import hydrograph.engine.spark.components.handler.OperationHelper
import hydrograph.engine.spark.components.utils.{EncoderHelper,OperationSchemaCreator}
import hydrograph.engine.transformation.userfunctions.base.TransformBase
import org.apache.spark.sql.{Column, DataFrame, Row}
import org.apache.spark.sql.catalyst.expressions.{LeafExpression, Nondeterministic}
import org.apache.spark.sql.functions._
import org.apache.spark.sql.types.{StructField, _}
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

