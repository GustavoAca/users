package com.glaiss.users.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Profile("!test")
@Configuration
@ConditionalOnProperty(name = "spring.feign.client.enabled", havingValue = "true")
@EnableFeignClients(basePackages = {"com.glaiss.users.client"})
public class FeignConfiguration {
}
