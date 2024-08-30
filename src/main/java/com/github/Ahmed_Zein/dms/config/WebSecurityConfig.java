package com.github.Ahmed_Zein.dms.config;

import com.github.Ahmed_Zein.dms.security.JwtFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfigurationSource;

@Configuration
public class WebSecurityConfig {
    private final JwtFilter jwtFilter;
    private final CorsConfigurationSource corsConfigurationSource;

    public WebSecurityConfig(JwtFilter jwtFilter, CorsConfigurationSource corsConfigurationSource) {
        this.jwtFilter = jwtFilter;
        this.corsConfigurationSource = corsConfigurationSource;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable);
        http.cors(httpSecurityCorsConfigurer -> httpSecurityCorsConfigurer.configurationSource(corsConfigurationSource));
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
