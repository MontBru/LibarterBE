package com.bryan.libarterbe.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    @Autowired
    JavaMailSender javaMailSender;
    public boolean sendEmail(String sendTo, String subject, String text)
    {
        try {
            SimpleMailMessage msg = new SimpleMailMessage();
            msg.setTo(sendTo);
            msg.setSubject(subject);
            msg.setText(text);

            javaMailSender.send(msg);
            return true;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return false;
        }

    }

}
