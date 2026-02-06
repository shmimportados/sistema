package com.agendamento.sistema.service;

import com.agendamento.sistema.dto.CustomerDTO;
import com.agendamento.sistema.model.Business;
import com.agendamento.sistema.model.Customer;
import com.agendamento.sistema.repository.BusinessRepository;
import com.agendamento.sistema.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private BusinessRepository businessRepository;

    public Customer createCustomer(Long businessId, CustomerDTO dto) {
        Business business = businessRepository.findById(businessId)
                .orElseThrow(() -> new RuntimeException("Negócio não encontrado"));

        Customer customer = Customer.builder()
                .fullName(dto.getFullName())
                .phone(dto.getPhone())
                .email(dto.getEmail())
                .whatsappPhone(dto.getWhatsappPhone())
                .business(business)
                .active(true)
                .build();

        return customerRepository.save(customer);
    }

    public Customer updateCustomer(Long customerId, Long businessId, CustomerDTO dto) {
        Customer customer = customerRepository.findByIdAndBusinessId(customerId, businessId)
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));

        customer.setFullName(dto.getFullName());
        customer.setPhone(dto.getPhone());
        customer.setEmail(dto.getEmail());
        customer.setWhatsappPhone(dto.getWhatsappPhone());

        return customerRepository.save(customer);
    }

    public List<CustomerDTO> getBusinessCustomers(Long businessId) {
        return customerRepository.findByBusinessId(businessId)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public Customer getCustomerById(Long customerId, Long businessId) {
        return customerRepository.findByIdAndBusinessId(customerId, businessId)
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));
    }

    public void deleteCustomer(Long customerId, Long businessId) {
        Customer customer = customerRepository.findByIdAndBusinessId(customerId, businessId)
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));
        customer.setActive(false);
        customerRepository.save(customer);
    }

    private CustomerDTO convertToDTO(Customer customer) {
        return CustomerDTO.builder()
                .id(customer.getId())
                .fullName(customer.getFullName())
                .phone(customer.getPhone())
                .email(customer.getEmail())
                .whatsappPhone(customer.getWhatsappPhone())
                .build();
    }
}
