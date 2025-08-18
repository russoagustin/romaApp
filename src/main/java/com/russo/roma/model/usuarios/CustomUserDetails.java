package com.russo.roma.model.usuarios;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class CustomUserDetails implements UserDetails{

    private Integer id;
    private String email;
    private String password;

    private Collection<? extends GrantedAuthority> authorities;

    private Boolean activo;

        public CustomUserDetails(Integer id, String email, String password,
                             Collection<? extends GrantedAuthority> authorities, boolean activo) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.authorities = authorities;
        this.activo = activo;
    }

    public Integer getId(){
        return this.id;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public boolean isEnabled() {
		return this.activo;
	}

}
