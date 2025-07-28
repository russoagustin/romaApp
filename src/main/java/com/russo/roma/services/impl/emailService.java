package com.russo.roma.services.impl;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class emailService {

    private final JavaMailSender mailSender;

    public emailService(JavaMailSender mailSender){
        this.mailSender = mailSender;
    }

    public void enviarEmail(String destino, String asunto, String Contenido){
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(destino);
        message.setText(Contenido);
        message.setSubject(asunto);
        mailSender.send(message);
    }
}
