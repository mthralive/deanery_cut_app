package com.deanery.app.service;

import com.deanery.app.dto.RequestDto;
import com.deanery.app.error.exception.UserNotFoundException;
import com.deanery.app.model.Enums.RequestStatus;
import com.deanery.app.model.Individual;
import com.deanery.app.model.IndividualWorkPlan;
import com.deanery.app.model.Notification;
import com.deanery.app.model.Request;
import com.deanery.app.repository.NotificationRepository;
import com.deanery.app.repository.RequestRepository;
import groovy.util.logging.Slf4j;
import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class RequestService {
    private final RequestRepository requestRepository;
    private final NotificationRepository notificationRepository;

    @Transactional
    public Request find(UUID userId, UUID entId) {
        return requestRepository.findByEntityAndStatus(userId, entId);
    }

    @Transactional
    public Request create(RequestDto requestDto) {
        final Request r = find(requestDto.getUser().getId(), requestDto.getIndividual().getId());
        if(r != null){
            throw new ValidationException("Заявка уже была отправлена");
        }
        final Request req = new Request(null, requestDto.getRequest_text(),  LocalDate.now(), RequestStatus.SENDED,  requestDto.getUser(), requestDto.getIndividual());
        return requestRepository.save(req);
    }

    @Transactional(readOnly = true)
    public Request findRequest(UUID id) {
        final Optional<Request> base = requestRepository.findById(id);
        return base.orElseThrow(() -> new UserNotFoundException(id));
    }

    @Transactional
    public Request updateStatus(UUID id){
        final Request r = findRequest(id);
        if(r.getStatus() == RequestStatus.IN_WORK){
            r.setStatus(RequestStatus.COMPLETED);
            notificationRepository.save(new Notification(null, "Статус заявки был изменен на 'Выполнено'", false, r.getUser()));
        }
        if(r.getStatus() == RequestStatus.SENDED){
            r.setStatus(RequestStatus.IN_WORK);
            notificationRepository.save(new Notification(null, "Статус заявки был изменен на 'В работе'", false, r.getUser()));
        }
        return requestRepository.save(r);
    }

    @Transactional
    public Request cancelRequest(UUID id){
        final Request r = findRequest(id);
        r.setStatus(RequestStatus.CANCELED);
        notificationRepository.save(new Notification(null, "Заявка была отменена", false, r.getUser()));
        return requestRepository.save(r);
    }

    @Transactional(readOnly = true)
    public List<Request> findAllRequest() {
        return requestRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<Request> findAllRequestByStatus(RequestStatus status) {
        return requestRepository.findAllByStatusOrderByDateDesc(status);
    }

    @Transactional(readOnly = true)
    public List<Request> findAllRequestByUser(UUID id) {
        return requestRepository.findAllByUser_IdOrderByDateDesc(id);
    }
}
