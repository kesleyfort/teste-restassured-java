package com.sicredi.api.service;

import com.sicredi.api.model.product.CreateProductRequest;
import com.sicredi.api.spec.RequestSpecs;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

/**
 * Servico para interacao com os endpoints de produtos da API.
 *
 * @see <a href="https://dummyjson.com/docs/products">Products API</a>
 */
public class ProductService {

    /**
     * Lista produtos com paginacao padrao.
     *
     * @return Response contendo a lista de produtos
     */
    public Response getProducts() {
        return given()
                .spec(RequestSpecs.baseSpec())
            .when()
                .get("/products");
    }

    /**
     * Lista produtos com parametros de paginacao customizados.
     *
     * @param limit quantidade maxima de produtos a retornar
     * @param skip  quantidade de registros a pular
     * @return Response contendo a lista de produtos paginada
     */
    public Response getProducts(int limit, int skip) {
        return given()
                .spec(RequestSpecs.baseSpec())
                .queryParam("limit", limit)
                .queryParam("skip", skip)
            .when()
                .get("/products");
    }

    /**
     * Busca um produto por ID.
     *
     * @param id identificador do produto
     * @return Response contendo os dados do produto ou erro 404
     */
    public Response getProductById(int id) {
        return given()
                .spec(RequestSpecs.baseSpec())
            .when()
                .get("/products/{id}", id);
    }

    /**
     * Cria um novo produto com os dados fornecidos.
     *
     * @param product POJO CreateProductRequest com dados do produto
     * @return Response contendo o produto criado
     */
    public Response createProduct(CreateProductRequest product) {
        return given()
                .spec(RequestSpecs.jsonBodySpec())
                .body(product)
            .when()
                .post("/products/add");
    }

    /**
     * Envia uma requisicao de criacao de produto com body vazio (JSON vazio).
     *
     * @return Response contendo a resposta da API
     */
    public Response createProductWithEmptyBody() {
        return given()
                .spec(RequestSpecs.jsonBodySpec())
                .body("{}")
            .when()
                .post("/products/add");
    }
}
