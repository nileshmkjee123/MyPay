package com.example.services;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import com.example.models.Wallet;
import com.example.repo.WalletRepository;
import com.example.utils.Constants;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;


@Service
public class WalletService {
    @Autowired
    JSONParser jsonParser;
    @Autowired
    WalletRepository walletRepository;
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    KafkaTemplate<String,String> kafkaTemplate;
    @Value("${wallet.create.opening-balance}")
    private Double balance;
    private static Logger logger = LoggerFactory.getLogger(WalletService.class);
    @KafkaListener(topics = {Constants.USER_CREATED_TOPIC},groupId = "test_123")
    public void create(String msg) throws ParseException {
    JSONObject event = (JSONObject) jsonParser.parse(msg);
    String mobile = String.valueOf(event.get("mobile"));
    if(mobile == null)
    {logger.warn("create: unable to find userId in the event,data={}",event);
        return;
    }
        Wallet wallet = Wallet.builder()
                .mobile(mobile)
                .balance(balance)
                .build();
    this.walletRepository.save(wallet);
    }
    @KafkaListener(topics = {Constants.TXN_INITIATED_TOPIC},groupId = "test_123")
    public void update(String msg) throws ParseException, JsonProcessingException {
        JSONObject event = (JSONObject) jsonParser.parse(msg);
        String sender = String.valueOf(event.get("sender"));
        String receiver = String.valueOf(event.get("receiver"));
        Double amount = (Double) event.get("amount");
        String externalTxnId = String.valueOf(event.get("externalTxnId"));
        JSONObject message = new JSONObject();
        message.put("receiver",receiver);
        message.put("sender",sender);
        message.put("amount",amount);
        message.put("externalTxnId",externalTxnId);
        //wallet update and set the transaction state from pending to success or failed
        Wallet senderWallet = walletRepository.findByMobile(sender);
        Wallet receiverWallet = walletRepository.findByMobile(receiver);
        if(senderWallet == null || receiverWallet == null || senderWallet.getBalance()<amount){
            logger.warn("Wallets won't get updated as the constraints failed");
            //notify other services that transaction failed
            // now wallet instead acts as a publisher to publish the transaction status for topic=wallet update
            // this message will be consumed by transaction service
            message.put("walletUpdateStatus", "FAILED");
            kafkaTemplate.send(Constants.WALLET_UPDATE_TOPIC,objectMapper.writeValueAsString(message));
            return;
        }
        try {
            walletRepository.updateWallet(sender, -amount);
            walletRepository.updateWallet(receiver, amount);
            message.put("walletUpdateStatus", "SUCCESS");
            kafkaTemplate.send(Constants.WALLET_UPDATE_TOPIC,objectMapper.writeValueAsString(message));
        }
        catch (Exception e){
            message.put("walletUpdateStatus", "FAILED");
            kafkaTemplate.send(Constants.WALLET_UPDATE_TOPIC,objectMapper.writeValueAsString(message));
        }
    }
}
