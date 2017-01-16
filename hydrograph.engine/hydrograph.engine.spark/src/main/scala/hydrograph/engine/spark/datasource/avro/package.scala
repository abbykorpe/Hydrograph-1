
package hydrograph.engine.spark.datasource

import org.apache.spark.sql.{DataFrame, DataFrameReader, DataFrameWriter}

package object avro {
  /**
   * Adds a method, `avro`, to DataFrameWriter that allows you to write avro files using
   * the DataFileWriter
   */
  implicit class AvroDataFrameWriter[T](writer: DataFrameWriter[T]) {
    def avro: String => Unit = writer.format("hydrograph.engine.spark.datasource").save
  }

  /**
   * Adds a method, `avro`, to DataFrameReader that allows you to read avro files using
   * the DataFileReade
   */
  implicit class AvroDataFrameReader(reader: DataFrameReader) {
    def avro: String => DataFrame = reader.format("hydrograph.engine.spark.datasource").load
  }
}
