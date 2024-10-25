package com.rosivancardoso.auth_api.services.impl;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.rosivancardoso.auth_api.dtos.AuthDto;
import com.rosivancardoso.auth_api.models.Usuario;
import com.rosivancardoso.auth_api.repositories.UsuarioRepository;
import com.rosivancardoso.auth_api.services.AutenticacaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;


@Service
public class AutenticacaoServiceImpl implements AutenticacaoService {


    @Autowired
    private UsuarioRepository usuarioRepository;


    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        return this.usuarioRepository.findByLogin(login);
    }

    @Override
    public String obterToken(AuthDto authDto) {

        Usuario usuario = this.usuarioRepository.findByLogin(authDto.login());

        return this.geraTokenJwt(usuario);

    }

    public String geraTokenJwt(Usuario usuario) {
        try {

            //algoritmo que sera utilizado na geracao do token
            Algorithm algorithm = Algorithm.HMAC256("my-secret"); //precisa definir na aplicacao  e precisa ficar muito segura

            return JWT.create()
                    .withIssuer("auth-api")
                    .withSubject(usuario.getLogin())
                    .withExpiresAt(this.gerarDataExpiracao())
                    .sign(algorithm);

        } catch (JWTCreationException exception) {
                throw new RuntimeException("Erro ao tentar gerar o token: " + exception.getMessage());
        }
    }

    public String validaTokenJwt(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256("my-secret"); //precisa definir na aplicacao  e precisa ficar muito segura
            return JWT.require(algorithm)
                    .withIssuer("auth-api")
                    .build()
                    .verify(token)
                    .getSubject();

        } catch (JWTVerificationException exception) {
            return ""; // se nao conseguiu fazer a validação do token;
        }
    }

    private Instant gerarDataExpiracao() {
        return LocalDateTime
                .now()
                .plusHours(8)
                .toInstant(ZoneOffset.of("-03:00"));
    }
}
