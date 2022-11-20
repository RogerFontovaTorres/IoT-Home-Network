package iot.cloudservice;

import iot.cloudservice.controller.Controller;
import iot.cloudservice.data.MqttToKafkaQueue;
import iot.cloudservice.kafkaproducer.DataProducer;
import iot.cloudservice.mqttsubscriber.Subscriber;

public class Main {
    public static void main(String[] args) {
        MqttToKafkaQueue queue = new MqttToKafkaQueue();
        Subscriber subscriber = new Subscriber("cloud-service", "tcp://localhost:1883", "Temperature", 0, queue);
        DataProducer producer = new DataProducer(queue);
        System.out.println("Cloud service created!");
        Controller controller = new Controller(subscriber, producer);
        controller.start();
    }
}