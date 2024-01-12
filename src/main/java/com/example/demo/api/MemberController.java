package com.example.demo.api;

import com.example.demo.domain.History;
import com.example.demo.dto.HistoryDTO;
import com.example.demo.dto.MemberDTO;
import com.example.demo.service.HistoryService;
import com.example.demo.service.MemberService;
import com.warrenstrange.googleauth.GoogleAuthenticator;
import com.warrenstrange.googleauth.GoogleAuthenticatorKey;
import com.warrenstrange.googleauth.GoogleAuthenticatorQRGenerator;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class MemberController {
    private static final Logger logger = LoggerFactory.getLogger(MemberController.class);
    private final MemberService memberService;
    private final HistoryService historyService;

    @PostMapping("/api/login")
    public ResponseEntity login(@RequestBody MemberDTO memberDTO, HistoryDTO historyDTO, HttpSession session) {
        HttpHeaders headers = new HttpHeaders();
        Map<String, String> body = new HashMap<>();
        HttpStatus status;

        GoogleAuthenticator googleAuthenticator = new GoogleAuthenticator();

        try {
            MemberDTO loginResult = memberService.login(memberDTO);
            session.setAttribute("getLoginId", memberDTO.getLoginId());

            if (loginResult != null) {
                // 로그인 성공
                logger.info("LOGIN SUCCESS");
                if (!loginResult.getLockYn().equals("N")) {
                    // LOCK_YN이 Y일 경우
                    status = HttpStatus.NO_CONTENT;  // 204
                } else {
                    // 로그인 LOCK 여부 체크
                    String otpKey = loginResult.getGoogleOtp();

                    // OTP Key가 있으면 loginId와 googleOtpKey를 보냄
                    if (otpKey != null && !otpKey.isBlank()) {
                        body.put("loginId", loginResult.getLoginId());
                        body.put("googleOtp", otpKey);
                    } else {
                        // 없으면 OTP Key 생성 후 업데이트
                        GoogleAuthenticatorKey googleAuthenticatorKey = googleAuthenticator.createCredentials();
                        String key = googleAuthenticatorKey.getKey();
                        memberService.updateOtpKey(loginResult.getLoginId(), key);

                        // OTP 등록 QR URL 생성하여 보냄
                        String qrUrl = GoogleAuthenticatorQRGenerator.getOtpAuthURL("BANKEDIN", loginResult.getLoginId(), googleAuthenticatorKey);
                        body.put("QRUrl", qrUrl);
                        body.put("loginId", loginResult.getLoginId());
                        body.put("googleOtp", key);
                        status = HttpStatus.CREATED; // 201

                        return new ResponseEntity(body, headers, status);
                    }

                    status = HttpStatus.OK; // 200
                }
            } else {
                // 로그인 실패, 히스토리 생성
                String loginId = session.getAttribute("getLoginId").toString();
                History history = new History(
                        loginId,
                        historyDTO.getLoginDt(),
                        "N"
                );
                historyService.create(history);

                // 로그인 실패 카운트 업데이트
                long loginFailCnt = memberService.findByFailCnt(loginId);
                memberService.updateFailCnt(loginId, loginFailCnt + 1);

                // 실패 횟수 체크하여 5회 이상이면 LOCK_YN UPDATE
                if (loginFailCnt > 4) {
                    memberService.updateLockYn(loginId, "Y");
                }
                status = HttpStatus.BAD_REQUEST; // 400
            }
        } catch (Exception exception) {
            status = HttpStatus.BAD_REQUEST; // 400
            logger.error("login/exception = {}", exception.getMessage());
        }
        return new ResponseEntity(body, headers, status);
    }

    @PostMapping("/api/auth")
    public ResponseEntity otpVerify(@RequestBody Map<String, String> data, MemberDTO memberDTO, HistoryDTO historyDTO) throws UnknownHostException {
        HttpHeaders headers = new HttpHeaders();
        Map<String, String> body = new HashMap<>();
        HttpStatus status;

        GoogleAuthenticator googleAuthenticator = new GoogleAuthenticator();

        boolean verify = googleAuthenticator.authorize(data.get("googleOtp"), Integer.parseInt(data.get("token")));

        logger.info("VerifyCode : {}", Integer.parseInt(data.get("token")));
        logger.info("Verify : {}", verify);

        History history = new History(
                memberDTO.getLoginId(),
                historyDTO.getLoginDt(),
                "Y"
        );
        historyService.create(history);

        // 로그인 성공 시 실패 카운트 0
        memberService.updateFailCnt(data.get("loginId"), 0);

        body.put("verify", String.valueOf(verify));
        status = HttpStatus.OK;

        return new ResponseEntity(body, headers, status);
    }
}
