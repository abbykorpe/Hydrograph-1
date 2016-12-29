package hydrograph.engine.spark.components


import hydrograph.engine.core.component.entity.UniqueSequenceEntity
import hydrograph.engine.spark.components.base.OperationComponentBase
import hydrograph.engine.spark.components.platform.BaseComponentParams
import org.apache.spark.sql.{DataFrame, Row}
import org.apache.spark.sql.catalyst.expressions.{LeafExpression, Nondeterministic}
import org.apache.spark.sql.catalyst.expressions.codegen.{CodegenContext, ExprCode}
import org.apache.spark.sql.types.{StructField, _}
import org.slf4j.LoggerFactory

import scala.math.BigInt.long2bigInt
/**
  * Created by sandeepv on 12/28/2016.
  */
class UniqueSequenceComponent(uniqueSequenceEntity: UniqueSequenceEntity, baseComponentParams: BaseComponentParams)extends OperationComponentBase with Serializable {

  val LOG = LoggerFactory.getLogger(classOf[UniqueSequenceComponent])

  /**
    * These method create spark component
    *
    * @return Map[String, DataFrame]
    */
  override def createComponent(): Map[String, DataFrame] = {

    val schema = baseComponentParams.getDataFrame.schema
    LOG.info("UniqueSequence Component Called with input Dataframe having Schema in the form of(Column_name,DataType,IsNullable): {}",
      baseComponentParams.getDataFrame.schema)

    val rdd = baseComponentParams.getDataFrame()
      .rdd.zipWithIndex().map(indexedRow =>
      Row.fromSeq(indexedRow._2.asInstanceOf[Long] +: indexedRow._1.toSeq))

    val newstructtype = StructType(
      StructField("seq2", LongType, true) ::
      StructField("f1", StringType, true) ::
      StructField("f2", IntegerType, true)::
      StructField("f3", DoubleType, true)::
      StructField("f4", FloatType, true)::
      StructField("f5", ShortType, true)::
      StructField("f6", BooleanType, true)::
      StructField("f7", LongType, true)::
      Nil)
    val df= baseComponentParams.getSparkSession().sqlContext.createDataFrame(rdd,newstructtype)
    val key = uniqueSequenceEntity.getOutSocketList.get(0).getSocketId
    Map(key -> df)
  }
}