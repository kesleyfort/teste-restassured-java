package com.sicredi.api.spec;

import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.ResponseSpecification;

/**
 * Especificacoes reutilizaveis para validacao de respostas REST Assured.
 * Centraliza validacoes comuns de status code e content-type.
 */
public final class ResponseSpecs {

    private ResponseSpecs() {
    }

    /**
     * Especificacao para resposta 200 OK com JSON.
     *
     * @return ResponseSpecification esperando status 200 e content-type JSON
     */
    public static ResponseSpecification ok() {
        return new ResponseSpecBuilder()
                .expectStatusCode(200)
                .expectContentType(ContentType.JSON)
                .build();
    }

    /**
     * Especificacao para resposta 201 Created com JSON.
     *
     * @return ResponseSpecification esperando status 201 e content-type JSON
     */
    public static ResponseSpecification created() {
        return new ResponseSpecBuilder()
                .expectStatusCode(201)
                .expectContentType(ContentType.JSON)
                .build();
    }

    /**
     * Especificacao para resposta 400 Bad Request com JSON.
     *
     * @return ResponseSpecification esperando status 400 e content-type JSON
     */
    public static ResponseSpecification badRequest() {
        return new ResponseSpecBuilder()
                .expectStatusCode(400)
                .expectContentType(ContentType.JSON)
                .build();
    }

    /**
     * Especificacao para resposta 401 Unauthorized com JSON.
     *
     * @return ResponseSpecification esperando status 401 e content-type JSON
     */
    public static ResponseSpecification unauthorized() {
        return new ResponseSpecBuilder()
                .expectStatusCode(401)
                .expectContentType(ContentType.JSON)
                .build();
    }

    /**
     * Especificacao para resposta 404 Not Found com JSON.
     *
     * @return ResponseSpecification esperando status 404 e content-type JSON
     */
    public static ResponseSpecification notFound() {
        return new ResponseSpecBuilder()
                .expectStatusCode(404)
                .expectContentType(ContentType.JSON)
                .build();
    }
}
