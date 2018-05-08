


import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.DataFrame;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.SQLContext;
import scala.Tuple2;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class HelloWorld {
    public  static void main(String args[]){
        SparkConf conf = new SparkConf().setAppName("someApp").setMaster("local");
        JavaSparkContext sc = new JavaSparkContext(conf);
        Integer NUM_SAMPLES = 100;
        List<Integer> l = new ArrayList<>(NUM_SAMPLES);
        for (int i = 0; i < NUM_SAMPLES; i++) {
            l.add(i);
        }

        long count = sc.parallelize(l).filter(i -> {
            double x = Math.random();
            double y = Math.random();
            return x*x + y*y < 1;
        }).count();
        System.out.println("Pi is roughly " + 4.0 * count / NUM_SAMPLES);
        int inChar;
        System.out.println("Enter a Character:");
        try {
            inChar = System.in.read();
            System.out.print("You entered ");
            System.out.println(inChar);
        }
        catch (IOException e){
            System.out.println("Error reading from user");
        }

    }
}
