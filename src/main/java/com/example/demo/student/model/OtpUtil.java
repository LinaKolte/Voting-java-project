package com.example.demo.student.model;

import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Component;

@Component
public class OtpUtil {

    private Map<String, String> otpMap = new ConcurrentHashMap<>();

    public String generateOtp(String email) {
        String otp = String.valueOf(100000 + new Random().nextInt(900000));
        otpMap.put(email, otp);
        return otp;
    }

    public boolean validateOtp(String email, String otp) {
        return otp.equals(otpMap.get(email));
    }

    public void clearOtp(String email) {
        otpMap.remove(email);
    }
}
