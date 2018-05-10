import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.DataFrame;
import org.apache.spark.sql.SQLContext;
import org.apache.spark.sql.SaveMode;
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

        sc.setLogLevel("ERROR");
        System.out.println("Reading DFs");
        DataFrame sellerTaxTable = sqlContext.read()
                .format("com.databricks.spark.csv")
                .option("inferSchema","true")
                .option("header","true")
                .load(args[0]);

        DataFrame buyerTaxTable = sqlContext.read()
                .format("com.databricks.spark.csv")
                .option("inferSchema","true")
                .option("header","true")
                .load(args[1]);



        DataFrame tableWithMistakes = sellerTaxTable.join(buyerTaxTable,buyerTaxTable.col("INN_SALE").equalTo(sellerTaxTable.col("INN_SALE"))
                .or(buyerTaxTable.col("KPP_SALE").equalTo(sellerTaxTable.col("KPP_SALE")))
                .or(buyerTaxTable.col("SUM_TABLE_SALE").equalTo(sellerTaxTable.col("SUM_TABLE_PURCHASE")))
                .or(buyerTaxTable.col("KPP_PURCHASE").equalTo(sellerTaxTable.col("KPP_PURCHASE")))
                .or(buyerTaxTable.col("INN_PURCHASE").equalTo(sellerTaxTable.col("INN_PURCHASE")))
                )
                .filter(buyerTaxTable.col("SUM_TABLE_SALE").notEqual(sellerTaxTable.col("SUM_TABLE_PURCHASE"))
                    .or(buyerTaxTable.col("KPP_SALE").notEqual(sellerTaxTable.col("KPP_SALE")))
                    .or(buyerTaxTable.col("INN_SALE").notEqual(sellerTaxTable.col("INN_SALE")))
                    .or(buyerTaxTable.col("KPP_PURCHASE").notEqual(sellerTaxTable.col("KPP_PURCHASE")))
                    .or(buyerTaxTable.col("INN_PURCHASE").notEqual(sellerTaxTable.col("INN_PURCHASE")))
                    )

               ;
        tableWithMistakes.persist();
        tableWithMistakes.show();
        tableWithMistakes.persist();
        /*System.out.println("Count of mistakes " + tableWithMistakes.count());
        String schemaString = "INN_SALE_FROM_SELLER KPP_SALE_FROM_SELLER INN_PURCHASE_FROM_SELLER KPP_PURCHASE_FROM_SELLER SUM_TABLE_PURCHASE NDS_TABLE_PURCHASE INN_PURCHASE_FROM_BUYER KPP_PURCHASE_FROM_BUYER " +
                "INN_SALE_FROM_BUYER KPP_SALE_FROM_BUYER SUM_TABLE_SALE NDS_TABLE_SALE";*/

       // tableWithMistakes.schema() = sqlContext.createDataFrame(tableWithMistakes,schemaString);
        tableWithMistakes.write()
                .format("com.databricks.spark.csv")
                .option("inferSchema","true")
                .option("header","true")
                .option("charset", "UTF-8")
                .option("delimiter", ",")
                .option("quote", "\'")
                .mode(SaveMode.Overwrite).save(args[2] + "result.csv");

    }
}
