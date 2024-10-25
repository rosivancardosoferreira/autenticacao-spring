package com.rosivancardoso.auth_api.services.impl;

import com.rosivancardoso.auth_api.dtos.UsuarioDto;
import com.rosivancardoso.auth_api.models.Usuario;
import com.rosivancardoso.auth_api.repositories.UsuarioRepository;
import com.rosivancardoso.auth_api.services.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UsuarioServiceImpl implements UsuarioService {

    //implementaçao do video
    @Autowired // ver depois pra que isso serve, acho que deveria ser o construtor
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    //como eu conheco
    //private final UsuarioRepository usuarioRepository;
    //public UsuarioServiceImpl(UsuarioRepository usuarioRepository) {
      //  this.usuarioRepository = usuarioRepository;
    //}

    @Override
    public UsuarioDto salvar(UsuarioDto usuarioDto) {
        Usuario usuarioJaExiste = this.usuarioRepository.findByLogin(usuarioDto.login());

        if(usuarioJaExiste != null) {
            throw new RuntimeException("Usuario ja existe!");
        }

        var passwordHash = this.passwordEncoder.encode(usuarioDto.senha());

        Usuario entity = new Usuario(usuarioDto.nome(), usuarioDto.login(), passwordHash, usuarioDto.role());
        Usuario novoUsuario = usuarioRepository.save(entity);

        return new UsuarioDto(novoUsuario.getNome(), novoUsuario.getLogin(), novoUsuario.getSenha(), novoUsuario.getRole());

    }
}
