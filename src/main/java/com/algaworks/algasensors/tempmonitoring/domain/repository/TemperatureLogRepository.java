package com.algaworks.algasensors.tempmonitoring.domain.repository;

import com.algaworks.algasensors.tempmonitoring.domain.model.SensorId;
import com.algaworks.algasensors.tempmonitoring.domain.model.TemperatureLog;
import com.algaworks.algasensors.tempmonitoring.domain.model.TemperatureLogId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TemperatureLogRepository extends JpaRepository<TemperatureLog, TemperatureLogId> {
    Page<TemperatureLog> findAllBySensorId(SensorId sensorId, Pageable pageable);
}
