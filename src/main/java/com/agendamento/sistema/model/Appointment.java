package com.agendamento.sistema.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "appointments")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @ManyToOne
    @JoinColumn(name = "business_id", nullable = false)
    private Business business;

    @ManyToOne
    @JoinColumn(name = "service_id", nullable = false)
    private Service service;

    @Column(nullable = false)
    private LocalDateTime appointmentDateTime;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AppointmentStatus status = AppointmentStatus.PENDING;

    @Column(length = 500)
    private String notes;

    @Column(nullable = false)
    private Boolean emailReminderSent = false;

    @Column(nullable = false)
    private Boolean whatsappReminderSent = false;

    @Column(nullable = false)
    private Boolean confirmationSent = false;

    private LocalDateTime confirmedAt;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    private LocalDateTime updatedAt;

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public enum AppointmentStatus {
        PENDING,      // Aguardando confirmação
        CONFIRMED,    // Confirmado
        CANCELLED,    // Cancelado
        COMPLETED,    // Realizado
        NO_SHOW       // Não compareceu
    }
}
