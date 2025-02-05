package com.glaiss.users.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.glaiss.core.config.cache.RedisConfiguration;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Configuration;

@EnableCaching
@Configuration
public class RedisConfig extends RedisConfiguration {

    public RedisConfig(ObjectMapper objectMapper) {
        super(objectMapper);
    }
}
