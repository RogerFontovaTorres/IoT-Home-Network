package iot.cloudservice.database;

import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.InfluxDBClientFactory;
import iot.cloudservice.data.PasiveWaitQueue;
import iot.cloudservice.database.entities.Temperature;
import iot.cloudservice.database.entities.TemperaturePrediction;

public class DatabaseController extends Thread {
    private final PasiveWaitQueue<Temperature> mqttQueue;
    private final PasiveWaitQueue<TemperaturePrediction> kafkaQueue;
    private final InfluxDBClient databaseClient;
    private final char[] token = "asdf12341234asdf".toCharArray();

    private final String org = "iot-home-network";

    private final String bucket = "iot-bucket";



    public DatabaseController(PasiveWaitQueue<Temperature> mqttQueue, PasiveWaitQueue<TemperaturePrediction> kafkaQueue){
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
