package com.omyronets.githubapitest.config;

import com.omyronets.githubapitest.web.WebhookInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebhookConfig implements WebMvcConfigurer {

    private final WebhookInterceptor webhookInterceptor;

    public WebhookConfig(WebhookInterceptor webhookInterceptor) {
        this.webhookInterceptor = webhookInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(webhookInterceptor).addPathPatterns("/webhook");
    }

}
