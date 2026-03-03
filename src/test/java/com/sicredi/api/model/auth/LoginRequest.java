package com.sicredi.api.model.auth;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * POJO para o corpo da requisicao de login na API.
 *
 * @see <a href="https://dummyjson.com/docs/auth">Auth API</a>
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LoginRequest {

    private String username;
    private String password;
    private Integer expiresInMins;

    public LoginRequest() {
    }

    public LoginRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public LoginRequest(String username, String password, Integer expiresInMins) {
        this.username = username;
        this.password = password;
        this.expiresInMins = expiresInMins;
    }

    /** @return o nome de usuario para autenticacao */
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    /** @return a senha do usuario */
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    /** @return tempo de expiracao do token em minutos, ou null para usar o padrao */
    public Integer getExpiresInMins() {
        return expiresInMins;
    }

    public void setExpiresInMins(Integer expiresInMins) {
        this.expiresInMins = expiresInMins;
    }
}
