package com.agendamento.sistema.controller;

import com.agendamento.sistema.dto.UserDTO;
import com.agendamento.sistema.model.User;
import com.agendamento.sistema.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;

@Controller
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @GetMapping("/login")
    public String login() {
        return "auth/login";
    }

    @PostMapping("/login")
    public String authenticate(@RequestParam String email, @RequestParam String password, 
                              HttpSession session, Model model) {
        try {
            var user = userService.authenticate(email, password);
            if (user.isPresent()) {
                session.setAttribute("user", user.get());
                
                if (user.get().getRole() == User.UserRole.BUSINESS_OWNER) {
                    return "redirect:/admin/dashboard";
                } else if (user.get().getRole() == User.UserRole.CUSTOMER) {
                    return "redirect:/client/appointments";
                } else {
                    return "redirect:/admin/users";
                }
            }
        } catch (Exception e) {
            model.addAttribute("error", "Erro ao autenticar: " + e.getMessage());
        }
        return "auth/login";
    }

    @GetMapping("/register")
    public String register() {
        return "auth/register";
    }

    @PostMapping("/register")
    public String registerUser(@RequestParam String email, @RequestParam String password,
                              @RequestParam String fullName, @RequestParam String phone,
                              @RequestParam String userType, Model model) {
        try {
            User.UserRole role = User.UserRole.valueOf(userType.toUpperCase());
            User user = userService.createUser(email, password, fullName, phone, role);
            model.addAttribute("success", "Usuário registrado com sucesso!");
            return "redirect:/auth/login";
        } catch (Exception e) {
            model.addAttribute("error", "Erro ao registrar: " + e.getMessage());
            return "auth/register";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/auth/login";
    }
}
