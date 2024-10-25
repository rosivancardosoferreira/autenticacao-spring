package com.rosivancardoso.auth_api.services;

import com.rosivancardoso.auth_api.dtos.AuthDto;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface AutenticacaoService extends UserDetailsService {

    public String obterToken(AuthDto authDto);

    public String validaTokenJwt(String token);
}
