package com.sicredi.api.service;

import com.sicredi.api.data.TestDataFactory;
import com.sicredi.api.model.auth.LoginRequest;
import com.sicredi.api.spec.RequestSpecs;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

/**
 * Servico para interacao com os endpoints de autenticacao da API.
 *
 * @see <a href="https://dummyjson.com/docs/auth">Auth API</a>
 */
public class AuthService {

    /**
     * Realiza login na API com as credenciais fornecidas.
     *
     * @param request POJO LoginRequest com credenciais
     * @return Response contendo dados do usuario e tokens
     */
    public Response login(LoginRequest request) {
        return given()
                .spec(RequestSpecs.jsonBodySpec())
                .body(request)
            .when()
                .post("/auth/login");
    }

    /**
     * Realiza login com credenciais validas e retorna um token de acesso.
     *
     * @return token JWT de acesso (accessToken)
     * @throws AssertionError se o login falhar
     */
    public String getValidToken() {
        return login(TestDataFactory.validLoginRequest())
                .then()
                .statusCode(200)
                .extract()
                .path("accessToken");
    }

    /**
     * Realiza uma requisicao GET /auth/products com o token fornecido.
     *
     * @param token JWT access token
     * @return Response contendo a lista de produtos autenticada
     */
    public Response getAuthProducts(String token) {
        return given()
                .spec(RequestSpecs.authenticatedSpec(token))
            .when()
                .get("/auth/products");
    }

    /**
     * Realiza uma requisicao GET /auth/products sem token de autenticacao.
     *
     * @return Response contendo o erro de autenticacao
     */
    public Response getAuthProductsWithoutToken() {
        return given()
                .spec(RequestSpecs.baseSpec())
            .when()
                .get("/auth/products");
    }

    /**
     * Realiza uma requisicao GET /auth/products com header Authorization vazio.
     *
     * @return Response contendo o erro de autenticacao
     */
    public Response getAuthProductsWithEmptyBearer() {
        return given()
                .spec(RequestSpecs.baseSpec())
                .header("Authorization", "Bearer ")
            .when()
                .get("/auth/products");
    }
}
