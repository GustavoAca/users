package com.glaiss.users.config;

import com.glaiss.core.logger.ApiLogginFilter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class FilterConfig {

    @Value("${spring.application.name}")
    private String apiName;

    @Bean
    public FilterRegistrationBean<ApiLogginFilter> apiLogginFilter() {
        FilterRegistrationBean<ApiLogginFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new ApiLogginFilter(apiName));
        registrationBean.addUrlPatterns("/*");
        registrationBean.setName("ApiLogginFilter");
        log.error(apiName);
        return registrationBean;
    }
}
