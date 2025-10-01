package com.algaworks.algasensors.tempmonitoring.domain.service;

import com.algaworks.algasensors.tempmonitoring.api.model.TemperatureLogData;
import com.algaworks.algasensors.tempmonitoring.domain.model.SensorId;
import com.algaworks.algasensors.tempmonitoring.domain.model.SensorMonitoring;
import com.algaworks.algasensors.tempmonitoring.domain.model.TemperatureLog;
import com.algaworks.algasensors.tempmonitoring.domain.model.TemperatureLogId;
import com.algaworks.algasensors.tempmonitoring.domain.repository.SensorMonitoringRepository;
import com.algaworks.algasensors.tempmonitoring.domain.repository.TemperatureLogRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;

@Service
@Slf4j
@RequiredArgsConstructor
public class TemperatureMonitoringService {

    private final SensorMonitoringRepository sensorMonitoringRepository;

    private final TemperatureLogRepository temperatureLogRepository;

    @Transactional
    public void processTemperatureData(TemperatureLogData data) {
        sensorMonitoringRepository.findById(new SensorId(data.getSensorId()))
                .ifPresentOrElse(sensor -> handleSensorMonitoring(data, sensor), () -> logIgnoredTemperature(data));
    }

    private void handleSensorMonitoring(TemperatureLogData data, SensorMonitoring sensor) {
        if (sensor.isEnabled()) {
            sensor.setLastTemperature(data.getValue());
            sensor.setUpdatedAt(OffsetDateTime.now());

            sensorMonitoringRepository.save(sensor);

            TemperatureLog temperatureLog = TemperatureLog.builder()
                    .id(new TemperatureLogId(data.getId()))
                    .registeredAt(data.getRegisteredAt())
                    .value(data.getValue())
                    .sensorId(new SensorId(data.getSensorId()))
                    .build();

            temperatureLogRepository.save(temperatureLog);

            log.info("Updated temperature log from sensor {} value: {}", data.getSensorId(), data.getValue());
        } else {
            logIgnoredTemperature(data);
        }
    }

    private void logIgnoredTemperature(TemperatureLogData data) {
        log.info("Ignored temperature log from unregistered sensor {} value: {}", data.getSensorId(), data.getValue());
    }

}
