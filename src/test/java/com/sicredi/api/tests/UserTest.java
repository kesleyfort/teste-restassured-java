package com.sicredi.api.tests;

import com.sicredi.api.base.BaseTest;
import com.sicredi.api.model.user.User;
import com.sicredi.api.model.user.UserListResponse;
import com.sicredi.api.service.UserService;
import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.Optional;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Testes para o endpoint GET /users.
 *
 * @see <a href="https://dummyjson.com/docs/users">Users API</a>
 */
@Feature("Users")
@DisplayName("GET /users - Listagem de Usuarios")
class UserTest extends BaseTest {

    private UserService userService;

    @BeforeEach
    void setUp() {
        userService = new UserService();
    }

    /**
     * Verifica que o endpoint /users retorna status 200 com paginacao padrao
     * contendo total, limit e skip na resposta.
     *
     * @see <a href="https://dummyjson.com/docs/users">Users API</a>
     */
    @Test
    @DisplayName("Deve retornar lista de usuarios com paginacao padrao")
    @Description("Verifica status 200 e campos de paginacao na resposta")
    void shouldReturnUsersWithDefaultPagination() {
        Response response = userService.getUsers();

        UserListResponse body = response.then()
                .statusCode(200)
                .extract()
                .as(UserListResponse.class);

        assertThat(body.getTotal()).isPositive();
        assertThat(body.getLimit()).isEqualTo(30);
        assertThat(body.getSkip()).isZero();
        assertThat(body.getUsers()).isNotEmpty();
    }

    /**
     * Verifica que os usuarios retornados possuem os campos obrigatorios
     * preenchidos: id, firstName, lastName, email, username.
     *
     * @see <a href="https://dummyjson.com/docs/users">Users API</a>
     */
    @Test
    @DisplayName("Deve retornar usuarios com campos obrigatorios preenchidos")
    @Description("Verifica que cada usuario possui id, firstName, lastName, email e username")
    void shouldReturnUsersWithRequiredFields() {
        UserListResponse body = userService.getUsers()
                .then()
                .statusCode(200)
                .extract()
                .as(UserListResponse.class);

        assertThat(body.getUsers()).allSatisfy(user -> {
            assertThat(user.getId()).isPositive();
            assertThat(user.getFirstName()).isNotBlank();
            assertThat(user.getLastName()).isNotBlank();
            assertThat(user.getEmail()).isNotBlank().contains("@");
            assertThat(user.getUsername()).isNotBlank();
        });
    }

    /**
     * Verifica que o usuario emilys existe na lista e possui as credenciais
     * corretas conforme documentacao da API (regra de negocio).
     *
     * @see <a href="https://dummyjson.com/docs/users">Users API</a>
     */
    @Test
    @DisplayName("Deve conter o usuario emilys com credenciais corretas")
    @Description("Valida regra de negocio: usuario emilys tem username e password conforme docs")
    void shouldContainEmilysUserWithCorrectCredentials() {
        UserListResponse body = userService.getUsers()
                .then()
                .statusCode(200)
                .extract()
                .as(UserListResponse.class);

        Optional<User> emilys = body.getUsers().stream()
                .filter(u -> "emilys".equals(u.getUsername()))
                .findFirst();

        assertThat(emilys).isPresent();
        assertThat(emilys.get().getPassword()).isEqualTo("emilyspass");
    }

    /**
     * Verifica que a paginacao funciona corretamente com parametros
     * limit e skip customizados.
     *
     * @param limit quantidade maxima de usuarios
     * @param skip  quantidade de registros a pular
     * @see <a href="https://dummyjson.com/docs/users">Users API</a>
     */
    @ParameterizedTest(name = "limit={0}, skip={1}")
    @CsvSource({"5, 0", "10, 5", "1, 10", "3, 20"})
    @DisplayName("Deve retornar usuarios com paginacao customizada")
    @Description("Verifica que limit e skip funcionam corretamente na paginacao")
    void shouldReturnUsersWithCustomPagination(int limit, int skip) {
        UserListResponse body = userService.getUsers(limit, skip)
                .then()
                .statusCode(200)
                .extract()
                .as(UserListResponse.class);

        assertThat(body.getLimit()).isEqualTo(limit);
        assertThat(body.getSkip()).isEqualTo(skip);
        assertThat(body.getUsers().size()).isLessThanOrEqualTo(limit);
    }

    /**
     * Verifica o comportamento quando skip e maior que o total de usuarios.
     * A API deve retornar uma lista vazia.
     *
     * @see <a href="https://dummyjson.com/docs/users">Users API</a>
     */
    @Test
    @DisplayName("Deve retornar lista vazia quando skip maior que total")
    @Description("Verifica que a API retorna lista vazia quando skip excede o total de registros")
    void shouldReturnEmptyListWhenSkipExceedsTotal() {
        UserListResponse body = userService.getUsers(10, 99999)
                .then()
                .statusCode(200)
                .extract()
                .as(UserListResponse.class);

        assertThat(body.getUsers()).isEmpty();
    }

    /**
     * Verifica o comportamento quando limit e 0.
     * A API DummyJSON ignora limit=0 e retorna a lista com paginacao padrao.
     * Isso pode ser considerado um bug (deveria retornar lista vazia).
     *
     * @see <a href="https://dummyjson.com/docs/users">Users API</a>
     */
    @Test
    @DisplayName("[BUG] Deve ignorar limit=0 e retornar lista padrao")
    @Description("BUG: API ignora limit=0 e retorna lista com paginacao padrao ao inves de lista vazia")
    void shouldReturnDefaultListWhenLimitIsZero() {
        UserListResponse body = userService.getUsers(0, 0)
                .then()
                .statusCode(200)
                .extract()
                .as(UserListResponse.class);

        assertThat(body.getUsers()).isNotEmpty();
    }

    /**
     * Verifica que a quantidade de usuarios retornados nunca excede o limit.
     *
     * @see <a href="https://dummyjson.com/docs/users">Users API</a>
     */
    @Test
    @DisplayName("Deve respeitar o limite de usuarios retornados")
    @Description("Verifica que o tamanho da lista nunca excede o limit solicitado")
    void shouldRespectLimitParameter() {
        UserListResponse body = userService.getUsers(5, 0)
                .then()
                .statusCode(200)
                .extract()
                .as(UserListResponse.class);

        assertThat(body.getUsers()).hasSizeLessThanOrEqualTo(5);
    }

    /**
     * Valida que a resposta do endpoint /users esta em conformidade
     * com o JSON Schema definido em user-list-response-schema.json.
     *
     * @see <a href="https://dummyjson.com/docs/users">Users API</a>
     */
    @Test
    @DisplayName("Deve estar em conformidade com o JSON Schema")
    @Description("Valida a resposta contra o JSON Schema definido para a listagem de usuarios")
    void shouldMatchJsonSchema() {
        userService.getUsers()
                .then()
                .statusCode(200)
                .body(matchesJsonSchemaInClasspath("schemas/user-list-response-schema.json"));
    }
}
