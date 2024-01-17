package com.example.dto;


import lombok.*;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TransactionInitiateRequest {
    @NotBlank
    private String receiver;
    @NotBlank
    private Double amount;
    private String purpose;
}
