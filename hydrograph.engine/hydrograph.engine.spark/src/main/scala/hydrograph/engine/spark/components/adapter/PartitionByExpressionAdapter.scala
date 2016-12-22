package hydrograph.engine.spark.components.adapter

import hydrograph.engine.core.component.generator.PartitionByExpressionEntityGenerator
import hydrograph.engine.jaxb.commontypes.TypeBaseComponent
import hydrograph.engine.spark.components.PartitionByExpressionComponent
import hydrograph.engine.spark.components.adapter.base.OperationAdatperBase
import hydrograph.engine.spark.components.base.OperationComponentBase
import hydrograph.engine.spark.components.platform.BaseComponentParams

/**
  * Created by santlalg on 12/15/2016.
  */
class PartitionByExpressionAdapter (typeBaseComponent: TypeBaseComponent) extends OperationAdatperBase{

  var partitionByExpression:PartitionByExpressionEntityGenerator=null;
  var sparkPartitionByExpressionComponent:PartitionByExpressionComponent=null;

  override def createGenerator(): Unit = {
    partitionByExpression=  new PartitionByExpressionEntityGenerator(typeBaseComponent)
  }

  override def createComponent(baseComponentParams: BaseComponentParams): Unit = {
    sparkPartitionByExpressionComponent= new PartitionByExpressionComponent(partitionByExpression.getEntity,baseComponentParams)
  }

  override def getComponent(): OperationComponentBase = sparkPartitionByExpressionComponent
}
