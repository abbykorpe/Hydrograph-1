package hydrograph.engine.spark.components

import hydrograph.engine.core.component.entity.FilterEntity
import hydrograph.engine.core.component.utils.OperationUtils
import hydrograph.engine.expression.userfunctions.FilterForExpression
import hydrograph.engine.spark.components.base.OperationComponentBase
import hydrograph.engine.spark.components.handler.OperationHelper
import hydrograph.engine.spark.components.platform.BaseComponentParams
import hydrograph.engine.spark.components.utils._
import hydrograph.engine.transformation.userfunctions.base.FilterBase
import org.apache.spark.sql.DataFrame
import org.apache.spark.sql.types.StructType
import org.slf4j.LoggerFactory

import scala.collection.JavaConverters._

/**
  * Created by vaijnathp on 12/12/2016.
  */
class FilterComponent(filterEntity: FilterEntity, componentsParams: BaseComponentParams) extends
  OperationComponentBase with OperationHelper[FilterBase] with Serializable {
  val LOG = LoggerFactory.getLogger(classOf[FilterComponent])

  override def createComponent(): Map[String, DataFrame] = {

    LOG.info("Filter Component Called with input Schema [in the form of(Column_name,DataType,IsNullable)]: {}", componentsParams.getDataFrame().schema)
    val inputSchema: StructType = componentsParams.getDataFrame().schema
    val outputFields = OperationUtils.getAllFields(filterEntity.getOutSocketList, inputSchema.map(_.name).asJava).asScala.toList
    val outputSchema: StructType = EncoderHelper().getEncoder(outputFields, componentsParams.getSchemaFields())

    var map: Map[String, DataFrame] = Map()

      val filterClass = initializeOperationList[FilterForExpression](filterEntity.getOperationsList,
        inputSchema, outputSchema).head

      filterClass.baseClassInstance match {
        case expression: FilterForExpression => expression.setValidationAPI(filterClass.validatioinAPI)
        case _ =>
      }
    filterEntity.getOutSocketList.asScala.foreach { outSocket =>

      LOG.info("Creating filter Component for '" + filterEntity.getComponentId + "' for socket: '"
        + outSocket.getSocketId + "' of type: '" + outSocket.getSocketType + "'")
      if (outSocket.getSocketType.equalsIgnoreCase("out")) {
        val outDF = componentsParams.getDataFrame().filter(row => {
          !filterClass.baseClassInstance.isRemove(filterClass.inputRow.setRow(row))
        })
        map += (outSocket.getSocketId -> outDF)
      }
      else {
        val unusedDF = componentsParams.getDataFrame().filter(row => {
          filterClass.baseClassInstance.isRemove(filterClass.inputRow.setRow(row))
        })
        map += (outSocket.getSocketId -> unusedDF)
      }
    }
    map
  }
}