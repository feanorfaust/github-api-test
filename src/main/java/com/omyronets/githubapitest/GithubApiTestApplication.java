package com.omyronets.githubapitest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.util.UrlPathHelper;

@SpringBootApplication
public class GithubApiTestApplication {

    public static void main(String[] args) {
        SpringApplication.run(GithubApiTestApplication.class, args);
    }

}
