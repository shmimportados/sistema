package com.agendamento.sistema.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerDTO {
    private Long id;
    private String fullName;
    private String phone;
    private String email;
    private String whatsappPhone;
}
