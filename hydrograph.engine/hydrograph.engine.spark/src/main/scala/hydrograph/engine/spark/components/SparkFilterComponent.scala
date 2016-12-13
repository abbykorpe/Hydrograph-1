package hydrograph.engine.spark.components

import java.util

import hydrograph.engine.core.component.entity.FilterEntity
import hydrograph.engine.spark.components.base.OperationComponentBase
import hydrograph.engine.spark.components.platform.BaseComponentParams
import hydrograph.engine.spark.components.utils._
import hydrograph.engine.transformation.userfunctions.base.FilterBase
import org.apache.spark.sql.{DataFrame, Row}
import scala.collection.JavaConverters._

/**
  * Created by vaijnathp on 12/12/2016.
  */
class SparkFilterComponent(filterEntity: FilterEntity, componentsParams: BaseComponentParams) extends
  OperationComponentBase with Serializable {

  override def createComponent(): Map[String, DataFrame] = {
    val scheme = componentsParams.getDataFrame.schema.map(e => e.name)

    val arr = new util.LinkedHashSet[String]()
    scheme.foreach(e => arr.add(e))
    var map: Map[String, DataFrame] = Map()

    val filterClass=classLoader[FilterBase](filterEntity.getOperation.getOperationClass)
    val fieldPosition=ReusableRowHelper(filterEntity.getOperation, null).determineInputFieldPositionsForFilter(scheme)
    val inputReusableRow=new SparkReusableRow(arr)


   filterEntity.getOutSocketList.asScala.foreach{outSocket=>
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

  def classLoader[T](className: String): T = {
    val clazz = Class.forName(className).getDeclaredConstructors
    clazz(0).setAccessible(true)
    clazz(0).newInstance().asInstanceOf[T]
  }
}
