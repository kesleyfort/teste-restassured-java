package com.sicredi.api.base;

import com.sicredi.api.config.Configuration;
import io.restassured.RestAssured;
import io.restassured.parsing.Parser;
import org.junit.jupiter.api.BeforeAll;

/**
 * Classe base para todos os testes de API.
 * Configura o REST Assured com URL base, parser JSON padrao e logging.
 */
public abstract class BaseTest {

    /**
     * Configuracao executada uma unica vez antes de todos os testes.
     * Define a URL base, parser padrao e habilita logging de requisicoes com falha.
     */
    @BeforeAll
    static void setupRestAssured() {
        Configuration config = Configuration.getInstance();
        RestAssured.baseURI = config.getBaseUrl();
        RestAssured.defaultParser = Parser.JSON;
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
    }
}
