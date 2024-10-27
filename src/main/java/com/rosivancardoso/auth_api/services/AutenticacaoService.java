package com.rosivancardoso.auth_api.services;

import com.rosivancardoso.auth_api.dtos.AuthDto;
import com.rosivancardoso.auth_api.dtos.TokenResponseDto;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface AutenticacaoService extends UserDetailsService {

    public TokenResponseDto obterToken(AuthDto authDto);

    public String validaTokenJwt(String token);

    TokenResponseDto obterRefreshToken(String refreshToken);
}
