package iot.cloudservice;

import iot.cloudservice.mqttsubscriber.Subscriber;

public class Main {
    public static void main(String[] args) {
        System.out.println("Cloud service created!");
        Subscriber subscriber = new Subscriber("cloud-service", "tcp://localhost:1883", "Temperature", 0);
        subscriber.start();
    }
}