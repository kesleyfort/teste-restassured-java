package com.sicredi.api.tests;

import com.sicredi.api.base.BaseTest;
import com.sicredi.api.model.common.ErrorResponse;
import com.sicredi.api.model.product.ProductListResponse;
import com.sicredi.api.service.AuthService;
import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Testes para o endpoint autenticado GET /auth/products.
 *
 * @see <a href="https://dummyjson.com/docs/auth">Auth API</a>
 */
@Feature("Authenticated Products")
@DisplayName("GET /auth/products - Produtos Autenticados")
class AuthProductsTest extends BaseTest {

    private AuthService authService;

    @BeforeEach
    void setUp() {
        authService = new AuthService();
    }

    /**
     * Verifica que o endpoint /auth/products retorna 200 com lista de produtos
     * quando um token valido e fornecido.
     *
     * @see <a href="https://dummyjson.com/docs/auth">Auth API</a>
     */
    @Test
    @DisplayName("Deve retornar produtos com token valido")
    @Description("GET /auth/products com token valido retorna 200 com lista de produtos")
    void shouldReturnProductsWithValidToken() {
        String token = authService.getValidToken();

        ProductListResponse response = authService.getAuthProducts(token)
                .then()
                .statusCode(200)
                .extract()
                .as(ProductListResponse.class);

        assertThat(response.getProducts()).isNotEmpty();
        assertThat(response.getTotal()).isPositive();
    }

    /**
     * Verifica que o endpoint /auth/products retorna campos de paginacao
     * corretamente quando autenticado.
     *
     * @see <a href="https://dummyjson.com/docs/auth">Auth API</a>
     */
    @Test
    @DisplayName("Deve retornar campos de paginacao com token valido")
    @Description("Verifica que total, limit e skip estao presentes na resposta autenticada")
    void shouldReturnPaginationFieldsWithValidToken() {
        String token = authService.getValidToken();

        ProductListResponse response = authService.getAuthProducts(token)
                .then()
                .statusCode(200)
                .extract()
                .as(ProductListResponse.class);

        assertThat(response.getTotal()).isPositive();
        assertThat(response.getLimit()).isPositive();
        assertThat(response.getSkip()).isNotNull();
    }

    /**
     * Verifica que o endpoint /auth/products retorna 401 quando
     * nenhum token de autenticacao e fornecido.
     *
     * @see <a href="https://dummyjson.com/docs/auth">Auth API</a>
     */
    @Test
    @DisplayName("Deve retornar 401 sem token de autenticacao")
    @Description("GET /auth/products sem token retorna 401 com mensagem 'Access Token is required'")
    void shouldReturn401WithoutToken() {
        ErrorResponse error = authService.getAuthProductsWithoutToken()
                .then()
                .statusCode(401)
                .extract()
                .as(ErrorResponse.class);

        assertThat(error.getMessage()).containsIgnoringCase("Access Token is required");
    }

    /**
     * Verifica que o endpoint /auth/products retorna 401 quando
     * um token invalido e fornecido.
     *
     * @see <a href="https://dummyjson.com/docs/auth">Auth API</a>
     */
    @Test
    @DisplayName("Deve retornar 401 com token invalido")
    @Description("GET /auth/products com token invalido retorna 401 com mensagem de erro")
    void shouldReturn401WithInvalidToken() {
        ErrorResponse error = authService.getAuthProducts("invalid-token-123")
                .then()
                .statusCode(401)
                .extract()
                .as(ErrorResponse.class);

        assertThat(error.getMessage()).containsIgnoringCase("Invalid/Expired Token");
    }

    /**
     * Verifica que o endpoint /auth/products retorna 401 quando
     * o header Authorization contem Bearer com valor vazio.
     *
     * @see <a href="https://dummyjson.com/docs/auth">Auth API</a>
     */
    @Test
    @DisplayName("Deve retornar 401 com Bearer vazio")
    @Description("GET /auth/products com header 'Bearer ' vazio retorna 401")
    void shouldReturn401WithEmptyBearer() {
        authService.getAuthProductsWithEmptyBearer()
                .then()
                .statusCode(401);
    }

    /**
     * Verifica que o endpoint /auth/products retorna 401 quando
     * um token malformado (nao-JWT) e fornecido.
     *
     * @see <a href="https://dummyjson.com/docs/auth">Auth API</a>
     */
    @Test
    @DisplayName("Deve retornar 401 com token malformado")
    @Description("GET /auth/products com token que nao e JWT retorna 401")
    void shouldReturn401WithMalformedToken() {
        authService.getAuthProducts("not.a.jwt.token.at.all")
                .then()
                .statusCode(401);
    }

    /**
     * Valida que a resposta autenticada de produtos esta em conformidade
     * com o JSON Schema definido em product-list-response-schema.json.
     *
     * @see <a href="https://dummyjson.com/docs/auth">Auth API</a>
     */
    @Test
    @DisplayName("Deve estar em conformidade com o JSON Schema")
    @Description("Valida a resposta autenticada contra o JSON Schema de lista de produtos")
    void shouldMatchProductListJsonSchema() {
        String token = authService.getValidToken();

        authService.getAuthProducts(token)
                .then()
                .statusCode(200)
                .body(matchesJsonSchemaInClasspath("schemas/product-list-response-schema.json"));
    }
}
