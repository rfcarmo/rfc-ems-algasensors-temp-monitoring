package com.algaworks.algasensors.tempmonitoring.api.model;

import lombok.Data;

@Data
public class SensorAlertInput {

    private Double minTemperature;

    private Double maxTemperature;

}
