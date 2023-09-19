package com.example.nlearn;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .requestMatchers("/admins-home/**").hasAuthority("ADMIN")
                .requestMatchers("/teachers-home/**").hasAnyAuthority("TEACHER", "ADMIN")
                .requestMatchers("/students-home/**").hasAuthority("STUDENT")
                .anyRequest().authenticated()
                .and()
                .logout()
                .clearAuthentication(true)
                .invalidateHttpSession(true)
                .logoutUrl("/logout")
                .and()
                .csrf().disable()
                .oauth2Login()
                .userInfoEndpoint()
                .and()
                .defaultSuccessUrl("http://localhost:3000", true)
                .and()
                .exceptionHandling().accessDeniedPage("/403");

        return http.build();
    }
}