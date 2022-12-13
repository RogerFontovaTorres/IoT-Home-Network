package iot.cloudservice.database;

import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.InfluxDBClientFactory;
import com.influxdb.client.WriteApiBlocking;
import com.influxdb.client.domain.WritePrecision;
import com.influxdb.client.write.Point;
import iot.cloudservice.data.KafkaToDatabaseQueue;
import iot.cloudservice.data.MqttToDatabaseQueue;
import iot.cloudservice.database.entities.Temperature;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.time.Instant;

public class DatabaseController extends Thread {
    private final MqttToDatabaseQueue mqttQueue;
    private final KafkaToDatabaseQueue kafkaQueue;
    private final InfluxDBClient databaseClient;
    private final char[] token = "lrwBTKwzmF4PahbKT3yTd1-zWdK_zh5QiKL_hJlSEum3k-6sRMC7QSYHnn0qtGn9pqds7IJtcO5nHkCan15XZA==".toCharArray();

    private final String org = "iot-home-network";

    private final String bucket = "iot-bucket";



    public DatabaseController(MqttToDatabaseQueue mqttQueue, KafkaToDatabaseQueue kafkaQueue){
        this.mqttQueue = mqttQueue;
        this.kafkaQueue = kafkaQueue;
        this.databaseClient = InfluxDBClientFactory.create("http://localhost:8086", token, org, bucket);
    }

    public void run(){
        KafkaDatabase kafkaDatabase = new KafkaDatabase(this.kafkaQueue, this.databaseClient);
        MqttDatabase mqttDatabase = new MqttDatabase(this.mqttQueue, this.databaseClient);
        kafkaDatabase.start();
        mqttDatabase.start();
        try {
            kafkaDatabase.join();
            mqttDatabase.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
