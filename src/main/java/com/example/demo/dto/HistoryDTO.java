package com.example.demo.dto;

import com.example.demo.domain.History;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class HistoryDTO {
    private String loginId;
    private LocalDateTime loginDt;
    private String loginIp;
    private String sucessYn;

    public HistoryDTO(History history) {
        this.loginId = history.getLoginId();
        this.loginDt = history.getLoginDt();
        this.loginIp = history.getLoginIp();
        this.sucessYn = history.getSucessYn();
    }
}
