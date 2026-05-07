package com.hexaquiz.security.service;

import com.hexaquiz.model.UserModel;
import com.hexaquiz.repository.UserRepository;
import com.hexaquiz.security.UserPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AuthorizationService implements UserDetailsService {

    private final UserRepository userRepository;

    public AuthorizationService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserModel user = userRepository.findByusername(username);

        if(user == null) {
            throw new UsernameNotFoundException("user nao encontrado");
        }
        return new UserPrincipal(user);
    }
}
