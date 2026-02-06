package com.agendamento.sistema.service;

import com.agendamento.sistema.dto.ServiceDTO;
import com.agendamento.sistema.model.Business;
import com.agendamento.sistema.model.Service;
import com.agendamento.sistema.repository.BusinessRepository;
import com.agendamento.sistema.repository.ServiceRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.Collectors;

@org.springframework.stereotype.Service
public class ServiceManagementService {

    @Autowired
    private ServiceRepository serviceRepository;

    @Autowired
    private BusinessRepository businessRepository;

    public Service createService(Long businessId, ServiceDTO dto) {
        Business business = businessRepository.findById(businessId)
                .orElseThrow(() -> new RuntimeException("Negócio não encontrado"));

        Service service = Service.builder()
                .name(dto.getName())
                .description(dto.getDescription())
                .price(dto.getPrice())
                .durationMinutes(dto.getDurationMinutes())
                .business(business)
                .active(true)
                .build();

        return serviceRepository.save(service);
    }

    public Service updateService(Long serviceId, Long businessId, ServiceDTO dto) {
        Service service = serviceRepository.findByIdAndBusinessId(serviceId, businessId)
                .orElseThrow(() -> new RuntimeException("Serviço não encontrado"));

        service.setName(dto.getName());
        service.setDescription(dto.getDescription());
        service.setPrice(dto.getPrice());
        service.setDurationMinutes(dto.getDurationMinutes());

        return serviceRepository.save(service);
    }

    public List<ServiceDTO> getBusinessServices(Long businessId) {
        return serviceRepository.findByBusinessId(businessId)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public Service getServiceById(Long serviceId, Long businessId) {
        return serviceRepository.findByIdAndBusinessId(serviceId, businessId)
                .orElseThrow(() -> new RuntimeException("Serviço não encontrado"));
    }

    public void deleteService(Long serviceId, Long businessId) {
        Service service = serviceRepository.findByIdAndBusinessId(serviceId, businessId)
                .orElseThrow(() -> new RuntimeException("Serviço não encontrado"));
        service.setActive(false);
        serviceRepository.save(service);
    }

    private ServiceDTO convertToDTO(Service service) {
        return ServiceDTO.builder()
                .id(service.getId())
                .name(service.getName())
                .description(service.getDescription())
                .price(service.getPrice())
                .durationMinutes(service.getDurationMinutes())
                .build();
    }
}
