package com.i2i.zapcab.repository;

import com.i2i.zapcab.model.PendingRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PendingRequestRepository extends JpaRepository<PendingRequest, Integer> {
    @Query("SELECT pr from PendingRequest pr WHERE pr.status =:status")
    List<PendingRequest> findByStatus(@Param("status") String status);
    Optional<PendingRequest> findByMobileNumber(String phoneNumber);
}
