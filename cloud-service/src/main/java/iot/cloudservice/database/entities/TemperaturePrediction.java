package iot.cloudservice.database.entities;

import com.fasterxml.jackson.annotation.JsonKey;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.datatype.jsr310.deser.InstantDeserializer;
import com.influxdb.annotations.Column;
import com.influxdb.annotations.Measurement;
import iot.cloudservice.data.MyInstantDeserializer;
import lombok.*;

import java.time.Instant;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Measurement(name = "temperature-prediction")
public class TemperaturePrediction {

    @Column(timestamp = true)
    @JsonProperty("ds")
    @JsonDeserialize(using = MyInstantDeserializer.class)
    private Instant time;

    @JsonProperty("yhat")
    private double prediction;

    @Column
    @JsonProperty("yhat_lower")
    private double predictionLower;

    @Column
    @JsonProperty("yhat_upper")
    private double predictionUpper;

    @Column(tag = true)
    @JsonProperty("sensorId")
    private String sensorId;
}
