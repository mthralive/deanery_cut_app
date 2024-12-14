package com.deanery.app;

import com.deanery.app.dto.RequestDto;
import com.deanery.app.error.exception.UserNotFoundException;
import com.deanery.app.error.exception.ValidationException;
import com.deanery.app.model.Enums.Gender;
import com.deanery.app.model.Enums.RequestStatus;
import com.deanery.app.model.Individual;
import com.deanery.app.model.Notification;
import com.deanery.app.model.Request;
import com.deanery.app.model.User;
import com.deanery.app.repository.NotificationRepository;
import com.deanery.app.repository.RequestRepository;
import com.deanery.app.service.RequestService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RequestServiceTest {

    @Mock
    private RequestRepository requestRepository;

    @Mock
    private NotificationRepository notificationRepository;

    @InjectMocks
    private RequestService requestService;

    private RequestDto requestDto;
    private Request request;
    private User user;
    private Individual individual;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        user = new User(UUID.randomUUID(), "John Doe");
        individual = new Individual(UUID.randomUUID(), 123, "Jane", "Doe", "Smith", LocalDate.of(1990, 1, 1), Gender.FEMALE, "City", "123-45-6789", "123-45-6789", "Address", "Actual Address", "jane.doe@example.com");
        request = new Request(UUID.randomUUID(), "Test request", LocalDate.now(), RequestStatus.SENDED, user, individual);
        requestDto = new RequestDto(new Request("Test request", user, individual));
    }

    @Test
    void testFind_RequestExists() {
        // Arrange
        when(requestRepository.findByEntityAndStatus(user.getId(), individual.getId())).thenReturn(request);

        // Act
        Request result = requestService.find(user.getId(), individual.getId());

        // Assert
        assertNotNull(result);
        assertEquals(request.getId(), result.getId());
    }

    @Test
    void testFind_RequestDoesNotExist() {
        // Arrange
        when(requestRepository.findByEntityAndStatus(user.getId(), individual.getId())).thenReturn(null);

        // Act
        Request result = requestService.find(user.getId(), individual.getId());

        // Assert
        assertNull(result);
    }


    @Test
    void testCreate_NewRequest() {
        // Arrange
        when(requestRepository.findByEntityAndStatus(user.getId(), individual.getId())).thenReturn(null);
        when(requestRepository.save(any(Request.class))).thenReturn(request);

        // Act
        Request result = requestService.create(requestDto);

        // Assert
        assertNotNull(result);
        assertEquals(requestDto.getRequest_text(), result.getRequest_text());
        verify(requestRepository).save(any(Request.class));
    }

    @Test
    void testUpdateStatus_SentToInWork() {
        // Arrange
        UUID id = request.getId();
        request.setStatus(RequestStatus.SENDED);
        when(requestRepository.findById(id)).thenReturn(Optional.of(request));
        when(requestRepository.save(any(Request.class))).thenReturn(request);

        // Act
        Request result = requestService.updateStatus(id);

        // Assert
        assertNotNull(result);
        assertEquals(RequestStatus.IN_WORK, result.getStatus());
        verify(notificationRepository).save(any(Notification.class));
    }

    @Test
    void testUpdateStatus_InWorkToCompleted() {
        // Arrange
        UUID id = request.getId();
        request.setStatus(RequestStatus.IN_WORK);
        when(requestRepository.findById(id)).thenReturn(Optional.of(request));
        when(requestRepository.save(any(Request.class))).thenReturn(request);

        // Act
        Request result = requestService.updateStatus(id);

        // Assert
        assertNotNull(result);
        assertEquals(RequestStatus.COMPLETED, result.getStatus());
        verify(notificationRepository).save(any(Notification.class));
    }

    @Test
    void testCancelRequest() {
        // Arrange
        UUID id = request.getId();
        when(requestRepository.findById(id)).thenReturn(Optional.of(request));
        when(requestRepository.save(any(Request.class))).thenReturn(request);

        // Act
        Request result = requestService.cancelRequest(id);

        // Assert
        assertNotNull(result);
        assertEquals(RequestStatus.CANCELED, result.getStatus());
        verify(notificationRepository).save(any(Notification.class));
    }

    @Test
    void testFindRequest_RequestExists() {
        // Arrange
        UUID id = request.getId();
        when(requestRepository.findById(id)).thenReturn(Optional.of(request));

        // Act
        Request result = requestService.findRequest(id);

        // Assert
        assertNotNull(result);
        assertEquals(request.getId(), result.getId());
    }

    @Test
    void testFindRequest_RequestNotFound() {
        // Arrange
        UUID id = UUID.randomUUID();
        when(requestRepository.findById(id)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(UserNotFoundException.class, () -> requestService.findRequest(id));
    }

    @Test
    void testFindAllRequest() {
        // Arrange
        List<Request> requests = List.of(request);
        when(requestRepository.findAll()).thenReturn(requests);

        // Act
        List<Request> result = requestService.findAllRequest();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void testFindAllRequestByStatus() {
        // Arrange
        RequestStatus status = RequestStatus.SENDED;
        List<Request> requests = List.of(request);
        when(requestRepository.findAllByStatusOrderByDateDesc(status)).thenReturn(requests);

        // Act
        List<Request> result = requestService.findAllRequestByStatus(status);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(status, result.get(0).getStatus());
    }

    @Test
    void testFindAllRequestByUser() {
        // Arrange
        List<Request> requests = List.of(request);
        when(requestRepository.findAllByUser_IdOrderByDateDesc(user.getId())).thenReturn(requests);

        // Act
        List<Request> result = requestService.findAllRequestByUser(user.getId());

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(user.getId(), result.get(0).getUser().getId());
    }
}

