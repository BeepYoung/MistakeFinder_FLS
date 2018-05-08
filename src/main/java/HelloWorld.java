import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.DataFrame;
import org.apache.spark.sql.SQLContext;
import org.apache.spark.sql.types.StructType;

import java.util.ArrayList;
import java.util.List;

//hdfs://quickstart.cloudera:8020/user/root/... path

//spark-submit --packages com.databricks:spark-csv_2.10:1.5.0  testApp.jar testProg/*

public class HelloWorld {
    public  static void main(String args[]){
        SparkConf conf = new SparkConf().setAppName("someApp").setMaster("local[*]");
        JavaSparkContext sc = new JavaSparkContext(conf);
        SQLContext sqlContext = new org.apache.spark.sql.SQLContext(sc);

        DataFrame df = sqlContext.read()
                .format("com.databricks.spark.csv")
                .option("inferSchema","true")
                .option("header","true")
                .load(args[0]);

        df.show();
        df.printSchema();



    }
}
