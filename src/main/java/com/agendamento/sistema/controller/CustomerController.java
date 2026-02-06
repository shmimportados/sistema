package com.agendamento.sistema.controller;

import com.agendamento.sistema.dto.CustomerDTO;
import com.agendamento.sistema.model.User;
import com.agendamento.sistema.service.BusinessService;
import com.agendamento.sistema.service.CustomerService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/customers")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @Autowired
    private BusinessService businessService;

    @GetMapping("/{businessId}")
    public String listCustomers(@PathVariable Long businessId, HttpSession session, Model model) {
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
        return "admin/customers";
    }

    @GetMapping("/{businessId}/new")
    public String newCustomer(@PathVariable Long businessId, HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/auth/login";
        }

        var business = businessService.findByIdAndOwnerId(businessId, user.getId());
        if (business.isEmpty()) {
            return "redirect:/admin/dashboard";
        }

        model.addAttribute("business", business.get());
        return "admin/customer-form";
    }

    @PostMapping("/{businessId}/create")
    public String createCustomer(@PathVariable Long businessId, CustomerDTO dto,
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

            customerService.createCustomer(businessId, dto);
            return "redirect:/admin/customers/" + businessId;
        } catch (Exception e) {
            model.addAttribute("error", "Erro ao criar cliente: " + e.getMessage());
            return "admin/customer-form";
        }
    }
}
