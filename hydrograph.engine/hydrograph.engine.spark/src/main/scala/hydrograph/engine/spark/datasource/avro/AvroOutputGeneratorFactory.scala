package hydrograph.engine.spark.datasource.avro

import org.apache.hadoop.mapreduce.TaskAttemptContext

import org.apache.spark.sql.execution.datasources.text.TextOutputWriter
import org.apache.spark.sql.execution.datasources.{OutputWriter, OutputWriterFactory}
import org.apache.spark.sql.types.StructType

 private class AvroOutputWriterFactory(schema: StructType, recordName: String, recordNamespace: String) extends OutputWriterFactory {

   override def newInstance(
      path: String,
      bucketId: Option[Int], // TODO: This doesn't belong here...
      dataSchema: StructType,
      context: TaskAttemptContext): OutputWriter = {
     new AvroOutputGenerator(path,context,schema,recordName,recordNamespace)
   }

   def getFileExtension(context: TaskAttemptContext): String ={
     ".avro"
   }
 }