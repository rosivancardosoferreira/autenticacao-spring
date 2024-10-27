package com.rosivancardoso.auth_api.services.impl;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.rosivancardoso.auth_api.dtos.AuthDto;
import com.rosivancardoso.auth_api.dtos.TokenResponseDto;
import com.rosivancardoso.auth_api.models.Usuario;
import com.rosivancardoso.auth_api.repositories.UsuarioRepository;
import com.rosivancardoso.auth_api.services.AutenticacaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;


@Service
public class AutenticacaoServiceImpl implements AutenticacaoService {

    @Value("${auth.jwt.token.secret}")
    private String secretKey;

    @Value("${auth.jwt.token.expiration}")
    private Integer horaExpiracaoToken;

    @Value("${auth.jwt.refresh-token.expiration}")
    private Integer horaExpiracaoRefreshToken;


    @Autowired
    private UsuarioRepository usuarioRepository;


    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        return this.usuarioRepository.findByLogin(login);
    }

    @Override
    public TokenResponseDto obterToken(AuthDto authDto) {

        Usuario usuario = this.usuarioRepository.findByLogin(authDto.login());

        return TokenResponseDto
                .builder()
                .token(this.geraTokenJwt(usuario, this.horaExpiracaoToken))
                .refreshToken(this.geraTokenJwt(usuario, this.horaExpiracaoRefreshToken))
                .build();
    }

    public String geraTokenJwt(Usuario usuario, Integer expiration) {
        try {

            //algoritmo que sera utilizado na geracao do token
            Algorithm algorithm = Algorithm.HMAC256("my-secret"); //precisa definir na aplicacao  e precisa ficar muito segura

            return JWT.create()
                    .withIssuer(this.secretKey)
                    .withSubject(usuario.getLogin())
                    .withExpiresAt(this.gerarDataExpiracao(expiration))
                    .sign(algorithm);

        } catch (JWTCreationException exception) {
                throw new RuntimeException("Erro ao tentar gerar o token: " + exception.getMessage());
        }
    }

    public String validaTokenJwt(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256("my-secret"); //precisa definir na aplicacao  e precisa ficar muito segura
            return JWT.require(algorithm)
                    .withIssuer(this.secretKey)
                    .build()
                    .verify(token)
                    .getSubject();

        } catch (JWTVerificationException exception) {
            return ""; // se nao conseguiu fazer a validação do token;
        }
    }

    @Override
    public TokenResponseDto obterRefreshToken(String refreshToken) {

        String login = this.validaTokenJwt(refreshToken);
        Usuario usuario = this.usuarioRepository.findByLogin(login);

        if( usuario == null) {
            throw new RuntimeException("Falhou ao gerar refresh token"); // o mais adequaldo é a unauthorized
        }

        var autentication = new UsernamePasswordAuthenticationToken(usuario, null, usuario.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(autentication);

        return TokenResponseDto
                .builder()
                .token(this.geraTokenJwt(usuario, this.horaExpiracaoToken))
                .refreshToken(this.geraTokenJwt(usuario, this.horaExpiracaoRefreshToken))
                .build();
    }

    private Instant gerarDataExpiracao(Integer expiration) {
        return LocalDateTime
                .now()
                .plusHours(expiration) //hard code 8 horas
                .toInstant(ZoneOffset.of("-04:00")); // amazonas
    }
}
