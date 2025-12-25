package com.northerntrust.registration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

@Service
public class SmsService {
    @Autowired
    private TwilioConfig twilioConfig;

    public void sendOtp(String phoneNumber, String otp) {
        Message.creator(
            new PhoneNumber(phoneNumber),
            new PhoneNumber(twilioConfig.getTwilioNumber()),
            "Your OTP code is: " + otp
        ).create();
    }
}