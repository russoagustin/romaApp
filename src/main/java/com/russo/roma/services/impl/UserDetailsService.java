package com.russo.roma.services.impl;

import java.util.HashSet;
import java.util.Set;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.russo.roma.model.usuarios.Usuario;
import com.russo.roma.repositories.interfaces.IUsuarioRepository;

@Service
public class UserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService{
 
    private IUsuarioRepository usuarioRepo;

    public UserDetailsService(IUsuarioRepository usuarioRepo){
        this.usuarioRepo = usuarioRepo;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepo.buscarPorEmail(email)
            .orElseThrow(()-> new UsernameNotFoundException("El email: "+ email + " no está registrado"));

        Set<SimpleGrantedAuthority> authorities = new HashSet<>();

        usuario.getRoles().forEach(r->{
            authorities.add(new SimpleGrantedAuthority(r.name()));
        });

        User user = new User(email, 
            usuario.getPassword(), 
            usuario.isActivo(), 
            true, 
            true, 
            true, 
            authorities);
            
        return user;
    }
    

}
