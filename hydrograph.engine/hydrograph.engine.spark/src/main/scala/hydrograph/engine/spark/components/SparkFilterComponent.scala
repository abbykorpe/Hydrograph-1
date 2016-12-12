package hydrograph.engine.spark.components

import java.util

import hydrograph.engine.core.component.entity.FilterEntity
import hydrograph.engine.spark.components.base.OperationComponentBase
import hydrograph.engine.spark.components.platform.BaseComponentParams
import hydrograph.engine.spark.components.utils._
import hydrograph.engine.transformation.userfunctions.base.FilterBase
import org.apache.spark.sql.{DataFrame, Row}

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

   for(i <- 0 until filterEntity.getOutSocketList.size()){
    val df = componentsParams.getDataFrame.filter(
       row =>{
         if(filterEntity.getOutSocketList.get(i).getSocketType.equalsIgnoreCase("unused"))
           !filter(scheme, arr, row)
         else
            filter(scheme, arr, row)
       })
    map += (filterEntity.getOutSocketList.get(i).getSocketId -> df)
  }
    map
  }

  def filter(scheme: Seq[String], arr: util.LinkedHashSet[String], row: Row): Boolean = {
    classLoader[FilterBase](filterEntity.getOperation.getOperationClass)
      .isRemove(RowHelper.convertToReusebleRow(ReusableRowHelper(filterEntity.getOperation, null).determineInputFieldPositionsForFilter(scheme), row, new SparkReusableRow(arr)))
  }

  def classLoader[T](className: String): T = {
    val clazz = Class.forName(className).getDeclaredConstructors
    clazz(0).setAccessible(true)
    clazz(0).newInstance().asInstanceOf[T]
  }
}
