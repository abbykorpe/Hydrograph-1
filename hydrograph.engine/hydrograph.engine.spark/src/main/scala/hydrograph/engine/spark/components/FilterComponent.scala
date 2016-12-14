package hydrograph.engine.spark.components

import java.util

import hydrograph.engine.core.component.entity.FilterEntity
import hydrograph.engine.spark.components.base.OperationComponentBase
import hydrograph.engine.spark.components.handler.ClassStringHandler
import hydrograph.engine.spark.components.platform.BaseComponentParams
import hydrograph.engine.spark.components.utils._
import hydrograph.engine.transformation.userfunctions.base.FilterBase
import org.apache.spark.sql.DataFrame
import org.slf4j.LoggerFactory

import scala.collection.JavaConverters._

/**
  * Created by vaijnathp on 12/12/2016.
  */
class FilterComponent(filterEntity: FilterEntity, componentsParams: BaseComponentParams) extends
  OperationComponentBase with Serializable with ClassStringHandler{
  val LOG = LoggerFactory.getLogger(classOf[FilterComponent])
  override def createComponent(): Map[String, DataFrame] = {
    val scheme = componentsParams.getDataFrame.schema.map(e => e.name)
    LOG.info("Filter Component Called with input Dataframe having Schema:", componentsParams.getDataFrame.schema)

    val fieldNameSet = new util.LinkedHashSet[String]()
    scheme.foreach(e => fieldNameSet.add(e))
    var map: Map[String, DataFrame] = Map()

    val filterClass=classLoader[FilterBase](filterEntity.getOperation.getOperationClass)
    val fieldPosition=ReusableRowHelper(filterEntity.getOperation, null).determineInputFieldPositionsForFilter(scheme)
    val inputReusableRow=new SparkReusableRow(fieldNameSet)


   filterEntity.getOutSocketList.asScala.foreach{outSocket=>
     LOG.info("Creating filter assembly for '" + filterEntity.getComponentId + "' for socket: '"
       + outSocket.getSocketId + "' of type: '" + outSocket.getSocketType + "'")

     val df = componentsParams.getDataFrame.filter(
       row =>{
         if(outSocket.getSocketType.equalsIgnoreCase("unused"))
           !filterClass
             .isRemove(RowHelper.convertToReusebleRow(fieldPosition, row, inputReusableRow))

         else
           filterClass
             .isRemove(RowHelper.convertToReusebleRow(fieldPosition, row, inputReusableRow))

       })
    map += (outSocket.getSocketId -> df)
  }
    map
  }
}
