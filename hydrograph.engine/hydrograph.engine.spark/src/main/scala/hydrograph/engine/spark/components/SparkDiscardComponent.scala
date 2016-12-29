package hydrograph.engine.spark.components


import hydrograph.engine.core.component.entity.DiscardEntity
import hydrograph.engine.core.component.entity.elements.{KeyField, SchemaField}
import hydrograph.engine.spark.components.base.SparkFlow
import hydrograph.engine.spark.components.platform.BaseComponentParams
import hydrograph.engine.spark.components.utils.{FieldManupulating, OperationSchemaCreator, RowHelper, SchemaCreator}
import org.apache.spark.sql.{Column, DataFrame}

/**
  * Created by snehada on 12/16/2016.
  */
class SparkDiscardComponent (discardEntity: DiscardEntity, componentsParams: BaseComponentParams) extends SparkFlow
{
    override def execute(): Unit = {
     componentsParams.getDataFrame().count()
  }
}
