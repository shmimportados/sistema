package com.agendamento.sistema.repository;

import com.agendamento.sistema.model.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    List<Appointment> findByBusinessId(Long businessId);
    List<Appointment> findByCustomerId(Long customerId);
    Optional<Appointment> findByIdAndBusinessId(Long id, Long businessId);

    @Query("SELECT a FROM Appointment a WHERE a.business.id = :businessId " +
           "AND a.appointmentDateTime >= :startDate AND a.appointmentDateTime <= :endDate")
    List<Appointment> findByBusinessIdAndDateRange(Long businessId, LocalDateTime startDate, LocalDateTime endDate);

    @Query("SELECT a FROM Appointment a WHERE a.status = 'PENDING' " +
           "AND a.confirmationSent = false AND a.business.id = :businessId")
    List<Appointment> findPendingAppointments(Long businessId);

    @Query("SELECT a FROM Appointment a WHERE (a.emailReminderSent = false OR a.whatsappReminderSent = false) " +
           "AND a.appointmentDateTime BETWEEN :startTime AND :endTime " +
           "AND a.status = 'CONFIRMED'")
    List<Appointment> findAppointmentsForReminder(LocalDateTime startTime, LocalDateTime endTime);
}
