package com.example.controller;

import com.example.dto.UserCreateRequest;
import com.example.models.User;
import com.example.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/user")
@AllArgsConstructor
public class UserController {
    UserService userService;
    @PostMapping("")
    public void createUser(@RequestBody @Valid UserCreateRequest userCreateRequest) throws JsonProcessingException {
    this.userService.create(userCreateRequest);
    }
    @GetMapping("")
            public User getUserDetails(@RequestParam("userId") int userId){
        return this.userService.get(userId);
    }
}
