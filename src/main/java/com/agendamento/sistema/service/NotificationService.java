package com.agendamento.sistema.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

@Service
public class NotificationService {

    @Autowired(required = false)
    private JavaMailSender mailSender;

    private static final String TWILIO_ACCOUNT_SID = ""; // Add your Twilio credentials
    private static final String TWILIO_AUTH_TOKEN = "";
    private static final String TWILIO_PHONE = "";

    public void sendEmailConfirmation(String email, String subject, String message) {
        try {
            if (mailSender == null) {
                System.out.println("Email não configurado - Simulando envio: " + email);
                return;
            }

            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setTo(email);
            mailMessage.setSubject(subject);
            mailMessage.setText(message);
            mailMessage.setFrom("noreply@agendamento.com");

            mailSender.send(mailMessage);
            System.out.println("Email enviado para: " + email);
        } catch (Exception e) {
            System.out.println("Erro ao enviar email: " + e.getMessage());
        }
    }

    public void sendWhatsappMessage(String phoneNumber, String message) {
        try {
            // Initialize Twilio (only if credentials are configured)
            if (!TWILIO_ACCOUNT_SID.isEmpty()) {
                Twilio.init(TWILIO_ACCOUNT_SID, TWILIO_AUTH_TOKEN);
                Message twilioMessage = Message.creator(
                        new PhoneNumber(phoneNumber),  // To number
                        new PhoneNumber(TWILIO_PHONE),  // From number
                        message)
                        .create();

                System.out.println("WhatsApp enviado: " + twilioMessage.getSid());
            } else {
                System.out.println("WhatsApp não configurado - Simulando envio para: " + phoneNumber);
            }
        } catch (Exception e) {
            System.out.println("Erro ao enviar WhatsApp: " + e.getMessage());
        }
    }

    public String generateConfirmationMessage(String customerName, String serviceName, String appointmentDateTime) {
        return "Olá " + customerName + "!\n\n" +
               "Confirmamos seu agendamento para:\n" +
               "Serviço: " + serviceName + "\n" +
               "Data/Hora: " + appointmentDateTime + "\n\n" +
               "Obrigado!";
    }

    public String generateReminderMessage(String customerName, String serviceName, String appointmentDateTime) {
        return "Olá " + customerName + "!\n\n" +
               "Lembrete: Você tem um agendamento marcado para:\n" +
               "Serviço: " + serviceName + "\n" +
               "Data/Hora: " + appointmentDateTime + "\n\n" +
               "Confirme seu comparecimento.";
    }
}
