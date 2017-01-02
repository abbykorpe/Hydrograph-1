package hydrograph.engine.spark.components.utils

import org.junit.{Assert, Test}

/**
  * Created by santlalg on 12/20/2016.
  */

class DbTableDescriptorTest {

  @Test
  def itShouldGenerateCreateQueryWithoutPrimaryKeys(): Unit = {

    //given
    val databaseType:String="Mysql"
    val tableName="abc"
    val fieldNames : Array[String]=Array("f1","f2","f3","f4","f5","f6","f7")
    val fieldDataType:Array[String] = Array("java.lang.String","java.lang.Integer","java.lang.Double","java.math.BigDecimal","java.util.Date","java.lang.Boolean","java.util.Date");
    val fieldScale:Array[Int]=Array(-999,-999,-999,2,-999,-999,-999)
    val fieldPrecision:Array[Int]=Array(-999,-999,-999,10,-999,-999,-999)
    val colDefs = JavaToSQLTypeMapping.createTypeMapping(databaseType,fieldDataType,fieldScale,fieldPrecision)
    val primaryKeys=null

    //when
    val createQuery= new DbTableDescriptor(tableName, fieldNames, colDefs, primaryKeys,databaseType).getCreateTableStatement()


    //then
    val expectedQuery:String="CREATE TABLE abc ( f1 VARCHAR(256),f2 INT,f3 DOUBLE,f4 DECIMAL(10,2),f5 TIMESTAMP,f6 TINYINT,f7 TIMESTAMP )";

    Assert.assertTrue(expectedQuery.equals(createQuery))
  }

  @Test
  def itShouldGenerateCreateQueryWithPrimaryKeys(): Unit = {

    //given
    val databaseType:String="Mysql"
    val tableName="abc"
    val fieldNames : Array[String]=Array("f1","f2","f3","f4","f5","f6","f7")
    val fieldDataType:Array[String] = Array("java.lang.String","java.lang.Integer","java.lang.Double","java.math.BigDecimal","java.util.Date","java.lang.Boolean","java.util.Date");
    val fieldScale:Array[Int]=Array(-999,-999,-999,2,-999,-999,-999)
    val fieldPrecision:Array[Int]=Array(-999,-999,-999,10,-999,-999,-999)
    val colDefs = JavaToSQLTypeMapping.createTypeMapping(databaseType,fieldDataType,fieldScale,fieldPrecision)
    val primaryKeys:Array[String]= Array("f1","f2")

    //when
    val createQuery= new DbTableDescriptor(tableName, fieldNames, colDefs, primaryKeys,databaseType).getCreateTableStatement()

    //then
    val expectedQuery:String="CREATE TABLE abc ( f1 VARCHAR(256),f2 INT,f3 DOUBLE,f4 DECIMAL(10,2),f5 TIMESTAMP,f6 TINYINT,f7 TIMESTAMP,PRIMARY KEY( f1,f2 ) )";

    Assert.assertTrue(expectedQuery.equals(createQuery))
  }
}
