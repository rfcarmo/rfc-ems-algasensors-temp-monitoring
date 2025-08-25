package com.algaworks.algasensors.tempmonitoring.domain.repository;

import com.algaworks.algasensors.tempmonitoring.domain.model.SensorId;
import com.algaworks.algasensors.tempmonitoring.domain.model.SensorMonitoring;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SensorMonitoringRepository extends JpaRepository<SensorMonitoring, SensorId> {
}
