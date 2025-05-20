package com.example.demo.student.service;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.student.model.Student;
import com.example.demo.student.repository.StudentRepository;

@Service
public class LoginoService {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private EmailService emailService;
    public LoginoService(StudentRepository studentRepository, EmailService emailService) { // ✅ Inject VoteService
        this.studentRepository = studentRepository;
        this.emailService=emailService;
    }

    // In-memory OTP storage: Aadhaar -> OTP
    private final Map<String, String> otpStore = new ConcurrentHashMap<>();

    // ✅ 1. Check if student exists by Aadhaar number
    public boolean checkUserExists(String aadharno) {
        return studentRepository.findByAadharno(aadharno).isPresent();
    }

    // ✅ 2. Generate OTP and send to email
    public void generateAndSendOtp(String aadharno) {
        Optional<Student> studentOpt = studentRepository.findByAadharno(aadharno);
        if (studentOpt.isPresent()) {
            Student student = studentOpt.get();
            String email = student.getEmail();

            // Generate a random 6-digit OTP
            String otp = String.valueOf((int)(Math.random() * 900000) + 100000);
            otpStore.put(aadharno, otp);

            // Send OTP to email
            emailService.sendOtp(email, otp);
        }
    }

    public boolean verifyOtp(String aadharno, String inputOtp) {
        String trimmedAadhar = aadharno.trim();
        String trimmedOtp = inputOtp.trim();
        String storedOtp = otpStore.get(trimmedAadhar);
    
        System.out.println("Stored OTP: " + storedOtp);
        System.out.println("Entered OTP: " + trimmedOtp);
    
        return storedOtp != null && storedOtp.equals(trimmedOtp);
    }
    
}
