/*******************************************************************************
 * Copyright 2017 Capital One Services, LLC and Bitwise, Inc.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package hydrograph.engine.spark.components.utils

import org.apache.spark.sql.types._
import org.junit.{Assert, Test}
/**
  * Created by santlalg on 1/2/2017.
  */
class SchemaUtilsTest {

  @Test
  def itShouldCompareSchemaWhenBothSchemaHaveSameNumberOfElement(): Unit = {

    val schema1 = new Array[StructField](4)

    schema1(0) = StructField("id", IntegerType)
    schema1(1) = StructField("salary", DoubleType)
    schema1(2) = StructField("accountNumer", LongType)
    schema1(3) = StructField("dob", DateType)

    val structType1 = new StructType(schema1)

    val schema2 = new Array[StructField](4)

    schema2(0) = StructField("id", IntegerType)
    schema2(1) = StructField("salary", DoubleType)
    schema2(2) = StructField("accountNumer", LongType)
    schema2(3) = StructField("dob", DateType)

    val structType2 = new StructType(schema2)

    Assert.assertTrue(SchemaUtils().compareSchema(structType1, structType2))
  }

  @Test
  def itShouldCompareFirstSchemaShouldPartOfSecondSchema(): Unit = {

    val schema1 = new Array[StructField](2)

    schema1(0) = StructField("dob", IntegerType)
    schema1(1) = StructField("salary", DoubleType)

    val structType2 = new StructType(schema1)

    val schema2 = new Array[StructField](4)

    schema2(0) = StructField("id", IntegerType)
    schema2(1) = StructField("salary", DoubleType)
    schema2(2) = StructField("accountNumer", LongType)
    schema2(3) = StructField("dob", DateType)

    val structType1 = new StructType(schema1)

    Assert.assertTrue(SchemaUtils().compareSchema(structType1, structType2))
  }

  @Test (expected = classOf[hydrograph.engine.spark.components.utils.SchemaMismatchException])
  def itShouldRaiseExceptionWhenFirstSchemaFieldDataTypeDoesNotMatchWithSecondSchemaFieldDataType(): Unit = {

    val schema1 = new Array[StructField](2)

    schema1(0) = StructField("id", IntegerType)
    schema1(1) = StructField("salary", IntegerType)

    val structType1 = new StructType(schema1)

    val schema2 = new Array[StructField](4)

    schema2(0) = StructField("id", IntegerType)
    schema2(1) = StructField("salary", DoubleType)
    schema2(2) = StructField("accountNumer", LongType)
    schema2(3) = StructField("dob", DateType)

    val structType2 = new StructType(schema2)

    SchemaUtils().compareSchema(structType1, structType2)
  }

  @Test (expected = classOf[hydrograph.engine.spark.components.utils.SchemaMismatchException])
  def itShouldRaiseExceptionWhenFirstSchemaFieldIsNotPartOfSecondSchema(): Unit = {

    val schema1 = new Array[StructField](2)

    schema1(0) = StructField("id", IntegerType)
    schema1(1) = StructField("accNo", IntegerType)

    val structType1 = new StructType(schema1)

    val schema2 = new Array[StructField](4)

    schema2(0) = StructField("id", IntegerType)
    schema2(1) = StructField("salary", DoubleType)
    schema2(2) = StructField("accountNumer", LongType)
    schema2(3) = StructField("dob", DateType)

    val structType2 = new StructType(schema2)

    SchemaUtils().compareSchema(structType1, structType2)
  }
}
