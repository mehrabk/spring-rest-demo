package com.mehrab.springdemo.controller;

import com.mehrab.springdemo.payload.Product;
import com.mehrab.springdemo.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/product")
public class ProductController {

    private ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }


    @RequestMapping(method = RequestMethod.GET, path = "/getAll")
    public Product[] getAll() {
        return productService.getProducts();
    }
}
