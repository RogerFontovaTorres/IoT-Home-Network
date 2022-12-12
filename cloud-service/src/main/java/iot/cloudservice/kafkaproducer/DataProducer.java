package iot.cloudservice.kafkaproducer;

import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import iot.cloudservice.data.MqttToKafkaQueue;
import iot.cloudservice.data.MySerializer;
import iot.cloudservice.database.entities.AnaliticsData;
import iot.cloudservice.database.entities.Temperature;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.DoubleSerializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.math.BigInteger;
import java.time.Instant;
import java.util.Date;
import java.util.Properties;

public class DataProducer extends Thread {
    private final Producer<String, Temperature> producer;
    private final MqttToKafkaQueue queue;

    public DataProducer(MqttToKafkaQueue queue){
        Properties props = new Properties();
        props.put("bootstrap.servers", "localhost:9092"); // Set the broker server
        props.put("acks", "all");
        props.put("retries", 0);
        props.put("key.serializer", StringSerializer.class.getName()); // set how to serialize key
        //props.put("value.serializer", PickleSerializer.class.getName()); // set how to serialize value
        //props.put("key.serializer", StringSerializer.class.getName()); // set how to serialize key
        props.put("value.serializer", MySerializer.class); // set how to serialize value

        this.producer = new KafkaProducer<>(props);
        this.queue = queue;
    }

    public void run(){
        System.out.println("Data Producer UP!");
        while(true){
            Temperature message = queue.poll();
            ProducerRecord<String, Temperature> producerMessage = new ProducerRecord<>("temperature", message);

            this.producer.send(producerMessage);

            System.out.println("Producer: " + message.getValue());
            System.out.println("");
        }
    }
}
