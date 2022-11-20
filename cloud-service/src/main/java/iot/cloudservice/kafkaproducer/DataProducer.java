package iot.cloudservice.kafkaproducer;

import iot.cloudservice.data.MqttToKafkaQueue;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.util.Properties;
import java.util.concurrent.ConcurrentLinkedQueue;

public class DataProducer extends Thread {
    private Producer<String, String> producer;
    private MqttToKafkaQueue queue;

    public DataProducer(MqttToKafkaQueue queue){
        Properties props = new Properties();
        props.put("bootstrap.servers", "localhost:9092");
        props.put("acks", "all");
        props.put("retries", 0);
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

        //this.producer = new KafkaProducer<String, String>(props);
        this.queue = queue;
    }

    public void run(){
        System.out.println("Data Producer UP!");
        while(true){
            if(!queue.isEmpty()){
                MqttMessage mqttMessage = queue.poll();
                System.out.println("Producer: " + new String(mqttMessage.getPayload()));
                System.out.println("");
            }
        }
    }
}
