package com.cumulus.backend.user.service;

import com.cumulus.backend.exception.CustomException;
import com.cumulus.backend.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;
    private final StringRedisTemplate redisTemplate;
    private static final long EXPIRATION_MINUTES = 5;

    public void sendCodeToEmail(String email){
        String code = generateRandomCode();
        saveCodeToRedis(email, code);

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("[DMatch] 이메일 인증 코드입니다.");
        message.setText("인증 코드: " + code + "\n5분 이내에 입력해주세요.");

        mailSender.send(message);
    }

    public boolean verifyCode(String email, String code){
        String savedCode = redisTemplate.opsForValue().get("email-code:" + email);
        if (savedCode == null) { // TTL이 끝났거나, 인증코드를 요청하지 않은 경우
            throw new CustomException(ErrorCode.EMAIL_VERIFICATION_CODE_EXPIRED);
        }

        if (!savedCode.equals(code)) {
            throw new CustomException(ErrorCode.EMAIL_VERIFICATION_CODE_INVALID);
        }

        // 이메일 인증완료상태를 저장
        String key = "email-verified:"+email;
        redisTemplate.opsForValue().set(key, "true", Duration.ofMinutes(30)); // 인증후 30분만 가입 허용
        return true;
    }

    private String generateRandomCode() {
        return String.valueOf((int) ((Math.random() * 900000) + 100000)); // 6자리 인증번호
    }

    private void saveCodeToRedis(String email, String code){
        String key = "email-code:"+email;
        redisTemplate.opsForValue().set(key, code, Duration.ofMinutes(EXPIRATION_MINUTES));
    }
}
