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

    private final String org = "IoTHomeNetwork";

    private final String bucket = "iothome";



    public DatabaseController(MqttToDatabaseQueue mqttQueue, KafkaToDatabaseQueue kafkaQueue){
        this.mqttQueue = mqttQueue;
        this.kafkaQueue = kafkaQueue;
        this.token = "huY3H2UN2lvshdmn7bOEoGtqRHqVA00o8VN2OI_K0GxK14ClVsZdnaigwsuniPfyXaLzzz1BvZJg2RMQgzb5MA==".toCharArray();
        this.databaseClient = InfluxDBClientFactory.create("http://localhost:8086", token, org, bucket);
    }

    public void run(){
        System.out.println("DatabaseController UP!");
        while(true){
            MqttMessage message = mqttQueue.poll();
            String temperature = new String(message.getPayload());
            System.out.println("DatabaseController: " + temperature);
            saveTemperature(message);

            /*
            // it will run on another thread
            if(!kafkaQueue.isEmpty()){
                System.out.println("DatabaseController: Kafka message saved");
                //save message to database
            }
            */
        }
    }

    public void saveTemperature(MqttMessage message){
        WriteApiBlocking writeApi = databaseClient.getWriteApiBlocking();
        String value = new String(message.getPayload());
        Temperature temperature = new Temperature();
        temperature.setLocation("home");
        temperature.setValue(value);
        temperature.setTime(Instant.now());
        writeApi.writeMeasurement(WritePrecision.NS,temperature);
        System.out.println("Temperature saved!");
    }
}
