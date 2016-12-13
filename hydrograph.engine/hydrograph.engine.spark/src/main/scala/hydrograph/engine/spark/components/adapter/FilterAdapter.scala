package hydrograph.engine.spark.components.adapter

import hydrograph.engine.core.component.generator.FilterEntityGenerator
import hydrograph.engine.jaxb.commontypes.TypeBaseComponent
import hydrograph.engine.spark.components.SparkFilterComponent
import hydrograph.engine.spark.components.adapter.base.OperationAdatperBase
import hydrograph.engine.spark.components.base.OperationComponentBase
import hydrograph.engine.spark.components.platform.BaseComponentParams

/**
  * Created by vaijnathp on 12/12/2016.
  */
class FilterAdapter(typeBaseComponent: TypeBaseComponent) extends OperationAdatperBase {

  var filter: FilterEntityGenerator = null;
  var sparkFilterComponent: SparkFilterComponent = null;

  override def createGenerator(): Unit = {
    filter = new FilterEntityGenerator(typeBaseComponent)
  }

  override def createComponent(baseComponentParams: BaseComponentParams): Unit = {
    sparkFilterComponent = new SparkFilterComponent(filter.getEntity, baseComponentParams)
  }

  override def getComponent(): OperationComponentBase = sparkFilterComponent
}
