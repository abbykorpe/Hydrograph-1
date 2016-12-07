package hydrograph.engine.spark.components

import hydrograph.engine.core.component.entity.UnionAllEntity
import hydrograph.engine.spark.components.base.StraightPullComponentBase
import hydrograph.engine.spark.components.platform.BaseComponentParams
import org.apache.spark.sql.DataFrame

import scala.collection.mutable.ListBuffer

/**
  * Created by gurdits on 10/15/2016.
  */
class SparkUnionAllComponent(unionAllEntity: UnionAllEntity, componentsParams: BaseComponentParams)
  extends
    StraightPullComponentBase {


  def merge(dataFrameList: ListBuffer[DataFrame]): DataFrame = {
    def combine(dataFrame: DataFrame, acc: Int): DataFrame = {
      if (dataFrameList.size <= acc) dataFrame
      else combine(dataFrame.union(dataFrameList(acc)),acc + 1)
    }
    combine(dataFrameList(0),1)
  }

  override def createComponent(): Map[String, DataFrame] = {

    val df=merge(componentsParams.getDataFrameList())
    val key = unionAllEntity.getOutSocketList.get(0).getSocketId
    Map(key -> df)

  }

}

