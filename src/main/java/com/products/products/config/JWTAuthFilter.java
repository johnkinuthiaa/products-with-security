package com.products.products.config;

import com.products.products.service.JWTUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;

@Component
public class JWTAuthFilter extends OncePerRequestFilter {
    private final HandlerExceptionResolver handlerExceptionResolver;
    private final JWTUtils jwtUtils;
    private final UserDetailsService userDetailsService;

    public JWTAuthFilter(HandlerExceptionResolver handlerExceptionResolver,
                         JWTUtils jwtUtils,
                         UserDetailsService userDetailsService) {
        this.handlerExceptionResolver = handlerExceptionResolver;
        this.jwtUtils = jwtUtils;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {
        final String authHeader =request.getHeader("Authorization");
        if(authHeader ==null ||authHeader.startsWith("Bearer ")){
            filterChain.doFilter(request,response);
            return;
        }
//        try {
//
//        }
        try {
            final var jwt =authHeader.substring(7);
            final var userEmail =jwtUtils.extractUserNameFromToken(jwt);

            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if(userEmail !=null &&authentication ==null){
                UserDetails userDetails=this.userDetailsService.loadUserByUsername(userEmail);
                if(jwtUtils.isTokenValid(jwt,userDetails)){
                    UsernamePasswordAuthenticationToken authenticationToken =new UsernamePasswordAuthenticationToken(
                            userDetails,null,userDetails.getAuthorities()
                    );
                    authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                }
            }
            filterChain.doFilter(request,response);
        }catch (Exception e){
            handlerExceptionResolver.resolveException(request,response,null,e);
        }

    }
}
