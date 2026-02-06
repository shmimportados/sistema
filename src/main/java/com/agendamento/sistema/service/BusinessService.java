package com.agendamento.sistema.service;

import com.agendamento.sistema.dto.BusinessDTO;
import com.agendamento.sistema.model.Business;
import com.agendamento.sistema.model.User;
import com.agendamento.sistema.repository.BusinessRepository;
import com.agendamento.sistema.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BusinessService {

    @Autowired
    private BusinessRepository businessRepository;

    @Autowired
    private UserRepository userRepository;

    public Business createBusiness(BusinessDTO dto, Long ownerId) {
        User owner = userRepository.findById(ownerId)
                .orElseThrow(() -> new RuntimeException("Proprietário não encontrado"));

        Business business = Business.builder()
                .name(dto.getName())
                .type(dto.getType())
                .description(dto.getDescription())
                .phone(dto.getPhone())
                .email(dto.getEmail())
                .address(dto.getAddress())
                .city(dto.getCity())
                .state(dto.getState())
                .zipCode(dto.getZipCode())
                .owner(owner)
                .whatsappPhone(dto.getWhatsappPhone())
                .smtpEmail(dto.getEmail())
                .enableEmailReminder(dto.getEnableEmailReminder() != null ? dto.getEnableEmailReminder() : true)
                .enableWhatsappReminder(dto.getEnableWhatsappReminder() != null ? dto.getEnableWhatsappReminder() : true)
                .reminderHoursBefore(dto.getReminderHoursBefore() != null ? dto.getReminderHoursBefore() : 24)
                .appointmentDurationMinutes(dto.getAppointmentDurationMinutes() != null ? dto.getAppointmentDurationMinutes() : 60)
                .businessHoursStart(dto.getBusinessHoursStart() != null ? dto.getBusinessHoursStart() : "08:00")
                .businessHoursEnd(dto.getBusinessHoursEnd() != null ? dto.getBusinessHoursEnd() : "18:00")
                .active(true)
                .build();

        return businessRepository.save(business);
    }

    public Business updateBusiness(Long id, Long ownerId, BusinessDTO dto) {
        Business business = businessRepository.findByIdAndOwnerId(id, ownerId)
                .orElseThrow(() -> new RuntimeException("Negócio não encontrado"));

        business.setName(dto.getName());
        business.setType(dto.getType());
        business.setDescription(dto.getDescription());
        business.setPhone(dto.getPhone());
        business.setEmail(dto.getEmail());
        business.setAddress(dto.getAddress());
        business.setCity(dto.getCity());
        business.setState(dto.getState());
        business.setZipCode(dto.getZipCode());
        business.setWhatsappPhone(dto.getWhatsappPhone());
        business.setEnableEmailReminder(dto.getEnableEmailReminder());
        business.setEnableWhatsappReminder(dto.getEnableWhatsappReminder());
        business.setReminderHoursBefore(dto.getReminderHoursBefore());
        business.setAppointmentDurationMinutes(dto.getAppointmentDurationMinutes());
        business.setBusinessHoursStart(dto.getBusinessHoursStart());
        business.setBusinessHoursEnd(dto.getBusinessHoursEnd());

        return businessRepository.save(business);
    }

    public List<Business> findByOwnerId(Long ownerId) {
        return businessRepository.findByOwnerId(ownerId);
    }

    public Optional<Business> findById(Long id) {
        return businessRepository.findById(id);
    }

    public Optional<Business> findByIdAndOwnerId(Long id, Long ownerId) {
        return businessRepository.findByIdAndOwnerId(id, ownerId);
    }

    public BusinessDTO convertToDTO(Business business) {
        return BusinessDTO.builder()
                .id(business.getId())
                .name(business.getName())
                .type(business.getType())
                .description(business.getDescription())
                .phone(business.getPhone())
                .email(business.getEmail())
                .address(business.getAddress())
                .city(business.getCity())
                .state(business.getState())
                .zipCode(business.getZipCode())
                .whatsappPhone(business.getWhatsappPhone())
                .enableEmailReminder(business.getEnableEmailReminder())
                .enableWhatsappReminder(business.getEnableWhatsappReminder())
                .reminderHoursBefore(business.getReminderHoursBefore())
                .appointmentDurationMinutes(business.getAppointmentDurationMinutes())
                .businessHoursStart(business.getBusinessHoursStart())
                .businessHoursEnd(business.getBusinessHoursEnd())
                .build();
    }

    public void deleteBusiness(Long id, Long ownerId) {
        Business business = businessRepository.findByIdAndOwnerId(id, ownerId)
                .orElseThrow(() -> new RuntimeException("Negócio não encontrado"));
        business.setActive(false);
        businessRepository.save(business);
    }
}
