package hydrograph.engine.spark.components

import java.util
import java.util.Properties

import hydrograph.engine.core.component.entity.InputRDBMSEntity
import hydrograph.engine.core.component.entity.elements.{OutSocket, SchemaField}
import hydrograph.engine.spark.components.platform.BaseComponentParams
import org.apache.spark.sql.{SparkSession, _}
import org.junit.{Assert, Test}

/**
  * Created by manojp on 1/6/2017.
  */
class InputTeradataComponentTest {


  @Test
  def itShouldReadRecordFromTableUsingTable(): Unit = {

    // given
    val inputRDBMSEntity: InputRDBMSEntity = new InputRDBMSEntity
    inputRDBMSEntity setComponentId ("TeradataInputComponent")
    inputRDBMSEntity setDatabaseName ("hydrograph_db")
    inputRDBMSEntity setHostName ("10.130.250.235")
    inputRDBMSEntity setPort (1025)
    inputRDBMSEntity setJdbcDriver ("TeraJDBC4")
    inputRDBMSEntity setTableName ("tableTest")
    inputRDBMSEntity setUsername ("hydrograph")
    inputRDBMSEntity setPassword ("teradata")

    val sf1: SchemaField = new SchemaField("id", "java.lang.Integer");
    val sf2: SchemaField = new SchemaField("name", "java.lang.String");
    val sf3: SchemaField = new SchemaField("city", "java.lang.String");
    val sf4: SchemaField = new SchemaField("creditPoint", "java.lang.Double");
    val fieldList: util.ArrayList[SchemaField] = new util.ArrayList[SchemaField]();
    fieldList.add(sf1);
    fieldList.add(sf2);
    fieldList.add(sf3);
    fieldList.add(sf4);
    inputRDBMSEntity.setFieldsList(fieldList)
    inputRDBMSEntity.setRuntimeProperties(new Properties)

    val outSockets = new util.ArrayList[OutSocket]();
    outSockets add (new OutSocket("outSocket"));

    inputRDBMSEntity setOutSocketList (outSockets)

    val sparkSession = SparkSession.builder()
      .master("local")
      .appName("testing")
      .config("spark.sql.shuffle.partitions", "1")
      .config("spark.sql.warehouse.dir", "file:///tmp")
      .getOrCreate()

    val cp = new BaseComponentParams
    cp.setSparkSession(sparkSession)

    //when
    val df: Map[String, DataFrame] = new InputTeradataComponent(inputRDBMSEntity, cp).createComponent()

    val rows = df.get("outSocket").get.select("id").collect().toList

    println(rows)
    //then
    val expected = 5
    Assert.assertEquals(rows.length, expected)
  }

  @Test
  def itShouldReadRecordFromTableUsingQuery(): Unit = {

    // given
    val inputRDBMSEntity: InputRDBMSEntity = new InputRDBMSEntity
    inputRDBMSEntity setComponentId ("TeradataInputComponent")
    inputRDBMSEntity setDatabaseName ("hydrograph_db")
    inputRDBMSEntity setHostName ("10.130.250.235")
    inputRDBMSEntity setPort (1025)
    inputRDBMSEntity setJdbcDriver ("TeraJDBC4")
    inputRDBMSEntity setSelectQuery ("select * from tableTest where id=40")
    inputRDBMSEntity setUsername ("hydrograph")
    inputRDBMSEntity setPassword ("teradata")

    val sf1: SchemaField = new SchemaField("id", "java.lang.Integer");
    val sf2: SchemaField = new SchemaField("name", "java.lang.String");
    val sf3: SchemaField = new SchemaField("city", "java.lang.String");
    val sf4: SchemaField = new SchemaField("creditPoint", "java.lang.Double");
    val fieldList: util.ArrayList[SchemaField] = new util.ArrayList[SchemaField]();
    fieldList.add(sf1);
    fieldList.add(sf2);
    fieldList.add(sf3);
    fieldList.add(sf4);
    inputRDBMSEntity.setFieldsList(fieldList)

    val outSockets = new util.ArrayList[OutSocket]();
    outSockets.add(new OutSocket("outSocket"));

    inputRDBMSEntity.setOutSocketList(outSockets)
    inputRDBMSEntity.setRuntimeProperties(new Properties)

    val sparkSession = SparkSession.builder()
      .master("local")
      .appName("testing")
      .config("spark.sql.shuffle.partitions", "1")
      .config("spark.sql.warehouse.dir", "file:///tmp")
      .getOrCreate()

    val cp = new BaseComponentParams
    cp.setSparkSession(sparkSession)

    //when
    val df: Map[String, DataFrame] = new InputTeradataComponent(inputRDBMSEntity, cp).createComponent()

    val rows = df.get("outSocket").get.select("id").collect()

    //then
    val expected = 1
    Assert.assertEquals(rows.length, expected)
  }


}
