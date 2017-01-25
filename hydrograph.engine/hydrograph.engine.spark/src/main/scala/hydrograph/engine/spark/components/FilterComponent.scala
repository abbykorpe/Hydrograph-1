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

    if (filterEntity.getOperation.getOperationInputFields == null) {
      LOG.error("Filter Operation Input Fields Can not be Empty")
      throw new Exception("Filter Operation Input Fields Can not be Empty")
    }

    LOG.info("Filter Component Called with input Schema [in the form of(Column_name,DataType,IsNullable)]: {}", componentsParams.getDataFrame.schema)
    val inputSchema: StructType = componentsParams.getDataFrame.schema
    val outputFields = OperationUtils.getAllFields(filterEntity.getOutSocketList, inputSchema.map(_.name).asJava).asScala.toList
    val outputSchema: StructType = EncoderHelper().getEncoder(outputFields, componentsParams.getSchemaFields())

//    val fieldNameSet = new util.LinkedHashSet[String]()
//    filterEntity.getOperation.getOperationInputFields.foreach(e => fieldNameSet.add(e))

    var map: Map[String, DataFrame] = Map()

 /*   LOG.info("Filter Operation Input Fields: {}", filterEntity.getOperation.getOperationInputFields)
    val filterClass: FilterBase = filterEntity.getOperation match {
      case x if x.getOperationClass == null => {
        val filter = new FilterForExpression()
        filter.setValidationAPI(new ValidationAPI(x.getExpression, ""))
        filter
      }
      case y => classLoader[FilterBase](y.getOperationClass)
    }

    val fieldPosition = ReusableRowHelper(filterEntity.getOperation, null).determineInputFieldPositionsForFilter(scheme)

    val inputReusableRow = new SparkReusableRow(fieldNameSet)
*/

    filterEntity.getOutSocketList.asScala.foreach { outSocket =>
      LOG.info("Creating filter Component for '" + filterEntity.getComponentId + "' for socket: '"
        + outSocket.getSocketId + "' of type: '" + outSocket.getSocketType + "'")

      val isOutSocketTypeOut =outSocket.getSocketType.equalsIgnoreCase("out")

      /*//Filter component with MapPartition Function for calling filter's prepare method
      val scheme = componentsParams.getDataFrame.schema.map(e => e.name)
      val df = componentsParams.getDataFrame.mapPartitions(itr => {
        val filterClass = initializeOperationList[FilterForExpression](filterEntity.getOperationsList,
          inputSchema, outputSchema).head
        filterClass.baseClassInstance match {
          case t: FilterForExpression => t.setValidationAPI(filterClass.validatioinAPI)
          case t: FilterBase => t.prepare(filterEntity.getOperation.getOperationProperties, null)
        }
        itr.filter(row => {
          if (itr.isEmpty)
            filterClass.baseClassInstance.cleanup()

          if (isOutSocketTypeOut)
            !filterClass.baseClassInstance.isRemove(filterClass.inputRow.setRow(row))
          else
            filterClass.baseClassInstance.isRemove(filterClass.inputRow.setRow(row))
        })
      })(RowEncoder(EncoderHelper().getEncoder(scheme.toList, componentsParams.getSchemaFields())))*/

      val filterClass = initializeOperationList[FilterForExpression](filterEntity.getOperationsList,
        inputSchema, outputSchema).head
      filterClass.baseClassInstance match {
        case t: FilterForExpression => t.setValidationAPI(filterClass.validatioinAPI)
        case t: FilterBase => ;
        //t.prepare(filterEntity.getOperation.getOperationProperties, null)
      }
      val df = componentsParams.getDataFrame.filter(
        row =>{
          if(isOutSocketTypeOut)
            !filterClass.baseClassInstance.isRemove(filterClass.inputRow.setRow(row))
          else
            filterClass.baseClassInstance.isRemove(filterClass.inputRow.setRow(row))
        })


      /*val df= componentsParams.getDataFrame.mapPartitions( itr =>{

        filterClass.prepare(filterEntity.getOperation.getOperationProperties,null)
        val rs= itr.filter( row =>{

          if(itr.isEmpty)
            filterClass.cleanup()

          if(isOutSocketTypeOut)
            !filterClass
              .isRemove(RowHelper.convertToReusebleRow(fieldPosition, row,inputReusableRow))
          else
            filterClass
              .isRemove(RowHelper.convertToReusebleRow(fieldPosition, row, inputReusableRow))
        })
        rs
      })(RowEncoder(EncoderHelper().getEncoder(scheme.toList, componentsParams.getSchemaFields())))
      */


      /* val df = componentsParams.getDataFrame.filter(
         row =>{
           if(isOutSocketTypeOut)
             !filterClass
               .isRemove(RowHelper.convertToReusebleRow(fieldPosition, row,inputReusableRow))
           else
             filterClass
               .isRemove(RowHelper.convertToReusebleRow(fieldPosition, row, inputReusableRow))
         })*/
      map += (outSocket.getSocketId -> df)
    }

    map
  }
}