package com.sicredi.api.service;

import com.sicredi.api.spec.RequestSpecs;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

/**
 * Servico para interacao com os endpoints de usuarios da API.
 *
 * @see <a href="https://dummyjson.com/docs/users">Users API</a>
 */
public class UserService {

    /**
     * Lista usuarios com paginacao padrao.
     *
     * @return Response contendo a lista de usuarios
     */
    public Response getUsers() {
        return given()
                .spec(RequestSpecs.baseSpec())
            .when()
                .get("/users");
    }

    /**
     * Lista usuarios com parametros de paginacao customizados.
     *
     * @param limit quantidade maxima de usuarios a retornar
     * @param skip  quantidade de registros a pular
     * @return Response contendo a lista de usuarios paginada
     */
    public Response getUsers(int limit, int skip) {
        return given()
                .spec(RequestSpecs.baseSpec())
                .queryParam("limit", limit)
                .queryParam("skip", skip)
            .when()
                .get("/users");
    }
}
