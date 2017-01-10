package hydrograph.engine.spark.components

import hydrograph.engine.core.component.entity.CloneEntity
import hydrograph.engine.core.component.entity.elements.OutSocket
import hydrograph.engine.spark.components.base.StraightPullComponentBase
import hydrograph.engine.spark.components.platform.BaseComponentParams
import org.apache.spark.sql.DataFrame

import scala.collection.JavaConversions._
/**
  * Created by gurdits on 10/15/2016.
  */
class SparkCloneComponent(cloneEntity: CloneEntity, componentsParams: BaseComponentParams)
  extends
    StraightPullComponentBase {


  override def createComponent(): Map[String, DataFrame] = {

    def generateDataFrame(outSocketList:List[OutSocket]):Map[String,DataFrame] = outSocketList match {
      case List() => Map()
      case x::xs => Map(x.getSocketId->componentsParams.getDataFrame()) ++ generateDataFrame(xs)
    }
    generateDataFrame(cloneEntity.getOutSocketList.toList)
  }

}

