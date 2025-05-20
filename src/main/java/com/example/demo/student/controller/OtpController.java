package com.example.demo.student.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.student.model.OtpUtil;
import com.example.demo.student.service.EmailService;

@RestController
@RequestMapping("/api/otp")
public class OtpController {

    @Autowired
    private EmailService emailService;

    @Autowired
    private OtpUtil otpUtil;

    @PostMapping("/send")
    public ResponseEntity<String> sendOtp(@RequestParam String email) {
        String otp = otpUtil.generateOtp(email);
        emailService.sendOtp(email, otp);
        return ResponseEntity.ok("OTP sent to your email.");
    }

    @PostMapping("/verify")
    public ResponseEntity<String> verifyOtp(@RequestParam String email, @RequestParam String otp) {
        if (otpUtil.validateOtp(email, otp)) {
            otpUtil.clearOtp(email);
            return ResponseEntity.ok("OTP verified successfully.");
        } else {
            return ResponseEntity.badRequest().body("Invalid OTP.");
        }
    }
}
