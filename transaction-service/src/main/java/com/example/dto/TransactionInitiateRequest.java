package com.example.dto;


import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TransactionInitiateRequest {
    @NotBlank
    private String receiver;
    @NotNull
    private Double amount;
    private String purpose;
}
