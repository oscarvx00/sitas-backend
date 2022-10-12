package com.example.demo.dto;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;

import java.util.HashMap;
import java.util.Map;

public class CustomOAuth2UserTest {

    private final Map<String, Object> GOOGLE_DATA = new HashMap(){{
        put("at_hash", "dummy");
        put("email", "mail@mail.com");
    }};

    private final Map<String, Object> GITHUB_DATA = new HashMap(){{
        put("punblic_repos", "dummy");
        put("gists_url", "dummy");
        put("email", "mail@mail.com");
    }};

    private final Map<String, Object> DISCORD_DATA = new HashMap(){{
        put("discriminator", "dummy");
        put("premium_type", "dummy");
        put("email", "mail@mail.com");
    }};

    private final Map<String, Object> BAD_DATA = new HashMap(){{
        put("dummy", "dummy");
    }};

    @Test
    void checkGoogleOK() throws Exception{
        CustomOAuth2User customOAuth2UserTest = new CustomOAuth2User(GOOGLE_DATA, "username");

        Assertions.assertEquals(customOAuth2UserTest.getIdentifier(), "GOOGLE_username");
        Assertions.assertEquals(customOAuth2UserTest.getEmail(), "mail@mail.com");
        Assertions.assertEquals(customOAuth2UserTest.getUsername(), "username");
    }

    @Test
    void checkGithubOK() throws Exception{
        CustomOAuth2User customOAuth2UserTest = new CustomOAuth2User(GITHUB_DATA, "username");

        Assertions.assertEquals(customOAuth2UserTest.getIdentifier(), "GITHUB_username");
        Assertions.assertEquals(customOAuth2UserTest.getEmail(), "mail@mail.com");
        Assertions.assertEquals(customOAuth2UserTest.getUsername(), "username");
    }

    @Test
    void checkDiscordOK() throws Exception{
        CustomOAuth2User customOAuth2UserTest = new CustomOAuth2User(DISCORD_DATA, "username");

        Assertions.assertEquals(customOAuth2UserTest.getIdentifier(), "DISCORD_username");
        Assertions.assertEquals(customOAuth2UserTest.getEmail(), "mail@mail.com");
        Assertions.assertEquals(customOAuth2UserTest.getUsername(), "username");
    }

    @Test
    void checkProviderNotFound() {
        Assertions.assertThrows(Exception.class, () -> {
            CustomOAuth2User customOAuth2UserTest = new CustomOAuth2User(BAD_DATA, "username");
        });
    }
}
