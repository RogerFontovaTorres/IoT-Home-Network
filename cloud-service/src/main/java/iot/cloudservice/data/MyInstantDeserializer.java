package iot.cloudservice.data;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import iot.cloudservice.database.entities.TemperaturePrediction;
import org.apache.kafka.common.serialization.Deserializer;

import java.io.IOException;
import java.time.Instant;
import java.util.Map;

public class MyInstantDeserializer extends JsonDeserializer<Instant> {

    ObjectMapper deserializer = new ObjectMapper();

    @Override
    public Instant deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        return Instant.ofEpochMilli(jsonParser.getLongValue());
    }
}
