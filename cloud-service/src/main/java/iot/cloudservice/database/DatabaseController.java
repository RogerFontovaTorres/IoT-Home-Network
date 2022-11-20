package iot.cloudservice.database;

import iot.cloudservice.data.KafkaToDatabaseQueue;
import iot.cloudservice.data.MqttToDatabaseQueue;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public class DatabaseController extends Thread {
    private final MqttToDatabaseQueue mqttQueue;
    private final KafkaToDatabaseQueue kafkaQueue;

    public DatabaseController(MqttToDatabaseQueue mqttQueue, KafkaToDatabaseQueue kafkaQueue){
        this.mqttQueue = mqttQueue;
        this.kafkaQueue = kafkaQueue;
    }

    public void run(){
        System.out.println("DatabaseController UP!");
        while(true){
            if(!mqttQueue.isEmpty()){
                MqttMessage message = mqttQueue.poll();
                System.out.println("DatabaseController: " + new String(message.getPayload()));
                // save message to database
            }
            if(!kafkaQueue.isEmpty()){
                System.out.println("DatabaseController: Kafka message saved");
                //save message to database
            }
        }
    }
}
