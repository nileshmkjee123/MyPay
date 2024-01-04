package com.example.service;

//import com.example.dto.UserCreateRequest;

import com.example.dto.UserCreateRequest;
import com.example.models.User;
import com.example.repo.UserRepository;
import com.example.utils.Constants;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserService {
    UserRepository userRepository;
    KafkaTemplate<String,String> kafkaTemplate;
    ObjectMapper objectMapper;
    public void create(UserCreateRequest userCreateRequest) throws JsonProcessingException {
        User user = userCreateRequest.to();
        userRepository.save(user);

        //communicate to wallet service to create a user's wallet
        String message = objectMapper.writeValueAsString(user);
    kafkaTemplate.send(Constants.USER_CREATED_TOPIC,message);
    }
}
