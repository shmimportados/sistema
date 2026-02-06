package com.agendamento.sistema.repository;

import com.agendamento.sistema.model.Business;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BusinessRepository extends JpaRepository<Business, Long> {
    List<Business> findByOwnerId(Long ownerId);
    Optional<Business> findByIdAndOwnerId(Long id, Long ownerId);
}
