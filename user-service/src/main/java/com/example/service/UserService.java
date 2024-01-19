package com.example.service;

//import com.example.dto.UserCreateRequest;

import com.example.dto.UserCreateRequest;
import com.example.models.User;
import com.example.repo.UserCacheRepository;
import com.example.repo.UserRepository;
import com.example.utils.Constants;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.json.simple.JSONObject;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
@AllArgsConstructor
public class UserService implements UserDetailsService {
    UserRepository userRepository;
    KafkaTemplate<String,String> kafkaTemplate;
    ObjectMapper objectMapper;
    UserCacheRepository userCacheRepository;
    public void create(UserCreateRequest userCreateRequest) throws JsonProcessingException {
        User user = userCreateRequest.to();
        user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
        userRepository.save(user);

        //communicate to wallet service to create a user's wallet
        JSONObject event = objectMapper.convertValue(user, JSONObject.class);
        String message = objectMapper.writeValueAsString(event);
    kafkaTemplate.send(Constants.USER_CREATED_TOPIC,message);
    }

    public User get(int userId) {
   User user = userCacheRepository.get(userId);
   if (user == null)
   {
       user = this.userRepository.findById(userId).orElseThrow(NoSuchElementException::new);
       this.userCacheRepository.save(user);
   }
   return user;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return this.userRepository.findByMobile(username);
    }
}
