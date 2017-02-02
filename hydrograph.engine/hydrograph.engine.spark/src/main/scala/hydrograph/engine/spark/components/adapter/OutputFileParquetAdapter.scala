package hydrograph.engine.spark.components.adapter

import hydrograph.engine.core.component.generator.OutputFileParquetEntityGenerator
import hydrograph.engine.jaxb.commontypes.TypeBaseComponent
import hydrograph.engine.spark.components.OutputFileParquetComponent
import hydrograph.engine.spark.components.adapter.base.OutputAdatperBase
import hydrograph.engine.spark.components.base.SparkFlow
import hydrograph.engine.spark.components.platform.BaseComponentParams

class OutputFileParquetAdapter (typeBaseComponent: TypeBaseComponent) extends OutputAdatperBase{
  
  
    private  var outputFileParquet:OutputFileParquetEntityGenerator=null
    private var oFileParquetComponent:OutputFileParquetComponent=null

  override def createGenerator(): Unit = {
    outputFileParquet=new OutputFileParquetEntityGenerator(typeBaseComponent)
  }

  override def createComponent(baseComponentParams: BaseComponentParams): Unit = {
    oFileParquetComponent = new OutputFileParquetComponent(outputFileParquet.getEntity,
      baseComponentParams)
  }

  override def getComponent(): SparkFlow = oFileParquetComponent
  
}