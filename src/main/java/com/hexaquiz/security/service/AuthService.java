package com.hexaquiz.security.service;

import com.hexaquiz.dto.request.RequestLoginDto;
import com.hexaquiz.dto.response.ResponseLoginDto;
import com.hexaquiz.dto.response.ResponseUserDto;
import com.hexaquiz.dto.tokens.Tokens;
import com.hexaquiz.security.UserPrincipal;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final AuthorizationService authorizationService;
    public AuthService(AuthenticationManager authenticationManager, JwtService jwtService, AuthorizationService authorizationService) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.authorizationService = authorizationService;
    }

    public ResponseLoginDto login(RequestLoginDto dto){
        try{
            var usernamePassword = new UsernamePasswordAuthenticationToken(dto.username(), dto.password());
            var auth = authenticationManager.authenticate(usernamePassword);

            if(auth.isAuthenticated()){
                UserPrincipal user = (UserPrincipal) auth.getPrincipal();
                Tokens t = jwtService.getJwtUserToken(user);
                return new ResponseLoginDto(
                        t, new ResponseUserDto(
                                user.getId().toString(),
                                user.getname(),
                                user.getUsername(),
                                user.getProfileImage(),
                                user.getCreatedAt(),
                                user.getType() )
                );
            }
            throw new BadCredentialsException("Invalid username or password");
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    public Tokens getAccessTokenFromRefreshToken(String usernameFromRefreshToken){
        if(usernameFromRefreshToken==null || usernameFromRefreshToken.isEmpty()){
            throw new BadCredentialsException("Invalid username");
        }
        UserPrincipal user  = (UserPrincipal) authorizationService.loadUserByUsername(usernameFromRefreshToken);
        return jwtService.getJwtUserToken(user);
    }

}
