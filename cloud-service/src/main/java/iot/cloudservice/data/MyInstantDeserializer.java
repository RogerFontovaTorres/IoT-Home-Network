package iot.cloudservice.data;

import com.fasterxml.jackson.databind.ObjectMapper;
import iot.cloudservice.database.entities.TemperaturePrediction;
import org.apache.kafka.common.serialization.Deserializer;

import java.io.IOException;
import java.util.Map;

public class MyInstantDeserializer implements Deserializer<TemperaturePrediction> {
    ObjectMapper deserializer = new ObjectMapper();

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
        Deserializer.super.configure(configs, isKey);
    }

    @Override
    public TemperaturePrediction deserialize(String topic, byte[] data) {
        try {
            return deserializer.readValue(data, TemperaturePrediction.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
