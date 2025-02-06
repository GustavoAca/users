package com.glaiss.users.config;

import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Profile("!test")
@Configuration
@EnableFeignClients(basePackages = {"com.glaiss.users.client"})
public class FeignConfiguration {
}
