package com.example.demo.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@Table(name = "TUSER")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@ToString
public class Member {
    @Id
    private String loginId;
    private String loginPw;
    private long failCnt;
    private String lockYn;
    private String googleOtp;

    public Member(String loginId, String loginPw, int failCnt, String lockYn, String googleOtp) {
        this.loginId = loginId;
        this.loginPw = loginPw;
        this.failCnt = failCnt;
        this.lockYn = lockYn;
        this.googleOtp = googleOtp;
    }
}
