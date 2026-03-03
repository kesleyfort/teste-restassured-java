package com.sicredi.api.service;

import com.sicredi.api.spec.RequestSpecs;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

/**
 * Servico para interacao com o endpoint de health check da API.
 *
 * @see <a href="https://dummyjson.com/docs">DummyJSON API - Test</a>
 */
public class HealthService {

    /**
     * Realiza uma requisicao GET /test para verificar o status da API.
     *
     * @return Response contendo o status da API
     */
    public Response getHealthStatus() {
        return given()
                .spec(RequestSpecs.baseSpec())
            .when()
                .get("/test");
    }
}
