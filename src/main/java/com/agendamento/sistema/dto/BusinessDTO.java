package com.agendamento.sistema.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BusinessDTO {
    private Long id;
    private String name;
    private String type;
    private String description;
    private String phone;
    private String email;
    private String address;
    private String city;
    private String state;
    private String zipCode;
    private String whatsappPhone;
    private Boolean enableEmailReminder;
    private Boolean enableWhatsappReminder;
    private Integer reminderHoursBefore;
    private Integer appointmentDurationMinutes;
    private String businessHoursStart;
    private String businessHoursEnd;
}
