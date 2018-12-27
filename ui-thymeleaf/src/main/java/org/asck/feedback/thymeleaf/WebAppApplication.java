package org.asck.feedback.thymeleaf;

import org.asck.service.client.IFeedbackClientService;
import org.asck.service.client.impl.FeedbackClientService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@SpringBootApplication
public class WebAppApplication {

    public static void main(String[] args) {
        SpringApplication.run(WebAppApplication.class, args);
    }
    
    @Value("${service.base.path:http://localhost:8080/v1/feedback}")
    private String serviceBasePath;
    
    @Bean
    public IFeedbackClientService createService() {
    	return new FeedbackClientService(this.serviceBasePath);
    }
    
}