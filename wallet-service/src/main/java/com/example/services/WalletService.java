package com.example.services;
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
import org.springframework.stereotype.Service;


@Service
public class WalletService {
    @Autowired
    JSONParser jsonParser;
    @Autowired
    WalletRepository walletRepository;

    @Value("${wallet.create.opening-balance}")
    private Double balance;
    private static Logger logger = LoggerFactory.getLogger(WalletService.class);
    @KafkaListener(topics = {Constants.USER_CREATED_TOPIC},groupId = "test_123")
    public void create(String msg) throws ParseException {
    JSONObject event = (JSONObject) jsonParser.parse(msg);
    String userId = String.valueOf(event.get("userId"));
    if(userId == null)
    {logger.warn("create: unable to find userId in the event,data={}",event);
        return;
    }
        Wallet wallet = Wallet.builder()
                .userId(userId)
                .balance(balance)
                .build();
    this.walletRepository.save(wallet);
    }
}
