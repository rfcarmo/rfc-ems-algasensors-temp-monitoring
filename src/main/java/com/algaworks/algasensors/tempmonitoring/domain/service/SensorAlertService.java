package com.algaworks.algasensors.tempmonitoring.domain.service;

import com.algaworks.algasensors.tempmonitoring.api.model.TemperatureLogData;
import com.algaworks.algasensors.tempmonitoring.domain.model.SensorId;
import com.algaworks.algasensors.tempmonitoring.domain.repository.SensorAlertRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class SensorAlertService {

    private final SensorAlertRepository sensorAlertRepository;

    @Transactional
    public void handleAlert(TemperatureLogData data) {
        sensorAlertRepository.findById(new SensorId(data.getSensorId()))
                .ifPresentOrElse(alert -> {
                    if ((alert.getMaxTemperature() != null) && (data.getValue().compareTo(alert.getMaxTemperature()) >= 0)) {
                        log.info("Alert Max Temp. received: sensorId={}, temperature={}", data.getSensorId(), data.getValue());
                    } else if ((alert.getMinTemperature() != null) && (data.getValue().compareTo(alert.getMinTemperature()) <= 0)) {
                        log.info("Alert Min Temp. received: sensorId={}, temperature={}", data.getSensorId(), data.getValue());
                    } else {
                        logIgnoredTemperature(data);
                    }
                }, () -> logIgnoredTemperature(data));
    }

    private void logIgnoredTemperature(TemperatureLogData data) {
        log.info("Ignored temperature log from unregistered sensor {} value: {}", data.getSensorId(), data.getValue());
    }

}
