package com.mehrab.springdemo.service;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mehrab.springdemo.config.Utils;
import com.mehrab.springdemo.payload.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;

@Service
public class ProductServiceImpl implements ProductService {

    @Value("${external.api}")
    private String ExAPI;

    @Autowired
    Utils webConfig;

    @Override
    public Product[] getProducts() {
        HttpRequest req = HttpRequest.newBuilder().uri(URI.create(ExAPI+"/products")).GET().build();
        ObjectMapper objectMapper = new ObjectMapper();
        Product[] products;
        try {
            HttpResponse<String> res = webConfig.getHttp().send(req, HttpResponse.BodyHandlers.ofString());
            String result = res.body();
            int start = result.indexOf("[");
            int end = result.lastIndexOf("]");
            products = objectMapper.readValue(result.substring(start,end+1), Product[].class);
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
        return products;
    }
}
