package com.algaworks.algasensors.tempmonitoring.api.model;

import io.hypersistence.tsid.TSID;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SensorAlertOutput {

    private TSID id;

    private Double minTemperature;

    private Double maxTemperature;

}
