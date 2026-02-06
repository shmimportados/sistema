package com.agendamento.sistema.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ServiceDTO {
    private Long id;
    private String name;
    private String description;
    private java.math.BigDecimal price;
    private Integer durationMinutes;
}
