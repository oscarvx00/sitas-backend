package vx.sitas.sitas_backend.controller;


import vx.sitas.sitas_backend.dto.internal.CustomOAuth2User;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
//@CrossOrigin(origins = "*", allowedHeaders = "*")

public class UserController {
    @GetMapping("/me")
    public Map<String, String> user(@AuthenticationPrincipal DefaultOAuth2User principal) throws Exception{
        return new CustomOAuth2User(principal.getAttributes(), principal.getName()).jsonBody();
    }
}
