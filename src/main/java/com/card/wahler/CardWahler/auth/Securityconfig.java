package com.card.wahler.CardWahler.auth;

import com.card.wahler.CardWahler.auth.filter.CustomeAuthorizationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class Securityconfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.authorizeRequests().antMatchers("/api/**").hasAnyAuthority(ROLE.ROLE_USER.toString(), ROLE.ROLE_MANAGER.toString());
        http.authorizeRequests().anyRequest().authenticated();
        http.addFilterBefore(new CustomeAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class);
    }

    public enum ROLE {
        ROLE_USER, ROLE_MANAGER, ROLE_ADMIN, ROLE_SUPER_ADMIN
    }

}


