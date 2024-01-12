package com.example.demo.service;

import com.example.demo.domain.History;
import com.example.demo.repository.HistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class HistoryService {
    private final HistoryRepository historyRepository;

    @Transactional
    public void create(History history) {
        historyRepository.save(history);
    }

    public long countByLoginIdAndSucessYn(String loginId, String sucessYn) {

        long loginFailCnt = historyRepository.countByLoginIdAndSucessYn(loginId, sucessYn);
        return loginFailCnt;
    }
}
