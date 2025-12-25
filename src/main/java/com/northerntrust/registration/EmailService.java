package com.northerntrust.registration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.sendgrid.*;
import java.io.IOException;

@Service
public class EmailService {
    @Autowired
    private SendGrid sendGrid;

    public void sendOtp(String email, String otp) {
        Email from = new Email("no-reply@northerntrust.com");
        Email to = new Email(email);
        String subject = "Your OTP Code";
        Content content = new Content("text/plain", "Your OTP code is: " + otp);
        Mail mail = new Mail(from, subject, to, content);

        try {
            Request request = new Request();
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());
            sendGrid.api(request);
        } catch (IOException ex) {
            System.err.println("Error sending email: " + ex.getMessage());
        }
    }
}