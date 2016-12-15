package hydrograph.engine.spark.components.handler

/**
  * Created by vaijnathp on 12/13/2016.
  */
trait ClassStringHandler {
  def classLoader[T](className: String): T = {
    val clazz = Class.forName(className).getDeclaredConstructors
    clazz(0).setAccessible(true)
    clazz(0).newInstance().asInstanceOf[T]
  }
}
