package com.agendamento.sistema.controller;

import com.agendamento.sistema.dto.AppointmentDTO;
import com.agendamento.sistema.dto.CustomerDTO;
import com.agendamento.sistema.model.Appointment;
import com.agendamento.sistema.model.Business;
import com.agendamento.sistema.model.User;
import com.agendamento.sistema.service.AppointmentService;
import com.agendamento.sistema.service.BusinessService;
import com.agendamento.sistema.service.CustomerService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/admin/appointments")
public class AppointmentController {

    @Autowired
    private AppointmentService appointmentService;

    @Autowired
    private BusinessService businessService;

    @Autowired
    private CustomerService customerService;

    @GetMapping("/{businessId}")
    public String viewAppointments(@PathVariable Long businessId, HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/auth/login";
        }

        var business = businessService.findByIdAndOwnerId(businessId, user.getId());
        if (business.isEmpty()) {
            return "redirect:/admin/dashboard";
        }

        List<AppointmentDTO> appointments = appointmentService.getBusinessAppointments(businessId);
        model.addAttribute("business", business.get());
        model.addAttribute("appointments", appointments);
        return "admin/appointments";
    }

    @GetMapping("/{businessId}/new")
    public String newAppointment(@PathVariable Long businessId, HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/auth/login";
        }

        var business = businessService.findByIdAndOwnerId(businessId, user.getId());
        if (business.isEmpty()) {
            return "redirect:/admin/dashboard";
        }

        var customers = customerService.getBusinessCustomers(businessId);
        model.addAttribute("business", business.get());
        model.addAttribute("customers", customers);
        model.addAttribute("services", business.get().getServices());
        return "admin/appointment-form";
    }

    @PostMapping("/{businessId}/create")
    public String createAppointment(@PathVariable Long businessId, AppointmentDTO dto, 
                                   HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/auth/login";
        }

        try {
            var business = businessService.findByIdAndOwnerId(businessId, user.getId());
            if (business.isEmpty()) {
                return "redirect:/admin/dashboard";
            }

            appointmentService.createAppointment(businessId, dto);
            return "redirect:/admin/appointments/" + businessId;
        } catch (Exception e) {
            model.addAttribute("error", "Erro ao criar agendamento: " + e.getMessage());
            return "admin/appointment-form";
        }
    }

    @PostMapping("/{businessId}/{appointmentId}/confirm")
    public String confirmAppointment(@PathVariable Long businessId, @PathVariable Long appointmentId,
                                    HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/auth/login";
        }

        try {
            var business = businessService.findByIdAndOwnerId(businessId, user.getId());
            if (business.isEmpty()) {
                return "redirect:/admin/dashboard";
            }

            appointmentService.confirmAppointment(appointmentId, businessId);
            return "redirect:/admin/appointments/" + businessId;
        } catch (Exception e) {
            model.addAttribute("error", "Erro ao confirmar agendamento: " + e.getMessage());
            return "redirect:/admin/appointments/" + businessId;
        }
    }

    @PostMapping("/{businessId}/{appointmentId}/cancel")
    public String cancelAppointment(@PathVariable Long businessId, @PathVariable Long appointmentId,
                                   HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/auth/login";
        }

        try {
            appointmentService.cancelAppointment(appointmentId, businessId);
        } catch (Exception e) {
            // Log error
        }
        return "redirect:/admin/appointments/" + businessId;
    }
}
