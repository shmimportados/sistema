package com.agendamento.sistema.controller;

import com.agendamento.sistema.dto.AppointmentDTO;
import com.agendamento.sistema.dto.CustomerDTO;
import com.agendamento.sistema.model.Business;
import com.agendamento.sistema.repository.BusinessRepository;
import com.agendamento.sistema.service.AppointmentService;
import com.agendamento.sistema.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping("/book")
public class PublicBookingController {

    @Autowired
    private BusinessRepository businessRepository;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private AppointmentService appointmentService;

    @GetMapping("/{slug}")
    public String bookingPage(@PathVariable String slug, Model model) {
        // In a real application, you would resolve the slug to a business ID
        // For now, we'll use the slug as business ID
        try {
            Long businessId = Long.parseLong(slug);
            Optional<Business> business = businessRepository.findById(businessId);
            
            if (business.isPresent()) {
                model.addAttribute("business", business.get());
                model.addAttribute("services", business.get().getServices());
                return "client/booking";
            }
        } catch (Exception e) {
            // Handle error
        }
        return "error";
    }

    @PostMapping("/{businessId}/create-appointment")
    public String createAppointment(@PathVariable Long businessId, 
                                   @RequestParam String customerName,
                                   @RequestParam String customerPhone,
                                   @RequestParam String customerEmail,
                                   @RequestParam String customerWhatsapp,
                                   AppointmentDTO appointmentDTO,
                                   Model model) {
        try {
            // Create or get customer
            CustomerDTO customerDTO = CustomerDTO.builder()
                    .fullName(customerName)
                    .phone(customerPhone)
                    .email(customerEmail)
                    .whatsappPhone(customerWhatsapp)
                    .build();

            var customer = customerService.createCustomer(businessId, customerDTO);
            appointmentDTO.setCustomerId(customer.getId());

            // Create appointment
            appointmentService.createAppointment(businessId, appointmentDTO);

            model.addAttribute("success", "Agendamento realizado com sucesso!");
            return "client/booking-success";
        } catch (Exception e) {
            model.addAttribute("error", "Erro ao criar agendamento: " + e.getMessage());
            return "client/booking";
        }
    }
}
