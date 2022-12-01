package iot.cloudservice.data;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import iot.cloudservice.database.entities.Temperature;
import iot.cloudservice.database.entities.TemperaturePrediction;
import net.razorvine.pickle.PickleUtils;
import net.razorvine.pickle.Pickler;
import org.apache.kafka.common.header.Headers;
import org.apache.kafka.common.serialization.Deserializer;

import java.io.IOException;
import java.util.Arrays;
import java.util.Map;

public class MyDeserializer implements Deserializer<TemperaturePrediction> {
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
