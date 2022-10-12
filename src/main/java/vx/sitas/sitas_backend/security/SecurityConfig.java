package vx.sitas.sitas_backend.security;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    public void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.csrf().disable().antMatcher("/**").authorizeRequests()
                .antMatchers("/me").authenticated()
                .and()
                .oauth2Login()
                .permitAll()
                .defaultSuccessUrl("http://localhost:4200/me")
                .and()
                .logout().logoutSuccessUrl("/");
    }

}
