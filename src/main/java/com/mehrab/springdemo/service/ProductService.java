package com.mehrab.springdemo.service;

import com.mehrab.springdemo.payload.product.ProductResponse;

public interface ProductService {
    ProductResponse[] getProducts();
}
