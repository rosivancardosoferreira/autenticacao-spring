package com.rosivancardoso.auth_api.dtos;


import lombok.Builder;

@Builder
public record TokenResponseDto(String token, String refreshToken) {

    /*
    // Método para iniciar a construção
    public static TokenResponseDtoBuilder builder() {
        return new TokenResponseDtoBuilder();
    }

    // Classe interna para o padrão Builder
    public static class TokenResponseDtoBuilder {
        private String token;
        private String refreshToken;

        public TokenResponseDtoBuilder token(String token) {
            this.token = token;
            return this;
        }

        public TokenResponseDtoBuilder refreshToken(String refreshToken) {
            this.refreshToken = refreshToken;
            return this;
        }

        public TokenResponseDto build() {
            return new TokenResponseDto(token, refreshToken);
        }
    }

     */
}
