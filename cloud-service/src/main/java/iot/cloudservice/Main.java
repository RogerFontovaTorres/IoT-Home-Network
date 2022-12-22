package iot.cloudservice;

import iot.cloudservice.controller.Controller;
import iot.cloudservice.data.PasiveWaitQueue;
import iot.cloudservice.database.DatabaseController;
import iot.cloudservice.database.entities.Temperature;
import iot.cloudservice.database.entities.TemperaturePrediction;
import iot.cloudservice.kafkaconsumer.DataConsumer;
import iot.cloudservice.kafkaproducer.DataProducer;
import iot.cloudservice.mqttsubscriber.Subscriber;

public class Main {
    public static void main(String[] args) {
        System.out.println("Cloud service created!");

        PasiveWaitQueue<Temperature> mqttToKafkaQueue = new PasiveWaitQueue<>();
        PasiveWaitQueue<Temperature> mqttToDatabaseQueue = new PasiveWaitQueue<>();
        PasiveWaitQueue<TemperaturePrediction> kafkaToDatabaseQueue = new PasiveWaitQueue<>();

        DataProducer producer = new DataProducer(mqttToKafkaQueue);
        Subscriber subscriber = new Subscriber("cloud-service", "tcp://localhost:1883", "Temperature", 0, mqttToKafkaQueue, mqttToDatabaseQueue);
        DataConsumer consumer = new DataConsumer(kafkaToDatabaseQueue);
        DatabaseController database = new DatabaseController(mqttToDatabaseQueue, kafkaToDatabaseQueue);

        Controller controller = new Controller(subscriber, producer, consumer, database);
        controller.start();
    }
}