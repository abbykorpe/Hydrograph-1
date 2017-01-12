package hydrograph.engine.spark.components

import java.util

import hydrograph.engine.core.component.entity.PartitionByExpressionEntity
import hydrograph.engine.core.component.entity.elements.OutSocket
import hydrograph.engine.core.component.utils.OperationUtils
import hydrograph.engine.expression.userfunctions.PartitionByExpressionForExpression
import hydrograph.engine.spark.components.base.OperationComponentBase
import hydrograph.engine.spark.components.handler.OperationHelper
import hydrograph.engine.spark.components.platform.BaseComponentParams
import hydrograph.engine.spark.components.utils.{EncoderHelper, ReusableRowHelper, RowHelper, SparkReusableRow}
import hydrograph.engine.transformation.userfunctions.base.CustomPartitionExpression
import org.apache.spark.sql.DataFrame
import org.apache.spark.sql.catalyst.encoders.RowEncoder
import org.apache.spark.sql.types.StructType
import org.slf4j.{Logger, LoggerFactory}

import scala.collection.JavaConverters._

/**
  * Created by santlalg on 12/15/2016.
  */
class PartitionByExpressionComponent(partitionByExpressionEntity: PartitionByExpressionEntity, componentsParams: BaseComponentParams) extends
  OperationComponentBase with  OperationHelper[CustomPartitionExpression] with Serializable {

  private val LOG: Logger = LoggerFactory.getLogger(classOf[PartitionByExpressionComponent])

  override def createComponent(): Map[String, DataFrame] = {
    LOG.trace("In method createComponent()")

    val scheme = componentsParams.getDataFrame.schema.map(e => e.name)
    val inputSchema: StructType = componentsParams.getDataFrame.schema
    val outputFields = OperationUtils.getAllFields(partitionByExpressionEntity.getOutSocketList, inputSchema.map(_.name).asJava).asScala
      .toList
    val outputSchema: StructType = EncoderHelper().getEncoder(outputFields, componentsParams.getSchemaFields())

    LOG.info("Created PartitionByExpression Component '" + partitionByExpressionEntity.getComponentId
      + "' in Batch " + partitionByExpressionEntity.getBatch
      + " with output socket [" + getOutSocketId(partitionByExpressionEntity.getOutSocketList.asScala.toList).mkString(",")
      + "] operation [operationClass : " + partitionByExpressionEntity.getOperationsList.get(0).getOperationClass
      + ", operationInputField : " + partitionByExpressionEntity.getOperationsList.get(0).getOperationInputFields.toList.mkString(",")
      + "]")

    LOG.debug("Component id '" + partitionByExpressionEntity.getComponentId
      + "' in Batch " + partitionByExpressionEntity.getBatch
      + " with output socket [" + getOutSocketId(partitionByExpressionEntity.getOutSocketList.asScala.toList).mkString(",")
      + "] operation [operationClass : " + partitionByExpressionEntity.getOperationsList.get(0).getOperationClass
      + ", operationInputField : " + partitionByExpressionEntity.getOperationsList.get(0).getOperationInputFields.toList.mkString(",")
      + "] ")

    val fieldNameSet = new util.LinkedHashSet[String]()
    partitionByExpressionEntity.getOperation.getOperationInputFields.foreach(e => fieldNameSet.add(e))

    var map: Map[String, DataFrame] = Map()

    LOG.trace("Component id '" + partitionByExpressionEntity.getComponentId
      + "' scheme " + fieldNameSet.asScala.toList.mkString(","))

    try {
      val fieldPosition = ReusableRowHelper(partitionByExpressionEntity.getOperation, null).determineInputFieldPositionsForFilter(scheme)
      val inputReusableRow = new SparkReusableRow(fieldNameSet)

      LOG.trace("Operation input Field " + partitionByExpressionEntity.getOperationsList.get(0).getOperationInputFields.toList.mkString(",")
        + " having field position [" + fieldPosition.mkString(",") + "]")

      partitionByExpressionEntity.getOutSocketList.asScala.foreach { outSocket =>

        val partitionByExpressionClassList =  initializeOperationList[PartitionByExpressionForExpression](partitionByExpressionEntity.getOperationsList,
          inputSchema, outputSchema)
        partitionByExpressionClassList.foreach {
          sparkOperation =>
            sparkOperation.baseClassInstance match {
              //For Expression Editor call extra method setValidationAPI
              case t: PartitionByExpressionForExpression => t.setValidationAPI(sparkOperation.validatioinAPI)
              case t: CustomPartitionExpression => t.prepare(partitionByExpressionEntity.getOperation.getOperationProperties)
            }
        }
        val partitionByExpressionClass= partitionByExpressionClassList.head

        val df= componentsParams.getDataFrame.mapPartitions( itr =>{
          partitionByExpressionClass.baseClassInstance.prepare(partitionByExpressionEntity.getOperation.getOperationProperties)
          val rs= itr.filter( row =>{
            partitionByExpressionClass.baseClassInstance.getPartition(RowHelper.convertToReusebleRow(fieldPosition, row, inputReusableRow),
              partitionByExpressionEntity.getNumPartitions.toInt).equals(outSocket.getSocketId)
          })
          rs
        })(RowEncoder(EncoderHelper().getEncoder(scheme.toList, componentsParams.getSchemaFields())))
        map += (outSocket.getSocketId -> df)
      }
      map
    } catch {
      case e: Exception =>
        LOG.error("Error in PartitionByExpression component '" + partitionByExpressionEntity.getComponentId + "' error : " + e.getMessage, e)
        throw new RuntimeException("Error in PartitionByExpression Component '" + partitionByExpressionEntity.getComponentId + "'", e)
    }
  }

  def getOutSocketId(toList: List[OutSocket]): List[String] = {
    var outSocketId: List[String] = Nil
    toList.foreach(e => outSocketId = e.getSocketId :: outSocketId)
    outSocketId
  }

}
