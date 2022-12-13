package iot.cloudservice;

import iot.cloudservice.controller.Controller;
import iot.cloudservice.data.KafkaToDatabaseQueue;
import iot.cloudservice.data.MqttToDatabaseQueue;
import iot.cloudservice.data.MqttToKafkaQueue;
import iot.cloudservice.database.DatabaseController;
import iot.cloudservice.kafkaconsumer.DataConsumer;
import iot.cloudservice.kafkaproducer.DataProducer;
import iot.cloudservice.mqttsubscriber.Subscriber;

public class Main {
    public static void main(String[] args) {
        System.out.println("Cloud service created!");
        MqttToKafkaQueue mqttToKafkaQueue = new MqttToKafkaQueue();
        MqttToDatabaseQueue mqttToDatabaseQueue = new MqttToDatabaseQueue();
        KafkaToDatabaseQueue kafkaToDatabaseQueue = new KafkaToDatabaseQueue();

        DataProducer producer = new DataProducer(mqttToKafkaQueue);
        Subscriber subscriber = new Subscriber("cloud-service", "tcp://localhost:1883", "Temperature", 0, mqttToKafkaQueue, mqttToDatabaseQueue);
        DataConsumer consumer = new DataConsumer(kafkaToDatabaseQueue);
        DatabaseController database = new DatabaseController(mqttToDatabaseQueue, kafkaToDatabaseQueue);

        Controller controller = new Controller(subscriber, producer, consumer, database);
        controller.start();
    }
}