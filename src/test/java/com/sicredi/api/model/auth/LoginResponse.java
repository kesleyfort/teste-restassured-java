package com.sicredi.api.model.auth;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * POJO para a resposta de login da API.
 * Contem dados do usuario autenticado e tokens de acesso.
 *
 * @see <a href="https://dummyjson.com/docs/auth">Auth API</a>
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class LoginResponse {

    private Integer id;
    private String username;
    private String email;
    private String firstName;
    private String lastName;
    private String gender;
    private String image;
    private String accessToken;
    private String refreshToken;

    /** @return o ID do usuario autenticado */
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    /** @return o username do usuario autenticado */
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    /** @return o email do usuario autenticado */
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    /** @return o primeiro nome do usuario */
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /** @return o sobrenome do usuario */
    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /** @return o genero do usuario */
    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    /** @return a URL da imagem do usuario */
    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    /** @return o token JWT de acesso */
    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    /** @return o token de refresh para renovar o acesso */
    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}
