package vx.sitas.sitas_backend.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import vx.sitas.sitas_backend.dto.internal.CustomOAuth2User;
import vx.sitas.sitas_backend.service.RequestService;

@RestController
public class RequestController {

    @Autowired
    RequestService requestService;

    @PostMapping("/request")
    public void postRequests(@RequestBody String[] songNames, @AuthenticationPrincipal DefaultOAuth2User principal) throws RuntimeException{
        try {
            requestService.createDownloadRequests(songNames, new CustomOAuth2User(principal.getAttributes(), principal.getName()).getIdentifier());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
