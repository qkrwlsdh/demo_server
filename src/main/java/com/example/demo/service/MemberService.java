package com.example.demo.service;

import com.example.demo.config.SHA256;
import com.example.demo.domain.Member;
import com.example.demo.dto.MemberDTO;
import com.example.demo.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.NoSuchAlgorithmException;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;

    public MemberDTO login(MemberDTO memberDTO) throws NoSuchAlgorithmException {
        /*
        * 1. 회원이 입력한 이메일로 DB에서 조회
        * 2. DB에서 조회한 비밀번호와 사용자가 입력한 비밀번호가 일치하는지 판단
        * */
        Optional<Member> byLoginId = memberRepository.findByLoginId(memberDTO.getLoginId());

        SHA256 sha256 = new SHA256();

        String cryptogram = sha256.encrypt(memberDTO.getLoginPw());

        if (byLoginId.isPresent()) {
            // 조회 결과가 있다
            Member member = byLoginId.get();
            if (member.getLoginPw().equalsIgnoreCase(cryptogram)) {
                // 비밀번호 일치
                // entity -> dto 변환 후 리턴
                MemberDTO dto = MemberDTO.toMemberDTO(member);
                return dto;
            } else {
                // 비밀번호 불일치
                return null;
            }
        } else {
            // 조회 결과가 없다
            return null;
        }
    }

    @Transactional
    public void updateFailCnt(String id, long failCnt) {
        Member findMember = memberRepository.findByLoginId(id).orElseThrow(NullPointerException::new);

        findMember.setFailCnt(failCnt);
    }

    @Transactional
    public void updateLockYn(String id, String sucessYn) {
        Member findMember = memberRepository.findByLoginId(id).orElseThrow(NullPointerException::new);

        findMember.setLockYn(sucessYn);
    }

    public String findByGoogleOtp(String loginId) {
        return memberRepository.findByGoogleOtp(loginId);
    }

    @Transactional
    public void updateOtpKey(String id, String key) {
        Member findMember = memberRepository.findByLoginId(id).orElseThrow(NullPointerException::new);

        findMember.setGoogleOtp(key);
    }
}
