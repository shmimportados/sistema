package com.agendamento.sistema.repository;

import com.agendamento.sistema.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    List<Customer> findByBusinessId(Long businessId);
    Optional<Customer> findByIdAndBusinessId(Long id, Long businessId);
    Optional<Customer> findByPhoneAndBusinessId(String phone, Long businessId);
    Boolean existsByPhoneAndBusinessId(String phone, Long businessId);
}
