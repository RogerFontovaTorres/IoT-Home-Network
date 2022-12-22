package iot.cloudservice.controller;

import iot.cloudservice.database.DatabaseController;
import iot.cloudservice.kafkaconsumer.DataConsumer;
import iot.cloudservice.kafkaproducer.DataProducer;
import iot.cloudservice.mqttsubscriber.Subscriber;

public class Controller {
    private final Subscriber subscriber;
    private final DataProducer producer;
    private final DataConsumer consumer;
    private final DatabaseController database;


    public Controller(Subscriber subscriber, DataProducer producer, DataConsumer consumer, DatabaseController database){
        this.subscriber = subscriber;
        this.producer = producer;
        this.consumer = consumer;
        this.database = database;
    }

    // Each class is started in a different thread
    public void start(){
        this.database.start();
        this.subscriber.start();
        this.producer.start();
        this.consumer.start();

        try {
            this.subscriber.join();
            this.producer.join();
            this.consumer.join();
            this.database.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
