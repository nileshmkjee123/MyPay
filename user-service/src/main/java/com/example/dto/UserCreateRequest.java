package com.example.dto;

import com.example.models.User;
import lombok.*;

import javax.validation.constraints.NotBlank;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserCreateRequest {
    private String name;
    @NotBlank
    private String mobile;
    @NotBlank
    private String email;
public User to()
{
    return User.builder()
            .name(this.name)
            .mobile(this.mobile)
            .email(this.email)
            .build();
}
}
