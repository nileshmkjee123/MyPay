package com.example.controller;

import com.example.dto.TransactionInitiateRequest;
import com.example.model.TxnUser;
import com.example.service.TxnService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
@AllArgsConstructor
@RestController
public class TranssactionController {
    TxnService txnService;
@PostMapping("/transact")
    public String initiateTxn(@Valid @RequestBody TransactionInitiateRequest request) throws JsonProcessingException {
   TxnUser user = (TxnUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    return txnService.initiate(user.getUsername(), request);
}
}
