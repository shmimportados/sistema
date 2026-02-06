package com.agendamento.sistema.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "businesses")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Business {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String type; // salão, barbearia, manicure, mecânico

    @Column(length = 500)
    private String description;

    @Column(nullable = false)
    private String phone;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String address;

    private String city;

    private String state;

    private String zipCode;

    @OneToOne
    @JoinColumn(name = "owner_id", nullable = false)
    private User owner;

    @Column(nullable = false)
    private String whatsappPhone;

    @Column(nullable = false)
    private String whatsappApiKey = "";

    @Column(nullable = false)
    private String smtpHost = "smtp.gmail.com";

    @Column(nullable = false)
    private Integer smtpPort = 587;

    @Column(nullable = false)
    private String smtpEmail;

    @Column(nullable = false)
    private String smtpPassword;

    @Column(nullable = false)
    private Boolean enableEmailReminder = true;

    @Column(nullable = false)
    private Boolean enableWhatsappReminder = true;

    @Column(nullable = false)
    private Integer reminderHoursBefore = 24;

    @Column(nullable = false)
    private Integer appointmentDurationMinutes = 60;

    @Column(nullable = false)
    private String businessHoursStart = "08:00";

    @Column(nullable = false)
    private String businessHoursEnd = "18:00";

    @OneToMany(mappedBy = "business", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Service> services;

    @OneToMany(mappedBy = "business", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Appointment> appointments;

    @Column(nullable = false)
    private Boolean active = true;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    private LocalDateTime updatedAt;

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
