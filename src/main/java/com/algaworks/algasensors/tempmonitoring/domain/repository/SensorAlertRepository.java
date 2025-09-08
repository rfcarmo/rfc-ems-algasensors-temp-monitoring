package com.algaworks.algasensors.tempmonitoring.domain.repository;

import com.algaworks.algasensors.tempmonitoring.domain.model.SensorAlert;
import com.algaworks.algasensors.tempmonitoring.domain.model.SensorId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SensorAlertRepository extends JpaRepository<SensorAlert, SensorId> {
}
