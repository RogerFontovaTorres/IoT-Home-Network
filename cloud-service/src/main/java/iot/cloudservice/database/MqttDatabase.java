package iot.cloudservice.database;

import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.WriteApiBlocking;
import com.influxdb.client.domain.WritePrecision;
import iot.cloudservice.data.KafkaToDatabaseQueue;
import iot.cloudservice.data.MqttToDatabaseQueue;
import iot.cloudservice.database.entities.Temperature;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.time.Instant;

public class MqttDatabase extends Thread {
    private final MqttToDatabaseQueue queue;

    private final InfluxDBClient databaseClient;
    public MqttDatabase(MqttToDatabaseQueue queue, InfluxDBClient databaseClient) {
        this.queue = queue;
        this.databaseClient = databaseClient;
    }



    public void run(){
        System.out.println("Mqtt Database Controller UP!");
        while(true){
            MqttMessage message = queue.poll();
            String temperature = new String(message.getPayload());
            System.out.println("DatabaseController: " + temperature);
            saveTemperature(message);
        }
    }
    public void saveTemperature(MqttMessage message){
        WriteApiBlocking writeApi = databaseClient.getWriteApiBlocking();
        Double value = Double.valueOf(new String(message.getPayload()));
        Temperature temperature = new Temperature();
        temperature.setLocation("home");
        temperature.setValue(value);
        temperature.setTime(Instant.now());
        writeApi.writeMeasurement(WritePrecision.NS,temperature);
        System.out.println("Temperature saved!");
    }

}
