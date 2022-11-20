package iot.cloudservice.controller;

import iot.cloudservice.kafkaproducer.DataProducer;
import iot.cloudservice.mqttsubscriber.Subscriber;

import java.util.concurrent.ConcurrentLinkedQueue;

public class Controller {
    private Subscriber subscriber;
    private DataProducer producer;


    public Controller(Subscriber subscriber, DataProducer producer){
        this.subscriber = subscriber;
        this.producer = producer;
    }

    public void start(){
        this.subscriber.start();
        this.producer.start();
    }
}
