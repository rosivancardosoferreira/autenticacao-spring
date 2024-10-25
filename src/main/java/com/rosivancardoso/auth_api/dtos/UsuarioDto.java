package com.rosivancardoso.auth_api.dtos;

import com.rosivancardoso.auth_api.enums.RoleEnum;

public record UsuarioDto(
        String nome,
        String login,
        String senha,
        RoleEnum role
) { }
