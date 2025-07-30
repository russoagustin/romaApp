package com.russo.roma.services.impl;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.russo.roma.services.interfaces.IEmailService;

@Service
public class EmailService implements IEmailService {

    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender){
        this.mailSender = mailSender;
    }

    @Override
    public void enviarEmail(String destino, String asunto, String Contenido){
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(destino);
        message.setText(Contenido);
        message.setSubject(asunto);
        mailSender.send(message);
    }
}
