package com.agendamento.sistema.repository;

import com.agendamento.sistema.model.Service;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ServiceRepository extends JpaRepository<Service, Long> {
    List<Service> findByBusinessId(Long businessId);
    Optional<Service> findByIdAndBusinessId(Long id, Long businessId);
}
