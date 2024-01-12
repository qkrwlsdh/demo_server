package com.example.demo.repository;

import com.example.demo.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, String> {
    // 이메일로 회원 정보 조회
    Optional<Member> findByLoginId(String loginId);

    @Query(value =  " SELECT" +
                    " GOOGLE_OTP" +
                    " FROM TUSER" +
                    " WHERE LOGIN_ID = :loginId", nativeQuery = true)
    String findByGoogleOtp(@Param(value = "loginId") String loginId);

    @Query(value =  " SELECT" +
                    " FAIL_CNT" +
                    " FROM TUSER" +
                    " WHERE LOGIN_ID = :loginId", nativeQuery = true)
    long findByFailCnt(@Param(value = "loginId") String loginId);
}
