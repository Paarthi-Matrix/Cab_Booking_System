package com.i2i.zapcab.repository;

import com.i2i.zapcab.model.PendingRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static com.i2i.zapcab.common.ZapCabConstant.FIND_BY_STATUS_QUERY;

@Repository
public interface PendingRequestRepository extends JpaRepository<PendingRequest, Integer> {
    @Query(FIND_BY_STATUS_QUERY)
    List<PendingRequest> findByStatus(@Param("status") String status);
    Optional<PendingRequest> findByMobileNumber(String phoneNumber);
}
