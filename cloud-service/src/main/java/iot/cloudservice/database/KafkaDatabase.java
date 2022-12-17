package iot.cloudservice.database;

import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.WriteApiBlocking;
import com.influxdb.client.domain.WritePrecision;
import iot.cloudservice.data.PasiveWaitQueue;
import iot.cloudservice.database.entities.TemperaturePrediction;

public class KafkaDatabase extends Thread{
    private final PasiveWaitQueue<TemperaturePrediction> queue;

    private final InfluxDBClient databaseClient;
    public KafkaDatabase(PasiveWaitQueue<TemperaturePrediction> queue, InfluxDBClient databaseClient){
        this.queue = queue;
        this.databaseClient = databaseClient;
    }

    public void run(){
        System.out.println("Kafka Database Controller UP!");
        while(true){
            TemperaturePrediction message = queue.poll();
            saveTemperaturePrediction(message);

        }

    }

    public void saveTemperaturePrediction(TemperaturePrediction message){
        WriteApiBlocking writeApi = databaseClient.getWriteApiBlocking();
        writeApi.writeMeasurement(WritePrecision.NS,message);
        System.out.println("Temperature prediction saved!");
    }
}
