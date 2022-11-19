package iot.cloudservice.mqttsubscriber;

import org.eclipse.paho.client.mqttv3.*;

public class Subscriber {
    MqttClient client;
    String clientId;
    String topic;
    String broker;
    int qos;

    public Subscriber(String clientId, String broker, String topic, int qos){
        this.clientId = clientId;
        this.topic = topic;
        this.qos = qos;
        this.broker = broker;
        createClient();
    }

    private void createClient(){
        try {
            client = new MqttClient(broker, clientId);
        } catch (MqttException e) {
            throw new RuntimeException(e);
        }
    }

    public void start(){
        client.setCallback(new MqttCallback() {
            @Override
            public void connectionLost(Throwable throwable) {
                System.out.println("Connection lost: " + throwable);
            }

            @Override
            public void messageArrived(String topic, MqttMessage mqttMessage) throws Exception {
                System.out.println("topic: " + topic);
                System.out.println("Qos: " + mqttMessage.getQos());
                System.out.println("message: " + new String(mqttMessage.getPayload()));
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
