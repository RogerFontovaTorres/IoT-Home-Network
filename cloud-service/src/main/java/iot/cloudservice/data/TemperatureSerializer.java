package iot.cloudservice.data;

import com.fasterxml.jackson.databind.ObjectMapper;
import iot.cloudservice.database.entities.Temperature;
import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.Serializer;

import java.util.Map;

public class TemperatureSerializer implements Serializer<Temperature> {
    private final ObjectMapper serializer = new ObjectMapper();

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
    }

    @Override
    public byte[] serialize(String topic, Temperature data) {
        try {

            if (data == null){
                System.out.println("Null received at serializing");
                return null;
            }
            return serializer.writeValueAsBytes(data);
            // return pickler.dumps(data);
        } catch (Exception e) {
            throw new SerializationException("Error when serializing AnaliticsData to byte[]: " + e);
        }
    }

    @Override
    public void close() {
    }
}


