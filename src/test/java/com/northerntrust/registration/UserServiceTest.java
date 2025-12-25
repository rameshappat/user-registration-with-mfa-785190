package com.northerntrust.registration;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

class UserServiceTest {
    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;
    @Mock
    private EmailService emailService;
    @Mock
    private SmsService smsService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRegisterUser_Success() {
        RegistrationRequest request = new RegistrationRequest();
        request.setEmail("test@example.com");
        request.setPassword("password123");
        request.setPhoneNumber("1234567890");

        when(userRepository.findByEmail(request.getEmail())).thenReturn(null);

        ResponseEntity<String> response = userService.registerUser(request);

        assertEquals("User registered successfully. OTP sent.", response.getBody());
        verify(userRepository).save(any(User.class));
        verify(emailService).sendOtp(anyString(), anyString());
        verify(smsService).sendOtp(anyString(), anyString());
    }

    @Test
    void testRegisterUser_EmailAlreadyExists() {
        RegistrationRequest request = new RegistrationRequest();
        request.setEmail("test@example.com");

        when(userRepository.findByEmail(request.getEmail())).thenReturn(new User());

        ResponseEntity<String> response = userService.registerUser(request);

        assertEquals(409, response.getStatusCodeValue());
        assertEquals("Email already in use", response.getBody());
    }
}