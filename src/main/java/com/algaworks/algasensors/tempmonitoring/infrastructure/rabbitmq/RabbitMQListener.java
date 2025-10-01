package com.algaworks.algasensors.tempmonitoring.infrastructure.rabbitmq;

import com.algaworks.algasensors.tempmonitoring.api.model.TemperatureLogData;
import com.algaworks.algasensors.tempmonitoring.domain.service.SensorAlertService;
import com.algaworks.algasensors.tempmonitoring.domain.service.TemperatureMonitoringService;
import io.hypersistence.tsid.TSID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.util.Map;

import static com.algaworks.algasensors.tempmonitoring.infrastructure.rabbitmq.RabbitMQConfig.QUEUE_ALERTING;
import static com.algaworks.algasensors.tempmonitoring.infrastructure.rabbitmq.RabbitMQConfig.QUEUE_PROCESS_TEMPERATURE;

@Slf4j
@Component
@RequiredArgsConstructor
public class RabbitMQListener {

    private final TemperatureMonitoringService temperatureMonitoringService;

    private SensorAlertService sensorAlertService;

    @RabbitListener(queues = QUEUE_PROCESS_TEMPERATURE, concurrency = "2-3")
    public void handleProcessTemperature(@Payload TemperatureLogData temperatureLogData, @Headers Map<String, Object> headers) {
        TSID sensorId = temperatureLogData.getSensorId();
        Double temperature = temperatureLogData.getValue();

        log.info("Temperature log received: sensorId={}, temperature={}", sensorId, temperature);
        // log.info("Headers: {}", headers.toString());

        temperatureMonitoringService.processTemperatureData(temperatureLogData);
    }

    @RabbitListener(queues = QUEUE_ALERTING, concurrency = "2-3")
    public void handleAlerting(@Payload TemperatureLogData temperatureLogData, @Headers Map<String, Object> headers) {
        sensorAlertService.handleAlert(temperatureLogData);
    }

}
