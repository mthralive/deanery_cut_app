package com.deanery.app;

import com.deanery.app.dto.RequestDto;
import com.deanery.app.model.Enums.Gender;
import com.deanery.app.model.Enums.RequestStatus;
import com.deanery.app.model.Enums.UserRole;
import com.deanery.app.model.Individual;
import com.deanery.app.model.Notification;
import com.deanery.app.model.Request;
import com.deanery.app.model.User;
import com.deanery.app.repository.IndividualRepository;
import com.deanery.app.repository.NotificationRepository;
import com.deanery.app.repository.RequestRepository;
import com.deanery.app.repository.UserRepository;
import com.deanery.app.service.RequestService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class RequestServiceH2Test {

    @Autowired
    private RequestRepository requestRepository;

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private IndividualRepository individualRepository;

    @Autowired
    private RequestService requestService;

    private User user;
    private Individual individual;
    private RequestDto requestDto;
    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        // Initialize test data
        individual = individualRepository.save(new Individual(
                UUID.randomUUID(),
                123,
                "Jane",
                "Doe",
                "Smith",
                LocalDate.of(1990, 1, 1),
                Gender.FEMALE,
                "City",
                "123-45-6789",
                "123-45-6789",
                "Address",
                "Actual Address",
                "jane.doe@example.com"
        ));
        user = userRepository.save(new User(UUID.randomUUID(), "jane.doe@example.com", "12345678", individual, UserRole.ADMIN, 1000, true));

        requestDto = new RequestDto(new Request(null, "Test request",LocalDate.now(), RequestStatus.SENDED, user, individual));
    }

    @Test
    void testCreateRequest() {
        // Act
        Request createdRequest = requestService.create(requestDto);

        // Assert
        assertNotNull(createdRequest);
        assertEquals(requestDto.getRequest_text(), createdRequest.getRequest_text());
        assertEquals(RequestStatus.SENDED, createdRequest.getStatus());
    }

    @Test
    void testFindRequest() {
        // Arrange
        Request createdRequest = requestService.create(requestDto);

        // Act
        Request foundRequest = requestService.findRequest(createdRequest.getId());

        // Assert
        assertNotNull(foundRequest);
        assertEquals(createdRequest.getId(), foundRequest.getId());
    }

    @Test
    void testUpdateStatus_SentToInWork() {
        // Arrange
        Request createdRequest = requestService.create(requestDto);

        // Act
        Request updatedRequest = requestService.updateStatus(createdRequest.getId());

        // Assert
        assertNotNull(updatedRequest);
        assertEquals(RequestStatus.IN_WORK, updatedRequest.getStatus());

        // Check notification
        List<Notification> notifications = notificationRepository.findAll();
        assertEquals(1, notifications.size());
        assertEquals("Статус заявки был изменен на 'В работе'", notifications.get(0).getText());
    }

    @Test
    void testCancelRequest() {
        // Arrange
        Request createdRequest = requestService.create(requestDto);

        // Act
        Request canceledRequest = requestService.cancelRequest(createdRequest.getId());

        // Assert
        assertNotNull(canceledRequest);
        assertEquals(RequestStatus.CANCELED, canceledRequest.getStatus());

        // Check notification
        List<Notification> notifications = notificationRepository.findAll();
        assertEquals(1, notifications.size());
        assertEquals("Заявка была отменена", notifications.get(0).getText());
    }

    @Test
    void testFindAllRequestsByStatus() {
        // Arrange
        Request createdRequest = requestService.create(requestDto);

        // Act
        List<Request> requests = requestService.findAllRequestByStatus(RequestStatus.SENDED);

        // Assert
        assertEquals(1, requests.size());
        assertEquals(createdRequest.getId(), requests.get(0).getId());
    }

    @Test
    void testFindAllRequestsByUser() {
        // Arrange
        Request createdRequest = requestService.create(requestDto);

        // Act
        List<Request> requests = requestService.findAllRequestByUser(user.getId());

        // Assert
        assertEquals(1, requests.size());
        assertEquals(createdRequest.getId(), requests.get(0).getId());
    }
}

