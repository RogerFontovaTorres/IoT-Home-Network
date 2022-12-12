package iot.cloudservice.data;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import iot.cloudservice.database.entities.Temperature;
import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.Serializer;

import java.io.IOException;
import java.time.Instant;
import java.util.Map;

public class MyInstantSerializer extends JsonSerializer<Instant> {
    private final ObjectMapper serializer = new ObjectMapper();

    @Override
    public void serialize(Instant instant, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeNumber(instant.toEpochMilli());
    }
}


