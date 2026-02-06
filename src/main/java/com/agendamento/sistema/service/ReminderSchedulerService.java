package com.agendamento.sistema.service;

import com.agendamento.sistema.model.Appointment;
import com.agendamento.sistema.repository.AppointmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ReminderSchedulerService {

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private NotificationService notificationService;

    // Run every hour to send reminders
    @Scheduled(cron = "0 0 * * * *")
    public void sendReminders() {
        System.out.println("Verificando agendamentos para enviar lembretes...");
        
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime endTime = now.plusHours(24);

        List<Appointment> appointments = appointmentRepository.findAppointmentsForReminder(now, endTime);
        
        for (Appointment appointment : appointments) {
            if (!appointment.getEmailReminderSent() && appointment.getBusiness().getEnableEmailReminder()) {
                sendEmailReminder(appointment);
            }
            
            if (!appointment.getWhatsappReminderSent() && appointment.getBusiness().getEnableWhatsappReminder()) {
                sendWhatsappReminder(appointment);
            }
        }
    }

    private void sendEmailReminder(Appointment appointment) {
        try {
            String message = String.format(
                "Olá %s,\n\n" +
                "Lembrete: Você tem um agendamento em:\n" +
                "Serviço: %s\n" +
                "Data/Hora: %s\n\n" +
                "Obrigado!",
                appointment.getCustomer().getFullName(),
                appointment.getService().getName(),
                appointment.getAppointmentDateTime()
            );

            notificationService.sendEmailConfirmation(
                appointment.getCustomer().getEmail(),
                "Lembrete de Agendamento",
                message
            );

            appointment.setEmailReminderSent(true);
            appointmentRepository.save(appointment);
            System.out.println("Email de lembrete enviado para: " + appointment.getCustomer().getEmail());
        } catch (Exception e) {
            System.out.println("Erro ao enviar email de lembrete: " + e.getMessage());
        }
    }

    private void sendWhatsappReminder(Appointment appointment) {
        try {
            String message = String.format(
                "Olá %s!\n\n" +
                "Lembrete: Você tem um agendamento em:\n" +
                "Serviço: %s\n" +
                "Data/Hora: %s\n\n" +
                "Obrigado!",
                appointment.getCustomer().getFullName(),
                appointment.getService().getName(),
                appointment.getAppointmentDateTime()
            );

            notificationService.sendWhatsappMessage(
                appointment.getCustomer().getWhatsappPhone(),
                message
            );

            appointment.setWhatsappReminderSent(true);
            appointmentRepository.save(appointment);
            System.out.println("WhatsApp de lembrete enviado para: " + appointment.getCustomer().getWhatsappPhone());
        } catch (Exception e) {
            System.out.println("Erro ao enviar WhatsApp de lembrete: " + e.getMessage());
        }
    }
}
