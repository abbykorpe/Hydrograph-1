package hydrograph.engine.spark.components

import java.util

import hydrograph.engine.core.component.entity.InputRDBMSEntity
import hydrograph.engine.core.component.entity.elements.{OutSocket, SchemaField}
import hydrograph.engine.spark.components.platform.BaseComponentParams
import org.apache.spark.sql._
import org.junit.Test
import org.scalatest.Matchers._

/**
  * Created by santlalg on 12/8/2016.
  */
class SparkIMysqlComponentTest {

  @Test
  def itShouldReadRecordFromTableUsingTable(): Unit ={

    // given
    val inputRDBMSEntity: InputRDBMSEntity= new InputRDBMSEntity
    inputRDBMSEntity setComponentId("MysqlInputComponent")
    inputRDBMSEntity setDatabaseName("test")
    inputRDBMSEntity setHostName("10.130.248.53")
    inputRDBMSEntity setPort(3306)
    inputRDBMSEntity setJdbcDriver("com.mysql.jdbc.Driver")
    inputRDBMSEntity setTableName("ideaJDBC")
    inputRDBMSEntity setUsername("root")
    inputRDBMSEntity setPassword("root")

    val sf1:SchemaField = new SchemaField("id", "java.lang.Integer");
    val sf2:SchemaField = new SchemaField("name", "java.lang.String");
    val sf3:SchemaField = new SchemaField("city", "java.lang.String");
    val sf4:SchemaField = new SchemaField("creditPoint", "java.lang.Double");
    val fieldList:util.ArrayList[SchemaField] = new util.ArrayList[SchemaField]();
    inputRDBMSEntity.setFieldsList(fieldList)

    val outSockets = new util.ArrayList[OutSocket]();
    outSockets.add(new OutSocket("outSocket"));

    inputRDBMSEntity.setOutSocketList(outSockets)

    val sparkSession = SparkSession.builder()
      .master("local")
      .appName("testing")
      .config("spark.sql.shuffle.partitions", "1")
      .config("spark.sql.warehouse.dir", "file:///tmp")
      .getOrCreate()

    val cp= new BaseComponentParams
    cp.setSparkSession(sparkSession)

    //when
    val df:Map[String,DataFrame] = new SparkIMysqlComponent(inputRDBMSEntity,cp).createComponent()

    val rows=df.get("outSocket").get.select("id").collect().toList

    println(rows)
    //then
    rows should have size 5
  }

  @Test
  def itShouldReadRecordFromTableUsingQuery(): Unit ={

    // given
    val inputRDBMSEntity: InputRDBMSEntity= new InputRDBMSEntity
    inputRDBMSEntity setComponentId("MysqlInputComponent")
    inputRDBMSEntity setDatabaseName("test")
    inputRDBMSEntity setHostName("10.130.248.53")
    inputRDBMSEntity setPort(3306)
    inputRDBMSEntity setJdbcDriver("com.mysql.jdbc.Driver")
    inputRDBMSEntity setSelectQuery("select * from ideaJDBC where id=40")
    inputRDBMSEntity setUsername("root")
    inputRDBMSEntity setPassword("root")

    val sf1:SchemaField = new SchemaField("id", "java.lang.Integer");
    val sf2:SchemaField = new SchemaField("name", "java.lang.String");
    val sf3:SchemaField = new SchemaField("city", "java.lang.String");
    val sf4:SchemaField = new SchemaField("creditPoint", "java.lang.Double");
    val fieldList:util.ArrayList[SchemaField] = new util.ArrayList[SchemaField]();
    inputRDBMSEntity.setFieldsList(fieldList)

    val outSockets = new util.ArrayList[OutSocket]();
    outSockets.add(new OutSocket("outSocket"));

    inputRDBMSEntity.setOutSocketList(outSockets)

    val sparkSession = SparkSession.builder()
      .master("local")
      .appName("testing")
      .config("spark.sql.shuffle.partitions", "1")
      .config("spark.sql.warehouse.dir", "file:///tmp")
      .getOrCreate()

    val cp= new BaseComponentParams
    cp.setSparkSession(sparkSession)

    //when
    val df:Map[String,DataFrame] = new SparkIMysqlComponent(inputRDBMSEntity,cp).createComponent()

    val rows=df.get("outSocket").get.select("id").collect()


    //then
    rows should have size 1
  }
}
