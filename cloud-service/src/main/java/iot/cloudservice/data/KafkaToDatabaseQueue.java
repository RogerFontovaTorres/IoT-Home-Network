package iot.cloudservice.data;

import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.util.concurrent.ConcurrentLinkedQueue;

public class KafkaToDatabaseQueue {
    // Data received from mqtt will be stored in this queue until kafka producer takes it (in order) and sends it to the AI agent
    private ConcurrentLinkedQueue<MqttMessage> mqttToKafkaQueue;

    public KafkaToDatabaseQueue(){
        this.mqttToKafkaQueue = new ConcurrentLinkedQueue<>();
    }

    public void push(MqttMessage message){
        this.mqttToKafkaQueue.add(message);
    }

    public MqttMessage poll(){
        return this.mqttToKafkaQueue.poll();
    }

    public boolean isEmpty(){
        return this.mqttToKafkaQueue.isEmpty();
    }
}
