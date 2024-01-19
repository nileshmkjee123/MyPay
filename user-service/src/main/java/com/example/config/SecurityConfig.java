package com.example.config;

import com.example.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@AllArgsConstructor
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    UserService userService;
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //super.configure(http);
        http.httpBasic()
                .and().csrf().disable()
                .authorizeRequests().
                antMatchers(HttpMethod.POST,"/user/**").permitAll()
                .antMatchers(HttpMethod.GET,"/user/details/**").hasAuthority("usr")
                .antMatchers(HttpMethod.GET,"/user/mobile/**").hasAuthority("svc")
                .anyRequest()
                .authenticated()
                .and()
                .formLogin();

    }
@Bean
    public PasswordEncoder getPE(){
        return new BCryptPasswordEncoder();
}
}
