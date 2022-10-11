package com.example.demo;


import com.example.demo.security.CustomOAuth2User;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

@RestController
@RequestMapping("/products")

public class ProductController {

    List<Product> products = new ArrayList<>();

    public ProductController() {
        products.add(Product.from("Men's Shoes (White)", "White color men's shoes", 100, "USD"));
        products.add(Product.from("TShirt (Blue)", "Blue color t-shirt", 55, "USD"));
        products.add(Product.from("TShirt (White)", "White color t-shirt", 50, "USD"));
        products.add(Product.from("Short (White)", "White color short", 60, "USD"));
        products.add(Product.from("Short (Black)", "Black color short", 55, "USD"));
    }

    @GetMapping
    public List<Product> getProducts(){
        return products;
    }

    @GetMapping("/user")
    public Map<String, String> user(@AuthenticationPrincipal DefaultOAuth2User principal) throws Exception{
        return new CustomOAuth2User(principal).jsonBody();
    }
    @PostMapping
    public void addProduct(@RequestBody Product product){
        if (product.getName().isEmpty() || product.getDescription().isEmpty()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid product name or description");
        }
        if (product.getCurrency().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid currency");
        }
        if (product.getPrice() <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Price shoud be non-negative");
        }
        product.setId(UUID.randomUUID().toString());
        products.add(product);
    }

}
