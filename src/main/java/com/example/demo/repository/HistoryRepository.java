package com.example.demo.repository;

import com.example.demo.domain.History;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;

public interface HistoryRepository extends JpaRepository<History, LocalDateTime> {
    long countByLoginIdAndSucessYn(String loginId, String sucessYn);
}
