package iot.cloudservice.data;

import iot.cloudservice.database.entities.Temperature;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.util.concurrent.ConcurrentLinkedQueue;

public class MqttToKafkaQueue {

    // Data received from mqtt will be stored in this queue until kafka producer takes it (in order) and sends it to the AI agent
    private final ConcurrentLinkedQueue<Temperature> mqttToKafkaQueue;

    public MqttToKafkaQueue(){
        this.mqttToKafkaQueue = new ConcurrentLinkedQueue<>();
    }

    public void push(Temperature message){
        this.mqttToKafkaQueue.add(message);
        synchronized (this.mqttToKafkaQueue){
            this.mqttToKafkaQueue.notifyAll();
        }
    }

    public Temperature poll(){
        if(this.isEmpty()){
            this.waitData();
        }
        return this.mqttToKafkaQueue.poll();
    }

    public boolean isEmpty(){
        return this.mqttToKafkaQueue.isEmpty();
    }

    // waits until it receives a notification. from poll push method
    private void waitData(){
        synchronized (this.mqttToKafkaQueue){
            try {
                this.mqttToKafkaQueue.wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
