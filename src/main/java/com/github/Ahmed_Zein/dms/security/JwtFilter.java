package com.github.Ahmed_Zein.dms.security;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.github.Ahmed_Zein.dms.models.dao.LocalUserDAO;
import com.github.Ahmed_Zein.dms.services.JWTService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;

@Component
public class JwtFilter extends OncePerRequestFilter {
    private final LocalUserDAO localUserDAO;
    private final JWTService jwtService;

    public JwtFilter(LocalUserDAO localUserDAO, JWTService jwtService) {
        this.localUserDAO = localUserDAO;
        this.jwtService = jwtService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String header = request.getHeader("Authorization");
        String bearer = "Bearer ";
        if (header == null || !header.contains(bearer)) {
            filterChain.doFilter(request, response);
            return;
        }
        String token = header.substring(bearer.length());
        try {
            String email = jwtService.getUserEmail(token);
            var opUser = localUserDAO.findByEmailIgnoreCase(email);
            if (opUser.isEmpty()) {
                filterChain.doFilter(request, response);
                return;
            }
            var user = opUser.get();
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(user, null, new ArrayList<>());
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } catch (JWTVerificationException e) {
            System.err.println("JWT ERR");
        } finally {
            filterChain.doFilter(request, response);
        }
    }
}
