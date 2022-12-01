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
    private final char[] token;

    private final String org = "iothomenetwork";

    private final String bucket = "iot";



    public DatabaseController(MqttToDatabaseQueue mqttQueue, KafkaToDatabaseQueue kafkaQueue){
        this.mqttQueue = mqttQueue;
        this.kafkaQueue = kafkaQueue;
        this.token = "0V3x3iOC9wK-30NFucw93Tugp3_1DQ64GuV-WcjgSJJiWqNLfp7Obb0eT6GFajQsZzcMvyw5KZHmtlpGWsWz7A==".toCharArray();
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
