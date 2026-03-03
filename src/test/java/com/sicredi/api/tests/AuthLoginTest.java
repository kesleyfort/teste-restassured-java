package com.sicredi.api.tests;

import com.sicredi.api.base.BaseTest;
import com.sicredi.api.data.TestDataFactory;
import com.sicredi.api.model.auth.LoginRequest;
import com.sicredi.api.model.auth.LoginResponse;
import com.sicredi.api.model.common.ErrorResponse;
import com.sicredi.api.service.AuthService;
import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Testes para o endpoint POST /auth/login.
 *
 * @see <a href="https://dummyjson.com/docs/auth">Auth API</a>
 */
@Feature("Authentication")
@DisplayName("POST /auth/login - Autenticacao")
class AuthLoginTest extends BaseTest {

    private AuthService authService;

    @BeforeEach
    void setUp() {
        authService = new AuthService();
    }

    /**
     * Verifica que o login com credenciais validas retorna status 200
     * e o corpo da resposta contem os dados do usuario autenticado
     * incluindo accessToken e refreshToken.
     *
     * @see <a href="https://dummyjson.com/docs/auth">Auth API</a>
     */
    @Test
    @DisplayName("Deve fazer login com sucesso usando credenciais validas")
    @Description("Login com emilys/emilyspass retorna 200 com dados do usuario e tokens")
    void shouldLoginSuccessfullyWithValidCredentials() {
        LoginRequest request = TestDataFactory.validLoginRequest();

        LoginResponse response = authService.login(request)
                .then()
                .statusCode(200)
                .extract()
                .as(LoginResponse.class);

        assertThat(response.getId()).isPositive();
        assertThat(response.getUsername()).isEqualTo("emilys");
        assertThat(response.getEmail()).isNotBlank().contains("@");
        assertThat(response.getFirstName()).isEqualTo("Emily");
        assertThat(response.getLastName()).isEqualTo("Johnson");
    }

    /**
     * Verifica que o token JWT retornado pelo login e valido
     * (formato eyJ que indica um JWT Base64).
     *
     * @see <a href="https://dummyjson.com/docs/auth">Auth API</a>
     */
    @Test
    @DisplayName("Deve retornar access token JWT valido")
    @Description("Verifica que accessToken comeca com 'eyJ' (formato JWT)")
    void shouldReturnValidJwtAccessToken() {
        LoginRequest request = TestDataFactory.validLoginRequest();

        LoginResponse response = authService.login(request)
                .then()
                .statusCode(200)
                .extract()
                .as(LoginResponse.class);

        assertThat(response.getAccessToken())
                .isNotEmpty()
                .startsWith("eyJ");
    }

    /**
     * Verifica que o refresh token e retornado junto com o access token.
     *
     * @see <a href="https://dummyjson.com/docs/auth">Auth API</a>
     */
    @Test
    @DisplayName("Deve retornar refresh token")
    @Description("Verifica que refreshToken esta presente na resposta")
    void shouldReturnRefreshToken() {
        LoginRequest request = TestDataFactory.validLoginRequest();

        LoginResponse response = authService.login(request)
                .then()
                .statusCode(200)
                .extract()
                .as(LoginResponse.class);

        assertThat(response.getRefreshToken())
                .isNotEmpty()
                .startsWith("eyJ");
    }

    /**
     * Verifica que os dados do usuario retornados no login estao corretos
     * conforme os dados do usuario emilys na API.
     *
     * @see <a href="https://dummyjson.com/docs/auth">Auth API</a>
     */
    @Test
    @DisplayName("Deve retornar dados corretos do usuario no login")
    @Description("Valida que gender e image estao presentes na resposta de login")
    void shouldReturnCorrectUserDataOnLogin() {
        LoginRequest request = TestDataFactory.validLoginRequest();

        LoginResponse response = authService.login(request)
                .then()
                .statusCode(200)
                .extract()
                .as(LoginResponse.class);

        assertThat(response.getGender()).isNotBlank();
        assertThat(response.getImage()).isNotBlank();
    }

    /**
     * Verifica que o login com credenciais invalidas retorna status 400
     * com mensagem de erro adequada.
     *
     * @see <a href="https://dummyjson.com/docs/auth">Auth API</a>
     */
    @Test
    @DisplayName("Deve retornar 400 para credenciais invalidas")
    @Description("Login com credenciais invalidas retorna 400 com mensagem de erro")
    void shouldReturn400ForInvalidCredentials() {
        LoginRequest request = TestDataFactory.invalidLoginRequest();

        ErrorResponse error = authService.login(request)
                .then()
                .statusCode(400)
                .extract()
                .as(ErrorResponse.class);

        assertThat(error.getMessage()).isNotBlank();
    }

    /**
     * Verifica que a requisicao sem username retorna erro 400.
     *
     * @see <a href="https://dummyjson.com/docs/auth">Auth API</a>
     */
    @Test
    @DisplayName("Deve retornar 400 quando username esta ausente")
    @Description("Login sem username no body retorna 400")
    void shouldReturn400WhenUsernameMissing() {
        LoginRequest request = TestDataFactory.loginRequestWithoutUsername();

        authService.login(request)
                .then()
                .statusCode(400);
    }

    /**
     * Verifica que a requisicao sem password retorna erro 400.
     *
     * @see <a href="https://dummyjson.com/docs/auth">Auth API</a>
     */
    @Test
    @DisplayName("Deve retornar 400 quando password esta ausente")
    @Description("Login sem password no body retorna 400")
    void shouldReturn400WhenPasswordMissing() {
        LoginRequest request = TestDataFactory.loginRequestWithoutPassword();

        authService.login(request)
                .then()
                .statusCode(400);
    }

    /**
     * Verifica que a requisicao com campos vazios retorna erro 400.
     *
     * @see <a href="https://dummyjson.com/docs/auth">Auth API</a>
     */
    @Test
    @DisplayName("Deve retornar 400 para campos vazios")
    @Description("Login com username e password vazios retorna 400")
    void shouldReturn400ForEmptyCredentials() {
        LoginRequest request = TestDataFactory.emptyCredentialsLoginRequest();

        authService.login(request)
                .then()
                .statusCode(400);
    }

    /**
     * Verifica que e possivel configurar o tempo de expiracao do token
     * usando o campo expiresInMins na requisicao de login.
     *
     * @see <a href="https://dummyjson.com/docs/auth">Auth API</a>
     */
    @Test
    @DisplayName("Deve aceitar expiresInMins customizado")
    @Description("Login com expiresInMins=1 retorna 200 com token valido")
    void shouldAcceptCustomExpiresInMins() {
        LoginRequest request = TestDataFactory.loginRequestWithCustomExpiry(1);

        LoginResponse response = authService.login(request)
                .then()
                .statusCode(200)
                .extract()
                .as(LoginResponse.class);

        assertThat(response.getAccessToken()).isNotEmpty();
    }

    /**
     * Valida que a resposta de login esta em conformidade
     * com o JSON Schema definido em login-response-schema.json.
     *
     * @see <a href="https://dummyjson.com/docs/auth">Auth API</a>
     */
    @Test
    @DisplayName("Deve estar em conformidade com o JSON Schema de login")
    @Description("Valida a resposta de login contra o JSON Schema definido")
    void shouldMatchLoginJsonSchema() {
        LoginRequest request = TestDataFactory.validLoginRequest();

        authService.login(request)
                .then()
                .statusCode(200)
                .body(matchesJsonSchemaInClasspath("schemas/login-response-schema.json"));
    }

    /**
     * Valida que a resposta de erro esta em conformidade
     * com o JSON Schema definido em error-response-schema.json.
     *
     * @see <a href="https://dummyjson.com/docs/auth">Auth API</a>
     */
    @Test
    @DisplayName("Deve estar em conformidade com o JSON Schema de erro")
    @Description("Valida a resposta de erro contra o JSON Schema definido")
    void shouldMatchErrorJsonSchema() {
        LoginRequest request = TestDataFactory.invalidLoginRequest();

        authService.login(request)
                .then()
                .statusCode(400)
                .body(matchesJsonSchemaInClasspath("schemas/error-response-schema.json"));
    }
}
