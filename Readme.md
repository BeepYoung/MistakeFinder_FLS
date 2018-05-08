To run container:\
docker-compose up --build

To log in to bash:\
docker-compose exec web /bin/bash

To run app with csv file by spark-submit:\
spark-submit --packages com.databricks:spark-csv_2.10:1.5.0 testApp.jar /path/To/File