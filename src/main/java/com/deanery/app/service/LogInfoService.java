package com.deanery.app.service;

import com.deanery.app.model.LogInfo;
import com.deanery.app.model.User;
import com.deanery.app.repository.LogInfoRepository;
import groovy.util.logging.Slf4j;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class LogInfoService {
    private final LogInfoRepository logInfoRepository;

    @Transactional
    public LogInfo create(UUID entityId, String message, User user){
        return logInfoRepository.save(new LogInfo(null, entityId, message, LocalDateTime.now(), user));
    }

    @Transactional
    public List<LogInfo> getLogs(UUID id){
        return logInfoRepository.findAllByEntityIdOrderByDateDesc(id);
    }
}
