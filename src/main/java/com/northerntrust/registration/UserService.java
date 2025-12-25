package com.northerntrust.registration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private EmailService emailService;
    @Autowired
    private SmsService smsService;

    @Transactional
    public ResponseEntity<String> registerUser(RegistrationRequest request) {
        if (userRepository.findByEmail(request.getEmail()) != null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Email already in use");
        }
        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword()); // Ideally, hash the password
        user.setPhoneNumber(request.getPhoneNumber());
        user.setCreatedAt(LocalDateTime.now());
        user.setVerified(false);
        userRepository.save(user);

        // Send OTP via email and SMS
        String otp = generateOtp();
        emailService.sendOtp(user.getEmail(), otp);
        smsService.sendOtp(user.getPhoneNumber(), otp);

        return ResponseEntity.ok("User registered successfully. OTP sent.");
    }

    private String generateOtp() {
        // Implement OTP generation logic
        return "123456"; // Placeholder
    }
}