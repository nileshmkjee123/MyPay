package com.example.controller;

import com.example.dto.TransactionInitiateRequest;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
public class TranssactionController {
@PostMapping("/transact")
    public String initiateTxn(@Valid @RequestBody TransactionInitiateRequest request)
{
   // User user = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    return null;
}
}
