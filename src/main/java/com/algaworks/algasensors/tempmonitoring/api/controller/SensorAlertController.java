package com.algaworks.algasensors.tempmonitoring.api.controller;

import com.algaworks.algasensors.tempmonitoring.api.model.SensorAlertInput;
import com.algaworks.algasensors.tempmonitoring.api.model.SensorAlertOutput;
import com.algaworks.algasensors.tempmonitoring.domain.model.SensorAlert;
import com.algaworks.algasensors.tempmonitoring.domain.model.SensorId;
import com.algaworks.algasensors.tempmonitoring.domain.repository.SensorAlertRepository;
import io.hypersistence.tsid.TSID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/sensors/{sensorId}/alert")
@RequiredArgsConstructor
public class SensorAlertController {

    private final SensorAlertRepository sensorAlertRepository;

    @GetMapping
    public SensorAlertOutput getDetail(@PathVariable TSID sensorId) {
        SensorAlert sensorAlert = findById(sensorId);

        return SensorAlertOutput.builder()
                .id(sensorAlert.getId().getValue())
                .minTemperature(sensorAlert.getMinTemperature())
                .maxTemperature(sensorAlert.getMaxTemperature())
                .build();
    }

    @PutMapping
    public SensorAlertOutput createOrUpdate(@PathVariable TSID sensorId, @RequestBody SensorAlertInput input) {
        SensorAlert sensorAlert = findByIdOrDefault(sensorId);
        sensorAlert.setMinTemperature(input.getMinTemperature());
        sensorAlert.setMaxTemperature(input.getMaxTemperature());
        sensorAlertRepository.saveAndFlush(sensorAlert);

        return SensorAlertOutput.builder()
                .id(sensorAlert.getId().getValue())
                .minTemperature(sensorAlert.getMinTemperature())
                .maxTemperature(sensorAlert.getMaxTemperature())
                .build();
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable TSID sensorId) {
        SensorAlert sensorAlert = findById(sensorId);
        sensorAlertRepository.delete(sensorAlert);
    }

    private SensorAlert findById(TSID sensorId) {
        return sensorAlertRepository.findById(new SensorId(sensorId)).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Sensor alert not found"));
    }

    private SensorAlert findByIdOrDefault(TSID sensorId) {
        return sensorAlertRepository.findById(new SensorId(sensorId)).orElse(SensorAlert.builder()
                        .id(new SensorId(sensorId))
                        .minTemperature(null)
                        .maxTemperature(null)
                        .build());
    }

}
