package com.mehrab.springdemo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.http.HttpClient;

@Configuration
public class Utils {

    @Bean
    public HttpClient getHttp() {
        return HttpClient.newHttpClient();
    }
}
