package com.cnunodevs.serverfinanceapp.configuration.security.service;

import java.util.Optional;

import org.springframework.context.annotation.Primary;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.cnunodevs.serverfinanceapp.model.entity.Usuario;
import com.cnunodevs.serverfinanceapp.model.mapper.UserDetailsMapper;
import com.cnunodevs.serverfinanceapp.service.UsuariosService;

import lombok.RequiredArgsConstructor;

@Service
@Primary
@RequiredArgsConstructor
public class AppUserDetailsService implements UserDetailsService {

    private final UsuariosService usuariosService;
    private final UserDetailsMapper userDetailsMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        
        Optional<Usuario> usuario = usuariosService.findByUsername(username);

        if (!usuario.isPresent()) {
            throw new UsernameNotFoundException("User details not found: " + username);
        }

        return  userDetailsMapper.pojoToDto(usuario.get());
    }

    public Usuario loadUsuarioByUsername(String username) throws UsernameNotFoundException {
        
        Optional<Usuario> customerOptional = usuariosService.findByUsername(username);

        if (!customerOptional.isPresent()) {
            throw new UsernameNotFoundException("User details not found: " + username);
        }

        return  customerOptional.get();
    }


    
}