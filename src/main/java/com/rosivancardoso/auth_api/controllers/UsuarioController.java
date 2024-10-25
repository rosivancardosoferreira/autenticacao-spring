package com.rosivancardoso.auth_api.controllers;

import com.rosivancardoso.auth_api.dtos.UsuarioDto;
import com.rosivancardoso.auth_api.models.Usuario;
import com.rosivancardoso.auth_api.services.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController  {

    //implementaçao do video
    @Autowired
    private UsuarioService usuarioService;

    //como eu conheco
    /* private final UsuarioService usuarioService;
    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }*/

    @PostMapping
    private UsuarioDto salvar(@RequestBody UsuarioDto usuarioDto ) {
        return this.usuarioService.salvar(usuarioDto);
    }

    @GetMapping("/admin")
    private String getAdmin() {
        return "permissão de administrador";
    }

    @GetMapping("/user")
    private String getUser() {
        return "permissão de usuario";
    }

}
