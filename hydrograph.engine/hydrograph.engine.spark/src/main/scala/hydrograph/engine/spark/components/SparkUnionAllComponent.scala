package hydrograph.engine.spark.components

import hydrograph.engine.core.component.entity.UnionAllEntity
import hydrograph.engine.spark.components.base.StraightPullComponentBase
import hydrograph.engine.spark.components.platform.BaseComponentParams
import org.apache.spark.sql.DataFrame

import scala.collection.mutable.ListBuffer
import hydrograph.engine.core.component.entity.elements.SchemaField
import java.util.Set

/**
 * Created by gurdits on 10/15/2016.
 */
class SparkUnionAllComponent(unionAllEntity: UnionAllEntity, componentsParams: BaseComponentParams)
    extends StraightPullComponentBase {

  override def createComponent(): Map[String, DataFrame] = {

    val dataFrameList = componentsParams.getDataFrameList()
    val schemaFieldList = componentsParams.getSchemaFieldList()
    validateInputFields(schemaFieldList)
    val df = merge(dataFrameList)
    val key = unionAllEntity.getOutSocketList.get(0).getSocketId
    Map(key -> df)

  }

  def merge(dataFrameList: ListBuffer[DataFrame]): DataFrame = {
    def combine(dataFrame: DataFrame, acc: Int): DataFrame = {
      if (dataFrameList.size <= acc) dataFrame
      else combine(dataFrame.union(dataFrameList(acc)), acc + 1)
    }
    combine(dataFrameList(0), 1)
  }

  @throws(classOf[SchemaMismatchException])
  def validateInputFields(schemaFieldList: ListBuffer[Set[SchemaField]]) = {

    val refSchema = schemaFieldList(0)

    schemaFieldList.tail.foreach { sf => 
      {
         if (refSchema.size != sf.size) throw new SchemaMismatchException("Component:" + unionAllEntity.getComponentId()
          + " - Different schema is defined for input sockets. For UnionAll component schema of all input sockets should be same.");

        if (!refSchema.containsAll(sf)) throw new SchemaMismatchException("Component:" + unionAllEntity.getComponentId()
          + " - Different schema is defined for input sockets. For UnionAll component schema of all input sockets should be same.");
      }
    }
  }

  class SchemaMismatchException(msg: String) extends RuntimeException(msg: String)

}

