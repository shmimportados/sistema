package com.agendamento.sistema.service;

import com.agendamento.sistema.dto.UserDTO;
import com.agendamento.sistema.model.User;
import com.agendamento.sistema.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User createUser(String email, String password, String fullName, String phone, User.UserRole role) {
        if (userRepository.existsByEmail(email)) {
            throw new RuntimeException("Email já cadastrado");
        }

        User user = User.builder()
                .email(email)
                .password(password) // TODO: Use password encoding (BCrypt)
                .fullName(fullName)
                .phone(phone)
                .role(role)
                .active(true)
                .build();

        return userRepository.save(user);
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public Optional<User> authenticate(String email, String password) {
        return userRepository.findByEmailAndPassword(email, password);
    }

    public UserDTO convertToDTO(User user) {
        return UserDTO.builder()
                .id(user.getId())
                .email(user.getEmail())
                .fullName(user.getFullName())
                .phone(user.getPhone())
                .role(user.getRole().toString())
                .build();
    }

    public User updateUser(Long id, String fullName, String phone) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
        user.setFullName(fullName);
        user.setPhone(phone);
        return userRepository.save(user);
    }

    public User findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
    }
}
