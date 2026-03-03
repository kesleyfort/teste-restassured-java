package com.sicredi.api.spec;

import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

/**
 * Especificacoes reutilizaveis para requisicoes REST Assured.
 * Centraliza configuracoes comuns como content-type, headers e filtros.
 */
public final class RequestSpecs {

    private RequestSpecs() {
    }

    /**
     * Especificacao base com content-type JSON e filtro Allure para relatorios.
     *
     * @return RequestSpecification configurada com JSON e Allure
     */
    public static RequestSpecification baseSpec() {
        return new RequestSpecBuilder()
                .setContentType(ContentType.JSON)
                .setAccept(ContentType.JSON)
                .addFilter(new AllureRestAssured())
                .build();
    }

    /**
     * Especificacao autenticada com Bearer token.
     *
     * @param token JWT access token
     * @return RequestSpecification com header Authorization Bearer
     */
    public static RequestSpecification authenticatedSpec(String token) {
        return new RequestSpecBuilder()
                .addRequestSpecification(baseSpec())
                .addHeader("Authorization", "Bearer " + token)
                .build();
    }

    /**
     * Especificacao para envio de body JSON.
     *
     * @return RequestSpecification configurada para envio de JSON body
     */
    public static RequestSpecification jsonBodySpec() {
        return new RequestSpecBuilder()
                .setContentType(ContentType.JSON)
                .setAccept(ContentType.JSON)
                .addFilter(new AllureRestAssured())
                .build();
    }
}
