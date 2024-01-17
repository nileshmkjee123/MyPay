package com.example.service;

import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class TransactionUserDetailsService implements UserDetailsService {
RestTemplate restTemplate = new RestTemplate();
private static Logger logger = LoggerFactory.getLogger(TransactionUserDetailsService.class);
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //fetch the user details
       JSONObject userData = restTemplate.getForEntity("http://localhost:8082/user?userId="+username, JSONObject.class).getBody();
       logger.info("user data - {}",userData);
       return null;
    }
}
