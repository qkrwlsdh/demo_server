package com.example.demo.dto;

import com.example.demo.domain.Member;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MemberDTO {
    private String loginId;
    private String loginPw;
    private long failCnt;
    private String lockYn;
    private String googleOtp;

    public static MemberDTO toMemberDTO(Member member) {
        MemberDTO memberDTO = new MemberDTO();
        memberDTO.setLoginId(member.getLoginId());
        memberDTO.setLoginPw(member.getLoginPw());
        memberDTO.setLockYn(member.getLockYn());
        memberDTO.setFailCnt(member.getFailCnt());
        memberDTO.setGoogleOtp(member.getGoogleOtp());
        return memberDTO;
    }
}
