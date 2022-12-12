package iot.cloudservice.kafkaconsumer;

import iot.cloudservice.data.KafkaToDatabaseQueue;
import iot.cloudservice.data.MqttToKafkaQueue;
import iot.cloudservice.data.MyDeserializer;
import iot.cloudservice.database.entities.TemperaturePrediction;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.time.Duration;
import java.time.Instant;
import java.util.Collections;
import java.util.Properties;

public class DataConsumer extends Thread {
    private final KafkaConsumer<String, TemperaturePrediction> consumer;
    private final KafkaToDatabaseQueue queue;

    public DataConsumer(KafkaToDatabaseQueue queue){
        this.queue = queue;
        Properties props = new Properties();
        props.put("bootstrap.servers", "localhost:9092");
        props.setProperty("group.id", "predictions-results");
        // Set how to serialize key/value pairs
        props.setProperty("key.deserializer","org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer", MyDeserializer.class);
        // When a group is first created, it has no offset stored to start reading from. This tells it to start
        // with the earliest record in the stream.
        props.setProperty("auto.offset.reset","earliest");
        consumer = new KafkaConsumer<>(props);
        consumer.subscribe(Collections.singletonList("analytics_results"));
    }

    public void start(){
        System.out.println("Data Consumer UP!");
        while(true){
            ConsumerRecords<String, TemperaturePrediction> records = consumer.poll(Duration.ofMillis(100));

            for(ConsumerRecord<String, TemperaturePrediction> record : records){
                queue.push(record.value());
                System.out.println("Consumer: ");
                System.out.println("        Topic: " + record.topic());
                System.out.println("        Key: " + record.key());
                System.out.println("        Value: " + record.value().getTime());
                System.out.println("        Instant: " + Instant.now());
                System.out.println("        Instant: " + Instant.now().toEpochMilli());

                System.out.println("");
            }

        }
    }
}
