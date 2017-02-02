package hydrograph.engine.spark.components.utils

import org.slf4j.{Logger, LoggerFactory}

/**
  * Created by santlalg on 12/12/2016.
  */
class DbTableDescriptor(tableName: String, columnNames: Array[String], columnDefs: Array[String], primaryKeys: Array[String], databaseType: String) extends Serializable {

  val LOG: Logger = LoggerFactory.getLogger(classOf[DbTableDescriptor])
  val DB_TYPE_ORACLE = "oracle"
  var createTableStatement: List[String] = Nil
  var field: String = ""

  def getCreateTableStatement(): String = {

    createTableStatement = addCreateTableBody()
    val createTableStatment = String.format("CREATE TABLE %s ( %s )", tableName, joinField(createTableStatement.reverse, ","))
    LOG.info("Create query '" + createTableStatment + "' for " + databaseType + " output component")
    createTableStatment
  }

  def addCreateTableBody(): List[String] = {

    createTableStatement = addDefinitions();
    createTableStatement = addPrimaryKey();

    createTableStatement;
  }

  def addDefinitions(): List[String] = {
    if (databaseType.equalsIgnoreCase(DB_TYPE_ORACLE))
      for (i <- 0 until columnNames.length) {
        createTableStatement = (quoteIdentifier(columnNames(i).toUpperCase) + " " + columnDefs(i)) :: createTableStatement
      }
    else {
      for (i <- 0 until columnNames.length) {
        createTableStatement = (columnNames(i) + " " + columnDefs(i)) :: createTableStatement
      }
    }
    createTableStatement
  }

  def quoteIdentifier(colName: String): String = {
    s""""$colName""""
  }

  def addPrimaryKey(): List[String] = {

    if (hasPrimaryKey)
      createTableStatement = String.format("PRIMARY KEY( %s )", joinField(primaryKeys.toList, ",")) :: createTableStatement

    createTableStatement
  }

  private def hasPrimaryKey(): Boolean = primaryKeys != null && primaryKeys.length != 0

  def joinField(createTableStatement: List[String], s: String): String = {
    field = ""
    for (in <- 0 until createTableStatement.length) {
      if (in != 0) field = field + s
      if (createTableStatement(in) != null) field = field + createTableStatement(in)
    }
    field
  }
}
