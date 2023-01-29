package com.mehrab.springdemo.service;

import com.mehrab.springdemo.payload.Product;
import org.springframework.stereotype.Service;

public interface ProductService {
    Product[] getProducts();
}
