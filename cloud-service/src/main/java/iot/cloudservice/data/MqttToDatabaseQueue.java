package iot.cloudservice.data;

import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.util.concurrent.ConcurrentLinkedQueue;

public class MqttToDatabaseQueue {

    // Data received from mqtt will be stored in this queue until kafka producer takes it (in order) and sends it to the AI agent
    private final ConcurrentLinkedQueue<MqttMessage> mqttToDatabaseQueue;

    public MqttToDatabaseQueue(){
        this.mqttToDatabaseQueue = new ConcurrentLinkedQueue<>();
    }

    public void push(MqttMessage message){
        this.mqttToDatabaseQueue.add(message);
        synchronized (this.mqttToDatabaseQueue){
            this.mqttToDatabaseQueue.notifyAll();
        }
    }

    public MqttMessage poll(){
        if(this.isEmpty()){
            this.waitData();
        }
        return this.mqttToDatabaseQueue.poll();
    }

    public boolean isEmpty(){
        return this.mqttToDatabaseQueue.isEmpty();
    }

    private void waitData(){
        synchronized (this.mqttToDatabaseQueue){
            try {
                this.mqttToDatabaseQueue.wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
