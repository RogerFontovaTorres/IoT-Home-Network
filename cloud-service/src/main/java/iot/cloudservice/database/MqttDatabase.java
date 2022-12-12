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
            Temperature message = queue.poll();
            System.out.println("DatabaseController: " + message.getValue());
            saveTemperature(message);
        }
    }
    public void saveTemperature(Temperature temperature){
        WriteApiBlocking writeApi = databaseClient.getWriteApiBlocking();
        writeApi.writeMeasurement(WritePrecision.NS,temperature);
        System.out.println("Temperature saved!");
    }

}
