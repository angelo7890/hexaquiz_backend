package com.hexaquiz.security.component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hexaquiz.dto.error.ResponseError;
import com.hexaquiz.security.UserPrincipal;
import com.hexaquiz.security.service.AuthorizationService;
import com.hexaquiz.security.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class SecurityFilter extends OncePerRequestFilter {
    private final AuthorizationService authorizationService;
    private final JwtService jwtService;
    private final ObjectMapper objectMapper;

    public SecurityFilter(AuthorizationService authorizationService, JwtService jwtService, ObjectMapper objectMapper) {
        this.authorizationService = authorizationService;
        this.jwtService = jwtService;
        this.objectMapper = objectMapper;
    }
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        var token = this.recoverToken(request);
        System.out.println("token: " + token);
        if(token != null) {
            var login = jwtService.validateToken(token);
            System.out.println("login: " + login);
            if (login.isEmpty()){
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                response.setContentType("application/json");
                ResponseError responseError = new ResponseError(
                        "Token de acesso invalido",
                        HttpStatus.UNAUTHORIZED
                );
                response.getWriter().write(objectMapper.writeValueAsString(responseError));
                return;
            }
            String typeFromToken = jwtService.getTypeFromToken(token);
            if (typeFromToken.equals("refresh_token") && !request.getRequestURI().equals("/hexaquiz/user/refresh")) {
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                response.setContentType("application/json");
                ResponseError responseError = new ResponseError(
                        "Token de acesso invalido",
                        HttpStatus.UNAUTHORIZED
                );
                response.getWriter().write(objectMapper.writeValueAsString(responseError));
                return;
            }
            UserPrincipal user = (UserPrincipal) authorizationService.loadUserByUsername(login);

            var authentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());

            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        filterChain.doFilter(request, response);
    }


    private String recoverToken(HttpServletRequest request) {
        var authHeader = request.getHeader("Authorization");
        if (authHeader == null ){
            return null;
        }
        return authHeader.replace("Bearer ", "");
    }
}
