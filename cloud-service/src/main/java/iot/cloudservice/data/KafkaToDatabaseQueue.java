package iot.cloudservice.data;

import iot.cloudservice.database.entities.TemperaturePrediction;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.util.concurrent.ConcurrentLinkedQueue;

public class KafkaToDatabaseQueue {
    // Data received from mqtt will be stored in this queue until kafka producer takes it (in order) and sends it to the AI agent
    private final ConcurrentLinkedQueue<TemperaturePrediction> kafkaToDatabaseQueue;

    public KafkaToDatabaseQueue(){
        this.kafkaToDatabaseQueue = new ConcurrentLinkedQueue<>();
    }

    public void push(TemperaturePrediction message){
        this.kafkaToDatabaseQueue.add(message);
        synchronized (this.kafkaToDatabaseQueue){
            this.kafkaToDatabaseQueue.notifyAll();
        }
    }

    public TemperaturePrediction poll(){
        if(this.isEmpty()){
            this.waitData();
        }
        return this.kafkaToDatabaseQueue.poll();
    }

    public boolean isEmpty(){
        return this.kafkaToDatabaseQueue.isEmpty();
    }

    // waits until it receives a notification. from poll push method
    private void waitData(){
        synchronized (this.kafkaToDatabaseQueue){
            try {
                this.kafkaToDatabaseQueue.wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
