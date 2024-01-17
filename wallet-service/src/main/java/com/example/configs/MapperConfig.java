package com.example.configs;

import org.json.simple.parser.JSONParser;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MapperConfig {
@Bean
    public JSONParser getMapper()
    {
        return new JSONParser();
    }
}
