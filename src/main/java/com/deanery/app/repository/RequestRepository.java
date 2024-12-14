package com.deanery.app.repository;

import com.deanery.app.model.Enums.RequestStatus;
import com.deanery.app.model.Request;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface RequestRepository extends JpaRepository<Request, UUID> {
    @Query("select r from request r where r.user.id = :userId and r.individual.id = :entityId and r.status != 'COMPLETED' and r.status != 'CANCELED'")
    Request findByEntityAndStatus(@Param("userId") UUID userId, @Param("entityId") UUID entId);

    List<Request> findAllByStatusOrderByDateDesc(RequestStatus status);

    List<Request> findAllByUser_IdOrderByDateDesc(UUID user_id);
}
