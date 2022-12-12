package iot.cloudservice.mqttsubscriber;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import iot.cloudservice.data.MqttToDatabaseQueue;
import iot.cloudservice.data.MqttToKafkaQueue;
import iot.cloudservice.database.entities.Temperature;
import org.eclipse.paho.client.mqttv3.*;

public class Subscriber extends Thread {
    private MqttClient client;
    private final String clientId;
    private final String topic;
    private final String broker;
    private final int qos;

    final MqttToKafkaQueue kafkaQueue;
    final MqttToDatabaseQueue databaseQueue;
    private final ObjectMapper objectMapper = new ObjectMapper();

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
                sendMessageToQueues(mqttMessage);
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

    private void sendMessageToQueues(MqttMessage message){
        Temperature temperature = fromMqttMessageToTemperature(message);
        kafkaQueue.push(temperature);
        databaseQueue.push(temperature);
    }

    private Temperature fromMqttMessageToTemperature(MqttMessage message){
        try {
            String jsonMessage = new String(message.getPayload());
            return objectMapper.readValue(jsonMessage, Temperature.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

}
