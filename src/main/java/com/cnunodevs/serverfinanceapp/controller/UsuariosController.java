package com.cnunodevs.serverfinanceapp.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cnunodevs.serverfinanceapp.model.dto.UsuarioDTO;
import com.cnunodevs.serverfinanceapp.model.entity.Usuario;
import com.cnunodevs.serverfinanceapp.model.mapper.UsuarioMapper;
import com.cnunodevs.serverfinanceapp.service.UsuariosService;

import lombok.RequiredArgsConstructor;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/usuarios")
public class UsuariosController {

    private final UsuariosService usuariosService;
    private final UsuarioMapper usuarioMapper;

    @PostMapping("/new-user")
    public ResponseEntity<HttpStatus> registerNewUser(@RequestBody UsuarioDTO usuarioDTO){
        final Usuario usuario = usuarioMapper.dtoToPojo(usuarioDTO);
        usuariosService.createUsuario(usuario);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping
    public ResponseEntity<Boolean> usernameAlreadyExist(@RequestParam String username){
        final Boolean alreadyExist = usuariosService.usernameAlreadyExist(username);
        return ResponseEntity.status(HttpStatus.OK).body(alreadyExist);
    }
    
}
