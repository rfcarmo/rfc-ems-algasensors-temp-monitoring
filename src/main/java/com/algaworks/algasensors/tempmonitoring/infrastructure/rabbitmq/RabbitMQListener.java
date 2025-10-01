package com.algaworks.algasensors.tempmonitoring.infrastructure.rabbitmq;

import com.algaworks.algasensors.tempmonitoring.api.model.TemperatureLogData;
import com.algaworks.algasensors.tempmonitoring.domain.service.TemperatureMonitoringService;
import io.hypersistence.tsid.TSID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.util.Map;

import static com.algaworks.algasensors.tempmonitoring.infrastructure.rabbitmq.RabbitMQConfig.QUEUE;

@Slf4j
@Component
@RequiredArgsConstructor
public class RabbitMQListener {

    private final TemperatureMonitoringService temperatureMonitoringService;

    @RabbitListener(queues = QUEUE)
    public void handle(@Payload TemperatureLogData temperatureLogData, @Headers Map<String, Object> headers) {
        TSID sensorId = temperatureLogData.getSensorId();
        Double temperature = temperatureLogData.getValue();

        log.info("Temperature log received: sensorId={}, temperature={}", sensorId, temperature);
        log.info("Headers: {}", headers.toString());

        temperatureMonitoringService.processTemperatureData(temperatureLogData);
    }

}
