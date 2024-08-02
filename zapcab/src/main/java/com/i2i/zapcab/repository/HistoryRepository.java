package com.i2i.zapcab.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.i2i.zapcab.model.RideHistory;

import static com.i2i.zapcab.common.ZapCabConstant.COUNT_BY_USER_ID;

@Repository
public interface HistoryRepository extends JpaRepository<RideHistory, String> {
    List<RideHistory> findAllByUserId(String id);

    @Query(COUNT_BY_USER_ID)
    int countByUserId(@Param("userId") String userId);
}
