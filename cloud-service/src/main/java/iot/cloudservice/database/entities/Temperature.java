package iot.cloudservice.database.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.influxdb.annotations.Column;
import com.influxdb.annotations.Measurement;
import iot.cloudservice.data.MyInstantDeserializer;
import iot.cloudservice.data.MyInstantSerializer;

import java.time.Instant;

@Measurement(name = "temperature")
public class Temperature {

    @Column(tag = true)
    @JsonProperty("sensor_id")
    private String deviceId;
    @Column
    @JsonProperty("temperature")
    private Double value;

    @Column(timestamp = true)
    @JsonProperty("timestamp")
    @JsonDeserialize(using = MyInstantDeserializer.class)
    @JsonSerialize(using = MyInstantSerializer.class)
    public Instant time;

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    public Instant getTime() {
        return time;
    }

    public void setTime(Instant time) {
        this.time = time;
    }
}
