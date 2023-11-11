package com.example.productservice.controller;

import com.example.productservice.dto.Coupon;
import com.example.productservice.model.Product;
import com.example.productservice.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/productapi")
public class ProductController {

    @Autowired
    ProductRepository repository;
    @Autowired
    private RestTemplate restTemplate;
    @Value("${couponService.url}")
    private String couponServiceURL;

    @RequestMapping(value = "/products", method= RequestMethod.POST)
    public Product create(@RequestBody Product product) {
        Coupon coupon = restTemplate.getForObject(couponServiceURL + product.getCouponCode(), Coupon.class);
        product.setPrice(product.getPrice().subtract(coupon.getDiscount()));
        return repository.save(product);
    }

    @RequestMapping(value = "/products/{productId}", method = RequestMethod.GET)
    public Product getProductById(@PathVariable Long productId) {
        return repository.findById(productId).get();
    }
}
