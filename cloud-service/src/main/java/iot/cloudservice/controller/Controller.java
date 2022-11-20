package iot.cloudservice.controller;

import iot.cloudservice.database.DatabaseController;
import iot.cloudservice.kafkaproducer.DataProducer;
import iot.cloudservice.mqttsubscriber.Subscriber;

import java.util.concurrent.ConcurrentLinkedQueue;

public class Controller {
    private final Subscriber subscriber;
    private final DataProducer producer;
    private final DatabaseController database;


    public Controller(Subscriber subscriber, DataProducer producer, DatabaseController database){
        this.subscriber = subscriber;
        this.producer = producer;
        this.database = database;
    }

    // Each class is started in a different thread
    public void start(){
        this.subscriber.start();
        this.producer.start();
        this.database.start();
    }
}
