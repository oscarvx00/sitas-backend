package com.example.demo.controller;


import com.example.demo.dto.Product;
import com.example.demo.dto.CustomOAuth2User;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

@RestController

public class UserController {
    @GetMapping("/me")
    public Map<String, String> user(@AuthenticationPrincipal DefaultOAuth2User principal) throws Exception{
        return new CustomOAuth2User(principal.getAttributes(), principal.getName()).jsonBody();
    }
}
