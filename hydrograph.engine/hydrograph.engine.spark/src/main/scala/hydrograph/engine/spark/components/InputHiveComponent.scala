package hydrograph.engine.spark.components

import hydrograph.engine.core.component.entity.base.HiveEntityBase
import hydrograph.engine.core.component.entity.elements.SchemaField
import hydrograph.engine.spark.components.base.InputComponentBase
import hydrograph.engine.spark.components.platform.BaseComponentParams
import org.apache.spark.sql.DataFrame
import org.slf4j.LoggerFactory

import scala.collection.JavaConverters._

/**
  * Created by arshadalis on 12/13/2016.
  */
class InputHiveComponent(entity: HiveEntityBase, parameters: BaseComponentParams) extends InputComponentBase {
  val LOG = LoggerFactory.getLogger(classOf[InputHiveComponent])
  override def createComponent(): Map[String, DataFrame] = {
    LOG.trace("In method createComponent()")
    val sparkSession = parameters.getSparkSession()

    import sparkSession.sql


    val data = sql(constructQuery(entity))
    val key = entity.getOutSocketList.get(0).getSocketId

    LOG.info("Created Hive Input component " + entity.getComponentId + " in batch " + entity.getBatch + " with out socket " + key + " to read Hive table " + entity.getDatabaseName + "." + entity.getTableName)
    Map(key -> data)

  }

  def constructQuery(entity: HiveEntityBase): String = {
    LOG.trace("In method constructQuery() which returns constructed query to execute with spark-sql")
    var query = ""
    val databaseName = entity.getDatabaseName
    val tableName = entity.getTableName

    val fieldList = entity.getFieldsList.asScala.toList
    val partitionKeyValueMap = entity.getListOfPartitionKeyValueMap.asScala.toList


    query = query + "SELECT " + getFieldsForSelectHiveQuery(fieldList) + " FROM " + entity.getDatabaseName + "." + entity.getTableName

    if (partitionKeyValueMap != null && partitionKeyValueMap.size > 0)
      query = query + " WHERE " + getpartitionKeysClause(partitionKeyValueMap)

    query
  }

  def getFieldsForSelectHiveQuery(listOfFields: List[SchemaField]): String = {

    listOfFields.map(field => field.getFieldName).mkString(",")
  }

  def getpartitionKeysClause(pmap: List[java.util.HashMap[String,String]]): String = {

    pmap.map(m=> getKeyValuesForWhereClause(m.asScala.toMap)).mkString(" OR ")

  }

  def getKeyValuesForWhereClause(pmap: Map[String, String]): String = {

    pmap.map(e => e._1 + "='" + e._2 + "'").mkString(" AND ")

  }


}
