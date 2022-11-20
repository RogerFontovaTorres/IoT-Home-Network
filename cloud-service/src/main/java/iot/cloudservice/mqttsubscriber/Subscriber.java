package iot.cloudservice.mqttsubscriber;

import iot.cloudservice.data.MqttToDatabaseQueue;
import iot.cloudservice.data.MqttToKafkaQueue;
import org.eclipse.paho.client.mqttv3.*;

public class Subscriber extends Thread {
    private MqttClient client;
    private final String clientId;
    private final String topic;
    private final String broker;
    private final int qos;

    MqttToKafkaQueue kafkaQueue;
    MqttToDatabaseQueue databaseQueue;

    public Subscriber(String clientId, String broker, String topic, int qos, MqttToKafkaQueue kafkaQueue, MqttToDatabaseQueue databaseQueue){
        this.clientId = clientId;
        this.topic = topic;
        this.qos = qos;
        this.broker = broker;
        createClient();
        this.kafkaQueue = kafkaQueue;
        this.databaseQueue = databaseQueue;
    }

    private void createClient(){
        try {
            client = new MqttClient(broker, clientId);
        } catch (MqttException e) {
            throw new RuntimeException(e);
        }
    }

    public void run(){
        System.out.println("Subscriber UP!");
        client.setCallback(new MqttCallback() {
            @Override
            public void connectionLost(Throwable throwable) {
                System.out.println("Connection lost: " + throwable);
            }

            @Override
            public void messageArrived(String topic, MqttMessage mqttMessage) throws Exception {
                System.out.println("Subscriber: topic: " + topic);
                System.out.println("Subscriber: Qos: " + mqttMessage.getQos());
                System.out.println("Subscriber: message: " + new String(mqttMessage.getPayload()));
                System.out.println("");
                kafkaQueue.push(mqttMessage);
                databaseQueue.push(mqttMessage);
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {
                System.out.println("deliveryComplete---------" + token.isComplete());
            }
        });

        try {
            client.connect();
            client.subscribe(topic, qos);
        } catch (MqttException e) {
            throw new RuntimeException(e);
        }
    }
}
