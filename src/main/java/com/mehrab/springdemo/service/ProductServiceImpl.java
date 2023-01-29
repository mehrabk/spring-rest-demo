package com.mehrab.springdemo.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mehrab.springdemo.payload.product.ProductResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Service
public class ProductServiceImpl implements ProductService {

    @Value("${dummyjson.api}")
    private String dummyjsonAPI;

    @Override
    public ProductResponse[] getProducts() {
        // httpClient is both async and sync module for http request
        HttpClient http = HttpClient.newHttpClient();
        HttpRequest req = HttpRequest.newBuilder().uri(URI.create(dummyjsonAPI+"/products")).GET().build();
        ObjectMapper objectMapper = new ObjectMapper();
        ProductResponse[] products;
        try {
            HttpResponse<String> res = http.send(req, HttpResponse.BodyHandlers.ofString());
            String result = res.body();
            int start = result.indexOf("[");
            int end = result.lastIndexOf("]");
            products = objectMapper.readValue(result.substring(start,end+1), ProductResponse[].class);
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
        return products;
    }
}
