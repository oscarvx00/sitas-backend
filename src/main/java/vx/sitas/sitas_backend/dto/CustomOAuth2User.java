package vx.sitas.sitas_backend.dto;

import net.minidev.json.JSONObject;
import netscape.javascript.JSObject;
import org.apache.tomcat.util.descriptor.web.MultipartDef;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.*;

public class CustomOAuth2User {


    private String identifier;
    private String email;
    private String username;

    enum Provider {
        GOOGLE,
        GITHUB,
        DISCORD
    }

    public CustomOAuth2User(Map<String, Object> attributes, String name) throws Exception{
        this.username = name;
        this.email = Objects.toString(attributes.get("email"), "");
        this.identifier = generateIdentifier(name,attributes);
    }

    private String generateIdentifier(String username, Map<String, Object> attributes) throws Exception{
        Provider provider = getProviderName(attributes);
        return provider.toString() + "_" + username;
    }

    private Provider getProviderName(Map<String, Object> attributes) throws Exception{
        if(checkGoogle(attributes)) return Provider.GOOGLE;
        else if(checkGithub(attributes))    return Provider.GITHUB;
        else if(checkDiscord(attributes)) return Provider.DISCORD;
        else throw new Exception("Cannot identify provider");
    }

    private boolean checkGoogle(Map<String, Object> attributes){
        return attributes.containsKey("at_hash");
    }

    private boolean checkGithub(Map<String, Object> attributes) {
        return attributes.containsKey("public_repos")
                || attributes.containsKey("gists_url");
    }

    private boolean checkDiscord(Map<String, Object> attributes) {
        return attributes.containsKey("discriminator")
                || attributes.containsKey("premium_type");
    }

    @Override
    public String toString() {
        return "CustomOAuth2User{" +
                "identifier='" + identifier + '\'' +
                ", email='" + email + '\'' +
                ", username='" + username + '\'' +
                '}';
    }

    public Map<String, String> jsonBody() {
        HashMap<String, String> map = new HashMap<>();
        map.put("id", identifier);
        map.put("email", email);
        map.put("username", username);

        return map;
    }

    public String getIdentifier() {
        return identifier;
    }

    public String getEmail() {
        return email;
    }

    public String getUsername() {
        return username;
    }
}
