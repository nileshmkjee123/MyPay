package com.example.service;

import com.example.dto.TransactionInitiateRequest;
import com.example.model.Transaction;
import com.example.model.TransactionStatus;
import com.example.repo.TxnRepository;
import com.example.utils.Constant;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@AllArgsConstructor
public class TxnService {
    TxnRepository txnRepository;
    ObjectMapper objectMapper;
    JSONParser jsonParser;
    KafkaTemplate<String,String> kafkaTemplate;
    public String initiate(String sender, TransactionInitiateRequest request) throws JsonProcessingException {

        Transaction transaction = Transaction.builder()
                .externalTxnId(UUID.randomUUID().toString())
                .sender(sender)
                .receiver(request.getReceiver())
                .amount(request.getAmount())
                .transactionStatus(TransactionStatus.PENDING)
                .build();
        txnRepository.save(transaction);
        //After saving transaction publish event using kafka
        kafkaTemplate.send(Constant.TXN_INITIATED_TOPIC,objectMapper.writeValueAsString(transaction));

        //wallet service will listen to this msg and update wallet accordingly
        return transaction.getExternalTxnId();
    }
    @KafkaListener(topics = {Constant.WALLET_UPDATED_TOPIC}, groupId = "test123")
    public void updateTxn(String msg) throws ParseException, JsonProcessingException {
        JSONObject event = (JSONObject) jsonParser.parse(msg);
//        String sender = String.valueOf(event.get("sender"));
//        String receiver = String.valueOf(event.get("receiver"));
        String walletUpdateStatus = String.valueOf(event.get("walletUpdateStatus"));
        String externalTxnId = String.valueOf(event.get("externalTxnId"));
        Double amount = (Double) event.get("balance");

    TransactionStatus transactionStatus=  walletUpdateStatus.equals("FAILED")?TransactionStatus.FAILED:TransactionStatus.SUCCESSFUL;
    this.txnRepository.updateTxnStatus(externalTxnId,transactionStatus);
        Transaction transaction =  this.txnRepository.findByExternalTxnId(externalTxnId);
    kafkaTemplate.send(Constant.TXN_COMPLETED_TOPIC,objectMapper.writeValueAsString(transaction));
    }
}
