package iot.cloudservice.database;

import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.WriteApiBlocking;
import com.influxdb.client.domain.WritePrecision;
import iot.cloudservice.data.KafkaToDatabaseQueue;
import iot.cloudservice.database.entities.TemperaturePrediction;
import org.apache.kafka.clients.consumer.ConsumerRecord;

import java.time.Instant;

public class KafkaDatabase extends Thread{
    private final KafkaToDatabaseQueue queue;

    private final InfluxDBClient databaseClient;
    public KafkaDatabase(KafkaToDatabaseQueue queue, InfluxDBClient databaseClient){
        this.queue = queue;
        this.databaseClient = databaseClient;
    }

    public void run(){
        System.out.println("Kafka Database Controller UP!");
        while(true){
            ConsumerRecord<String, TemperaturePrediction> record = queue.poll();
            System.out.println("\n\n\nHERE\n\n\n");
            saveTemperaturePrediction(record);

        }

    }

    public void saveTemperaturePrediction(ConsumerRecord<String, TemperaturePrediction> message){
        WriteApiBlocking writeApi = databaseClient.getWriteApiBlocking();
        writeApi.writeMeasurement(WritePrecision.NS,message.value());
        System.out.println("Temperature prediction saved!");
    }
}
