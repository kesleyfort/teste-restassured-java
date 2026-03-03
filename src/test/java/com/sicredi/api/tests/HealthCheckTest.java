package com.sicredi.api.tests;

import com.sicredi.api.base.BaseTest;
import com.sicredi.api.model.common.TestStatusResponse;
import com.sicredi.api.service.HealthService;
import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Testes para o endpoint de health check GET /test.
 *
 * @see <a href="https://dummyjson.com/docs">DummyJSON API</a>
 */
@Feature("Health Check")
@DisplayName("GET /test - Health Check")
class HealthCheckTest extends BaseTest {

    private HealthService healthService;

    @BeforeEach
    void setUp() {
        healthService = new HealthService();
    }

    /**
     * Verifica que o endpoint /test retorna status 200 e body com status "ok"
     * e method "GET", confirmando que a API esta disponivel.
     *
     * @see <a href="https://dummyjson.com/docs">DummyJSON API</a>
     */
    @Test
    @DisplayName("Deve retornar status 200 com status ok e method GET")
    @Description("Verifica que a API esta disponivel e respondendo corretamente")
    void shouldReturnOkStatusAndGetMethod() {
        Response response = healthService.getHealthStatus();

        TestStatusResponse body = response.then()
                .statusCode(200)
                .extract()
                .as(TestStatusResponse.class);

        assertThat(body.getStatus()).isEqualTo("ok");
        assertThat(body.getMethod()).isEqualTo("GET");
    }

    /**
     * Verifica que o content-type da resposta do endpoint /test e JSON.
     *
     * @see <a href="https://dummyjson.com/docs">DummyJSON API</a>
     */
    @Test
    @DisplayName("Deve retornar content-type JSON")
    @Description("Verifica que o content-type da resposta e application/json")
    void shouldReturnJsonContentType() {
        Response response = healthService.getHealthStatus();

        response.then()
                .statusCode(200)
                .contentType("application/json");
    }

    /**
     * Valida que a resposta do endpoint /test esta em conformidade
     * com o JSON Schema definido em test-response-schema.json.
     *
     * @see <a href="https://dummyjson.com/docs">DummyJSON API</a>
     */
    @Test
    @DisplayName("Deve estar em conformidade com o JSON Schema")
    @Description("Valida a resposta contra o JSON Schema definido para o endpoint /test")
    void shouldMatchJsonSchema() {
        healthService.getHealthStatus()
                .then()
                .statusCode(200)
                .body(matchesJsonSchemaInClasspath("schemas/test-response-schema.json"));
    }
}
