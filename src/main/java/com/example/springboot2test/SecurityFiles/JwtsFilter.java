package com.example.springboot2test.SecurityFiles;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtsFilter extends OncePerRequestFilter{
    @Autowired
    JwtsUtil jwtsUtil;

    @Autowired
    C_UserDetailsService c_UserDetailsService;

    @SuppressWarnings("null")
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
        throws ServletException, IOException {
        String authHead = request.getHeader("Authorization");
        if(authHead == null || !authHead.startsWith("Bearer ")){
            filterChain.doFilter(request, response);
            return;
        }
        String token = authHead.substring(7);
        try {
            String email = jwtsUtil.extractToken(token); 
            if(email != null && SecurityContextHolder.getContext().getAuthentication() == null){
                UserDetails userDetails = c_UserDetailsService.loadUserByUsername(email);
                if(jwtsUtil.validateToken(token, userDetails)){
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails, 
                        null,
                        userDetails.getAuthorities()
                    );
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write("{\"error\": \"Invalid or expired JWT token\"}");
            return; 
        }
        filterChain.doFilter(request, response);
    }
    
}
