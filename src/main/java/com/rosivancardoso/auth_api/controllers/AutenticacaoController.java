package com.rosivancardoso.auth_api.controllers;

import com.rosivancardoso.auth_api.dtos.AuthDto;
import com.rosivancardoso.auth_api.dtos.RequestRefreshDto;
import com.rosivancardoso.auth_api.dtos.TokenResponseDto;
import com.rosivancardoso.auth_api.services.AutenticacaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AutenticacaoController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private AutenticacaoService autenticacaoService;

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public TokenResponseDto auth(@RequestBody AuthDto authDto) {
        var usuarioAutentication = new UsernamePasswordAuthenticationToken(authDto.login(), authDto.senha());

        this.authenticationManager.authenticate(usuarioAutentication);

        return this.autenticacaoService.obterToken(authDto);
    }


    @PostMapping("/refresh-token")
    @ResponseStatus(HttpStatus.OK)
    public TokenResponseDto authRefreshToken(@RequestBody RequestRefreshDto requestRefreshDto) {
        return this.autenticacaoService.obterRefreshToken(requestRefreshDto.refreshToken());
    }
}
