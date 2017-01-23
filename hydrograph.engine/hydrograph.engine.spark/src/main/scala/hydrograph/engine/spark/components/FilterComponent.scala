package hydrograph.engine.spark.components

import java.util

import hydrograph.engine.core.component.entity.FilterEntity
import hydrograph.engine.expression.api.ValidationAPI
import hydrograph.engine.expression.userfunctions.FilterForExpression
import hydrograph.engine.spark.components.base.OperationComponentBase
import hydrograph.engine.spark.components.platform.BaseComponentParams
import hydrograph.engine.spark.components.utils._
import hydrograph.engine.transformation.userfunctions.base.FilterBase
import org.apache.spark.sql.DataFrame
import org.apache.spark.sql.catalyst.encoders.RowEncoder
import org.slf4j.LoggerFactory

import scala.collection.JavaConverters._

/**
  * Created by vaijnathp on 12/12/2016.
  */
class FilterComponent(filterEntity: FilterEntity, componentsParams: BaseComponentParams) extends
  OperationComponentBase with Serializable {
  val LOG = LoggerFactory.getLogger(classOf[FilterComponent])
  override def createComponent(): Map[String, DataFrame] = {

    if (filterEntity.getOperation.getOperationInputFields==null) {
      LOG.error("Filter Operation Input Fields Can not be Empty")
      throw new Exception("Filter Operation Input Fields Can not be Empty")
    }

    val scheme = componentsParams.getDataFrame.schema.map(e => e.name)
    LOG.info("Filter Component Called with input Dataframe having Schema in the form of(Column_name,DataType,IsNullable): {}", componentsParams.getDataFrame.schema)

    val fieldNameSet = new util.LinkedHashSet[String]()
    filterEntity.getOperation.getOperationInputFields.foreach(e => fieldNameSet.add(e))
    var map: Map[String, DataFrame] = Map()

    LOG.info("Filter Operation Input Fields: {}",filterEntity.getOperation.getOperationInputFields)
    val filterClass: FilterBase = filterEntity.getOperation match {
      case x if x.getOperationClass == null => {
        val filter = new FilterForExpression()
        filter.setValidationAPI(new ValidationAPI(x.getExpression,""))
        filter
      }
      case y => classLoader[FilterBase](y.getOperationClass)
    }

    val fieldPosition=ReusableRowHelper(filterEntity.getOperation, null).determineInputFieldPositionsForFilter(scheme)

    val inputReusableRow = new SparkReusableRow(fieldNameSet)

    filterEntity.getOutSocketList.asScala.foreach{outSocket=>
      LOG.info("Creating filter Component for '" + filterEntity.getComponentId + "' for socket: '"
        + outSocket.getSocketId + "' of type: '" + outSocket.getSocketType + "'")


      val df= componentsParams.getDataFrame.mapPartitions( itr =>{

        filterClass.prepare(filterEntity.getOperation.getOperationProperties,null)
        val rs= itr.filter( row =>{

          if(itr.isEmpty)
            filterClass.cleanup()

          if(outSocket.getSocketType.equalsIgnoreCase("out"))
            !filterClass
              .isRemove(RowHelper.convertToReusebleRow(fieldPosition, row,inputReusableRow))
          else
            filterClass
              .isRemove(RowHelper.convertToReusebleRow(fieldPosition, row, inputReusableRow))
        })
        rs
      })(RowEncoder(EncoderHelper().getEncoder(scheme.toList, componentsParams.getSchemaFields())))

     /* val df = componentsParams.getDataFrame.filter(
        row =>{
          if(outSocket.getSocketType.equalsIgnoreCase("unused"))
            filterClass
              .isRemove(RowHelper.convertToReusebleRow(fieldPosition, row,inputReusableRow))
          else
            !filterClass
              .isRemove(RowHelper.convertToReusebleRow(fieldPosition, row, inputReusableRow))
        })*/
      map += (outSocket.getSocketId -> df)
    }

    map
  }

  def classLoader[T](className: String): T = {
    val clazz = Class.forName(className).getDeclaredConstructors
    clazz(0).setAccessible(true)
    clazz(0).newInstance().asInstanceOf[T]
  }
}