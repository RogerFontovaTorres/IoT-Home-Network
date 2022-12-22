package iot.cloudservice.kafkaproducer;

import iot.cloudservice.data.TemperatureSerializer;
import iot.cloudservice.data.PasiveWaitQueue;
import iot.cloudservice.database.entities.Temperature;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;

public class DataProducer extends Thread {
    private final Producer<String, Temperature> producer;
    private final PasiveWaitQueue<Temperature> queue;

    public DataProducer(PasiveWaitQueue<Temperature> queue){
        Properties props = new Properties();
        props.put("bootstrap.servers", "localhost:9092"); // Set the broker server
        props.put("acks", "all");
        props.put("retries", 0);
        props.put("key.serializer", StringSerializer.class.getName()); // set how to serialize key
        props.put("value.serializer", TemperatureSerializer.class); // set how to serialize value

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
