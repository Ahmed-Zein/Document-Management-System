package com.github.Ahmed_Zein.dms.config;

import com.github.Ahmed_Zein.dms.security.JwtFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.AuthorizationFilter;

@Configuration
public class WebSecurityConfig {
    private final JwtFilter jwtFilter;

    public WebSecurityConfig(JwtFilter jwtFilter) {
        this.jwtFilter = jwtFilter;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable);
        http.addFilterBefore(jwtFilter, AuthorizationFilter.class);

        http.authorizeHttpRequests((authorize) -> authorize
                .requestMatchers(
                        "/v*/health",
                        "/v*/auth/**",
                        "/v*/auth",
                        "/swagger-resources/**",
                        "/v3/api-docs/**",
                        "/swagger-ui/**",
                        "/webjars/**"
                ).permitAll()
                .anyRequest().authenticated());
        return http.build();
    }
}
