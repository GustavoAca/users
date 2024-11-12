package com.glaiss.users.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EntityScan(basePackages = "com.glaiss.users.domain.model")
@EnableJpaRepositories(basePackages = {"com.glaiss.users.domain"})
public class RepositoryConfig {

}

