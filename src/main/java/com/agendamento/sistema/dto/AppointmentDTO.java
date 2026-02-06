package com.agendamento.sistema.dto;

import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AppointmentDTO {
    private Long id;
    private Long customerId;
    private Long serviceId;
    private LocalDateTime appointmentDateTime;
    private String status;
    private String notes;
    private Boolean emailReminderSent;
    private Boolean whatsappReminderSent;
    private Boolean confirmationSent;
}
