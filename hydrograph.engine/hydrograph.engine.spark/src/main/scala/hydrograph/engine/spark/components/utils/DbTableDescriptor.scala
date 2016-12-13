package hydrograph.engine.spark.components.utils

/**
  * Created by santlalg on 12/12/2016.
  */
class DbTableDescriptor(tableName:String, columnNames:Array[String], columnDefs:Array[String], primaryKeys:Array[String])  extends Serializable{

  var createTableStatement: List[String] = Nil
  var field:String  = ""

  def getCreateTableStatement(): String = {

    createTableStatement = addCreateTableBody()
    String.format("CREATE TABLE %s ( %s )",tableName,joinField(createTableStatement.reverse,","))
  }

 def addCreateTableBody(): List[String] = {

    createTableStatement = addDefinitions();
    createTableStatement = addPrimaryKey();

    return createTableStatement;
  }
  def addDefinitions(): List[String] = {
    for(i  <- 0 until columnNames.length){
      createTableStatement = (columnNames(i) + " " + columnDefs(i)) :: createTableStatement
    }
    createTableStatement
  }

  def addPrimaryKey(): List[String] = {

    if(hasPrimaryKey)
      createTableStatement =  String.format( "PRIMARY KEY( %s )", joinField(primaryKeys.toList,",")) :: createTableStatement

    createTableStatement
  }

  private def hasPrimaryKey(): Boolean = primaryKeys != null && primaryKeys.length !=0

 def joinField(createTableStatement: List[String], s: String): String = {
    field = ""
    for ( in <- 0 until createTableStatement.length) {
      if(in !=0) field = field + s
      if(createTableStatement(in) != null) field = field + createTableStatement(in)
    }
    field
  }
}
