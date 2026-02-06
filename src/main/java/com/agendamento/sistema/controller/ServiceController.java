package com.agendamento.sistema.controller;

import com.agendamento.sistema.dto.ServiceDTO;
import com.agendamento.sistema.model.User;
import com.agendamento.sistema.service.BusinessService;
import com.agendamento.sistema.service.ServiceManagementService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/services")
public class ServiceController {

    @Autowired
    private ServiceManagementService serviceManagementService;

    @Autowired
    private BusinessService businessService;

    @GetMapping("/{businessId}")
    public String listServices(@PathVariable Long businessId, HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/auth/login";
        }

        var business = businessService.findByIdAndOwnerId(businessId, user.getId());
        if (business.isEmpty()) {
            return "redirect:/admin/dashboard";
        }

        var services = serviceManagementService.getBusinessServices(businessId);
        model.addAttribute("business", business.get());
        model.addAttribute("services", services);
        return "admin/services";
    }

    @GetMapping("/{businessId}/new")
    public String newService(@PathVariable Long businessId, HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/auth/login";
        }

        var business = businessService.findByIdAndOwnerId(businessId, user.getId());
        if (business.isEmpty()) {
            return "redirect:/admin/dashboard";
        }

        model.addAttribute("business", business.get());
        return "admin/service-form";
    }

    @PostMapping("/{businessId}/create")
    public String createService(@PathVariable Long businessId, ServiceDTO dto,
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

            serviceManagementService.createService(businessId, dto);
            return "redirect:/admin/services/" + businessId;
        } catch (Exception e) {
            model.addAttribute("error", "Erro ao criar serviço: " + e.getMessage());
            return "admin/service-form";
        }
    }

    @GetMapping("/{businessId}/{serviceId}/edit")
    public String editService(@PathVariable Long businessId, @PathVariable Long serviceId,
                             HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/auth/login";
        }

        var business = businessService.findByIdAndOwnerId(businessId, user.getId());
        if (business.isEmpty()) {
            return "redirect:/admin/dashboard";
        }

        try {
            var service = serviceManagementService.getServiceById(serviceId, businessId);
            model.addAttribute("business", business.get());
            model.addAttribute("service", service);
            return "admin/service-edit";
        } catch (Exception e) {
            return "redirect:/admin/services/" + businessId;
        }
    }

    @PostMapping("/{businessId}/{serviceId}/update")
    public String updateService(@PathVariable Long businessId, @PathVariable Long serviceId,
                               ServiceDTO dto, HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/auth/login";
        }

        try {
            serviceManagementService.updateService(serviceId, businessId, dto);
            return "redirect:/admin/services/" + businessId;
        } catch (Exception e) {
            model.addAttribute("error", "Erro ao atualizar serviço: " + e.getMessage());
            return "admin/service-edit";
        }
    }

    @PostMapping("/{businessId}/{serviceId}/delete")
    public String deleteService(@PathVariable Long businessId, @PathVariable Long serviceId,
                               HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/auth/login";
        }

        try {
            serviceManagementService.deleteService(serviceId, businessId);
        } catch (Exception e) {
            // Log error
        }
        return "redirect:/admin/services/" + businessId;
    }
}
