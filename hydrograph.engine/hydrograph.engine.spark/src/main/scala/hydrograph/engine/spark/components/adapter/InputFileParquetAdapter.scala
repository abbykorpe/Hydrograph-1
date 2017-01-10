package hydrograph.engine.spark.components.adapter

import hydrograph.engine.core.component.generator.InputFileParquetEntityGenerator
import hydrograph.engine.jaxb.commontypes.TypeBaseComponent
import hydrograph.engine.spark.components.InputFileParquetComponent
import hydrograph.engine.spark.components.adapter.base.InputAdatperBase
import hydrograph.engine.spark.components.base.InputComponentBase
import hydrograph.engine.spark.components.platform.BaseComponentParams

class InputFileParquetAdapter(typeBaseComponent: TypeBaseComponent) extends InputAdatperBase {

  private var inputFileParquet: InputFileParquetEntityGenerator = null
  private var iFileParquetComponent: InputFileParquetComponent = null

  override def createGenerator(): Unit = {
    inputFileParquet = new InputFileParquetEntityGenerator(typeBaseComponent)
  }

  override def createComponent(baseComponentParams: BaseComponentParams): Unit = {
    iFileParquetComponent = new InputFileParquetComponent(inputFileParquet.getEntity,
      baseComponentParams)
  }

  override def getComponent(): InputComponentBase = iFileParquetComponent
}