package com.agendamento.sistema.controller;

import com.agendamento.sistema.dto.BusinessDTO;
import com.agendamento.sistema.model.Business;
import com.agendamento.sistema.model.User;
import com.agendamento.sistema.service.BusinessService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private BusinessService businessService;

    @GetMapping("/dashboard")
    public String dashboard(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/auth/login";
        }

        if (user.getRole() != User.UserRole.BUSINESS_OWNER && user.getRole() != User.UserRole.ADMIN) {
            return "redirect:/auth/login";
        }

        List<Business> businesses = businessService.findByOwnerId(user.getId());
        model.addAttribute("businesses", businesses);
        model.addAttribute("user", user);

        return "admin/dashboard";
    }

    @GetMapping("/business/new")
    public String newBusiness(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/auth/login";
        }

        model.addAttribute("businessTypes", new String[]{"salão", "barbearia", "manicure", "mecânico"});
        return "admin/business-form";
    }

    @PostMapping("/business/create")
    public String createBusiness(HttpSession session, BusinessDTO dto, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/auth/login";
        }

        try {
            Business business = businessService.createBusiness(dto, user.getId());
            return "redirect:/admin/business/" + business.getId();
        } catch (Exception e) {
            model.addAttribute("error", "Erro ao criar negócio: " + e.getMessage());
            return "admin/business-form";
        }
    }

    @GetMapping("/business/{id}")
    public String viewBusiness(@PathVariable Long id, HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/auth/login";
        }

        var business = businessService.findByIdAndOwnerId(id, user.getId());
        if (business.isEmpty()) {
            return "redirect:/admin/dashboard";
        }

        model.addAttribute("business", business.get());
        return "admin/business-details";
    }

    @GetMapping("/business/{id}/edit")
    public String editBusiness(@PathVariable Long id, HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/auth/login";
        }

        var business = businessService.findByIdAndOwnerId(id, user.getId());
        if (business.isEmpty()) {
            return "redirect:/admin/dashboard";
        }

        model.addAttribute("business", business.get());
        model.addAttribute("businessTypes", new String[]{"salão", "barbearia", "manicure", "mecânico"});
        return "admin/business-edit";
    }

    @PostMapping("/business/{id}/update")
    public String updateBusiness(@PathVariable Long id, HttpSession session, BusinessDTO dto, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/auth/login";
        }

        try {
            businessService.updateBusiness(id, user.getId(), dto);
            return "redirect:/admin/business/" + id;
        } catch (Exception e) {
            model.addAttribute("error", "Erro ao atualizar negócio: " + e.getMessage());
            return "admin/business-edit";
        }
    }
}
