package com.i2i.zapcab.repository;

import com.i2i.zapcab.model.History;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HistoryRepository extends JpaRepository<History,String> {
    List<History> findAllByUserId(String id);
}
