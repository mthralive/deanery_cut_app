package com.deanery.app.repository;

import com.deanery.app.model.LogInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface LogInfoRepository extends JpaRepository<LogInfo, UUID> {
    List<LogInfo> findAllByEntityIdOrderByDateDesc(UUID id);
}
