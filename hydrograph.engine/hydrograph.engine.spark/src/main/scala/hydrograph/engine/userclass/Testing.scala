package hydrograph.engine.userclass

import org.apache.spark.sql.{Row, SparkSession}
import org.apache.spark.sql.catalyst.encoders.RowEncoder
import org.apache.spark.sql.types.{StructType, DataTypes, StructField}
import scala.collection.JavaConversions._
import scala.collection.JavaConverters._
/**
  * Created by gurdits on 12/1/2016.
  */
object Testing {

  def main(args: Array[String]) {

    val ss = SparkSession.builder()
      .master("local")
      .appName("test")
      .config("spark.sql.shuffle.partitions", "1")
      .config("spark.sql.warehouse.dir", "file:///tmp")
      .getOrCreate()

    val indf=ss.read.option("delimiter", ",").csv("C:\\Users\\gurdits\\ideaProjects\\spark-poc\\testData\\inputFiles\\delimitedInputFile.txt")

    val schema1 = StructType(List(StructField("id1", DataTypes.StringType, true)
      ,StructField("name", DataTypes.StringType, true)
      ,StructField("sal", DataTypes.StringType, true)))

    val key = StructType(List(StructField("key", DataTypes.StringType, true)))
    val groupdf=indf.groupByKey(r=>Row(r.get(0)))(RowEncoder(key))

    groupdf.flatMapGroups((r,i)=>{

      val itr=i.map(r=>{
        r
      })
      if(itr.isEmpty)
      println("group");

      itr
    })(RowEncoder(schema1)).show()



  }

}
