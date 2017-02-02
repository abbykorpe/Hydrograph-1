package hydrograph.engine.spark.components.base

import org.apache.spark.sql.DataFrame

/**
  * Created by gurdits on 10/15/2016.
  */
abstract class BaseComponent() {

  def createComponent():List[DataFrame]

}
