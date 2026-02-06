package com.agendamento.sistema.service;

import com.agendamento.sistema.dto.AppointmentDTO;
import com.agendamento.sistema.model.*;
import com.agendamento.sistema.repository.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@org.springframework.stereotype.Service
public class AppointmentService {

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private ServiceRepository serviceRepository;

    @Autowired
    private BusinessRepository businessRepository;

    @Autowired
    private NotificationService notificationService;

    public Appointment createAppointment(Long businessId, AppointmentDTO dto) {
        Business business = businessRepository.findById(businessId)
                .orElseThrow(() -> new RuntimeException("Negócio não encontrado"));

        Customer customer = customerRepository.findById(dto.getCustomerId())
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));

        Service service = serviceRepository.findById(dto.getServiceId())
                .orElseThrow(() -> new RuntimeException("Serviço não encontrado"));

        Appointment appointment = Appointment.builder()
                .customer(customer)
                .business(business)
                .service(service)
                .appointmentDateTime(dto.getAppointmentDateTime())
                .notes(dto.getNotes())
                .status(Appointment.AppointmentStatus.PENDING)
                .build();

        Appointment saved = appointmentRepository.save(appointment);

        // Send automatic confirmation
        sendConfirmation(saved);

        return saved;
    }

    public void confirmAppointment(Long appointmentId, Long businessId) {
        Appointment appointment = appointmentRepository.findByIdAndBusinessId(appointmentId, businessId)
                .orElseThrow(() -> new RuntimeException("Agendamento não encontrado"));

        appointment.setStatus(Appointment.AppointmentStatus.CONFIRMED);
        appointment.setConfirmedAt(LocalDateTime.now());
        appointmentRepository.save(appointment);

        // Send confirmation notification
        sendConfirmationNotification(appointment);
    }

    public void cancelAppointment(Long appointmentId, Long businessId) {
        Appointment appointment = appointmentRepository.findByIdAndBusinessId(appointmentId, businessId)
                .orElseThrow(() -> new RuntimeException("Agendamento não encontrado"));

        appointment.setStatus(Appointment.AppointmentStatus.CANCELLED);
        appointmentRepository.save(appointment);
    }

    public void completeAppointment(Long appointmentId, Long businessId) {
        Appointment appointment = appointmentRepository.findByIdAndBusinessId(appointmentId, businessId)
                .orElseThrow(() -> new RuntimeException("Agendamento não encontrado"));

        appointment.setStatus(Appointment.AppointmentStatus.COMPLETED);
        appointmentRepository.save(appointment);
    }

    public List<AppointmentDTO> getBusinessAppointments(Long businessId) {
        return appointmentRepository.findByBusinessId(businessId)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<Appointment> getAppointmentsByDateRange(Long businessId, LocalDateTime start, LocalDateTime end) {
        return appointmentRepository.findByBusinessIdAndDateRange(businessId, start, end);
    }

    private void sendConfirmation(Appointment appointment) {
        Business business = appointment.getBusiness();
        Customer customer = appointment.getCustomer();
        Service service = appointment.getService();

        String message = notificationService.generateConfirmationMessage(
                customer.getFullName(),
                service.getName(),
                appointment.getAppointmentDateTime().toString()
        );

        if (business.getEnableEmailReminder() && customer.getEmail() != null) {
            notificationService.sendEmailConfirmation(
                    customer.getEmail(),
                    "Agendamento Confirmado",
                    message
            );
            appointment.setEmailReminderSent(true);
        }

        if (business.getEnableWhatsappReminder()) {
            notificationService.sendWhatsappMessage(customer.getWhatsappPhone(), message);
            appointment.setWhatsappReminderSent(true);
        }

        appointment.setConfirmationSent(true);
        appointmentRepository.save(appointment);
    }

    private void sendConfirmationNotification(Appointment appointment) {
        Business business = appointment.getBusiness();
        Customer customer = appointment.getCustomer();
        Service service = appointment.getService();

        String message = "Seu agendamento foi confirmado!\n" +
                        "Serviço: " + service.getName() + "\n" +
                        "Data/Hora: " + appointment.getAppointmentDateTime();

        if (business.getEnableEmailReminder() && customer.getEmail() != null) {
            notificationService.sendEmailConfirmation(
                    customer.getEmail(),
                    "Agendamento Confirmado",
                    message
            );
        }

        if (business.getEnableWhatsappReminder()) {
            notificationService.sendWhatsappMessage(customer.getWhatsappPhone(), message);
        }
    }

    private AppointmentDTO convertToDTO(Appointment appointment) {
        return AppointmentDTO.builder()
                .id(appointment.getId())
                .customerId(appointment.getCustomer().getId())
                .serviceId(appointment.getService().getId())
                .appointmentDateTime(appointment.getAppointmentDateTime())
                .status(appointment.getStatus().toString())
                .notes(appointment.getNotes())
                .emailReminderSent(appointment.getEmailReminderSent())
                .whatsappReminderSent(appointment.getWhatsappReminderSent())
                .confirmationSent(appointment.getConfirmationSent())
                .build();
    }

    public Appointment getAppointmentById(Long appointmentId, Long businessId) {
        return appointmentRepository.findByIdAndBusinessId(appointmentId, businessId)
                .orElseThrow(() -> new RuntimeException("Agendamento não encontrado"));
    }
}
