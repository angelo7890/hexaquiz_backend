package com.hexaquiz.security;

import com.hexaquiz.enums.UserTypeEnum;
import com.hexaquiz.model.UserModel;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.UUID;

public class UserPrincipal implements UserDetails {

    private final UserModel user;

    public UserPrincipal(UserModel user) {
        this.user = user;
    }
    public UUID getId() {
        return user.getId();
    }

    public String getname(){
        return user.getName();
    }

    public UserTypeEnum getType() {
        return user.getType();
    }

    public String getEmail(){
        return user.getEmail();
    }

    public String getProfileImage(){
        return user.getProfileImage();
    }

    public LocalDateTime getCreatedAt(){
        return user.getCreatedAt();
    }
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return user.getAuthorities();
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
