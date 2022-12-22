package iot.cloudservice.database.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigInteger;
import java.time.Instant;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AnaliticsData implements Serializable{
    private float value;
    private Long time;
    private String sensorId;
}
