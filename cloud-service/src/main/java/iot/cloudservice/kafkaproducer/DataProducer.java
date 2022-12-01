package iot.cloudservice.kafkaproducer;

import iot.cloudservice.data.MqttToKafkaQueue;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.util.Properties;

public class KafkaProducer extends Thread {
    private final Producer<String, String> producer;
    private final MqttToKafkaQueue queue;

    public KafkaProducer(MqttToKafkaQueue queue){
        Properties props = new Properties();
        props.put("bootstrap.servers", "localhost:9092");
        props.put("acks", "all");
        props.put("retries", 0);
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

        this.producer = new org.apache.kafka.clients.producer.KafkaProducer<>(props);
        this.queue = queue;
    }

    public void run(){
        System.out.println("Data Producer UP!");
        while(true){
            MqttMessage mqttMessage = queue.poll();
            ProducerRecord<String, String> producerMessage = new ProducerRecord<>("Temperature", new String(mqttMessage.getPayload()));

            this.producer.send(producerMessage);

            System.out.println("Producer: " + new String(mqttMessage.getPayload()));
            System.out.println("");
        }
    }
}