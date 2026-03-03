package com.sicredi.api.tests;

import com.sicredi.api.base.BaseTest;
import com.sicredi.api.model.product.Product;
import com.sicredi.api.model.product.ProductListResponse;
import com.sicredi.api.service.ProductService;
import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Testes para o endpoint GET /products.
 *
 * @see <a href="https://dummyjson.com/docs/products">Products API</a>
 */
@Feature("Products List")
@DisplayName("GET /products - Listagem de Produtos")
class ProductListTest extends BaseTest {

    private ProductService productService;

    @BeforeEach
    void setUp() {
        productService = new ProductService();
    }

    /**
     * Verifica que o endpoint /products retorna status 200 com paginacao padrao
     * contendo total=194, limit=30 e skip=0.
     *
     * @see <a href="https://dummyjson.com/docs/products">Products API</a>
     */
    @Test
    @DisplayName("Deve retornar produtos com paginacao padrao")
    @Description("Verifica status 200 e campos de paginacao padrao (total=194, limit=30, skip=0)")
    void shouldReturnProductsWithDefaultPagination() {
        ProductListResponse body = productService.getProducts()
                .then()
                .statusCode(200)
                .extract()
                .as(ProductListResponse.class);

        assertThat(body.getTotal()).isEqualTo(194);
        assertThat(body.getLimit()).isEqualTo(30);
        assertThat(body.getSkip()).isZero();
    }

    /**
     * Verifica que a lista de produtos retornada nao e vazia
     * e o tamanho respeita o limite padrao.
     *
     * @see <a href="https://dummyjson.com/docs/products">Products API</a>
     */
    @Test
    @DisplayName("Deve retornar lista nao vazia respeitando o limite")
    @Description("Verifica que products nao e vazio e seu tamanho <= limit")
    void shouldReturnNonEmptyListWithinLimit() {
        ProductListResponse body = productService.getProducts()
                .then()
                .statusCode(200)
                .extract()
                .as(ProductListResponse.class);

        assertThat(body.getProducts()).isNotEmpty();
        assertThat(body.getProducts().size()).isLessThanOrEqualTo(body.getLimit());
    }

    /**
     * Verifica que cada produto retornado possui os campos essenciais
     * preenchidos: id, title, price, category.
     *
     * @see <a href="https://dummyjson.com/docs/products">Products API</a>
     */
    @Test
    @DisplayName("Deve retornar produtos com campos essenciais preenchidos")
    @Description("Verifica que cada produto possui id, title, price e category")
    void shouldReturnProductsWithExpectedFields() {
        ProductListResponse body = productService.getProducts()
                .then()
                .statusCode(200)
                .extract()
                .as(ProductListResponse.class);

        assertThat(body.getProducts()).allSatisfy(product -> {
            assertThat(product.getId()).isPositive();
            assertThat(product.getTitle()).isNotBlank();
            assertThat(product.getPrice()).isNotNull();
            assertThat(product.getCategory()).isNotBlank();
        });
    }

    /**
     * Verifica que a paginacao customizada funciona corretamente
     * com diferentes combinacoes de limit e skip.
     *
     * @param limit quantidade maxima de produtos
     * @param skip  quantidade de registros a pular
     * @see <a href="https://dummyjson.com/docs/products">Products API</a>
     */
    @ParameterizedTest(name = "limit={0}, skip={1}")
    @CsvSource({"5, 0", "10, 10", "1, 50", "3, 100"})
    @DisplayName("Deve retornar produtos com paginacao customizada")
    @Description("Verifica que limit e skip funcionam corretamente na paginacao de produtos")
    void shouldReturnProductsWithCustomPagination(int limit, int skip) {
        ProductListResponse body = productService.getProducts(limit, skip)
                .then()
                .statusCode(200)
                .extract()
                .as(ProductListResponse.class);

        assertThat(body.getLimit()).isEqualTo(limit);
        assertThat(body.getSkip()).isEqualTo(skip);
        assertThat(body.getProducts().size()).isLessThanOrEqualTo(limit);
    }

    /**
     * Verifica que todos os precos dos produtos sao positivos.
     *
     * @see <a href="https://dummyjson.com/docs/products">Products API</a>
     */
    @Test
    @DisplayName("Deve retornar produtos com preco positivo")
    @Description("Verifica que todos os produtos possuem preco > 0")
    void shouldReturnProductsWithPositivePrice() {
        ProductListResponse body = productService.getProducts()
                .then()
                .statusCode(200)
                .extract()
                .as(ProductListResponse.class);

        assertThat(body.getProducts())
                .allSatisfy(product ->
                        assertThat(product.getPrice()).isPositive());
    }

    /**
     * Verifica que as avaliacoes dos produtos estao dentro do intervalo valido (0-5).
     *
     * @see <a href="https://dummyjson.com/docs/products">Products API</a>
     */
    @Test
    @DisplayName("Deve retornar produtos com rating entre 0 e 5")
    @Description("Verifica que todos os ratings estao no intervalo [0, 5]")
    void shouldReturnProductsWithValidRating() {
        ProductListResponse body = productService.getProducts()
                .then()
                .statusCode(200)
                .extract()
                .as(ProductListResponse.class);

        assertThat(body.getProducts())
                .allSatisfy(product -> {
                    if (product.getRating() != null) {
                        assertThat(product.getRating()).isBetween(0.0, 5.0);
                    }
                });
    }

    /**
     * Verifica que lista vazia e retornada quando skip excede o total de produtos.
     *
     * @see <a href="https://dummyjson.com/docs/products">Products API</a>
     */
    @Test
    @DisplayName("Deve retornar lista vazia quando skip excede o total")
    @Description("Verifica que a API retorna lista vazia quando skip > total de produtos")
    void shouldReturnEmptyListWhenSkipExceedsTotal() {
        ProductListResponse body = productService.getProducts(10, 99999)
                .then()
                .statusCode(200)
                .extract()
                .as(ProductListResponse.class);

        assertThat(body.getProducts()).isEmpty();
    }

    /**
     * Valida que a resposta do endpoint /products esta em conformidade
     * com o JSON Schema definido em product-list-response-schema.json.
     *
     * @see <a href="https://dummyjson.com/docs/products">Products API</a>
     */
    @Test
    @DisplayName("Deve estar em conformidade com o JSON Schema")
    @Description("Valida a resposta contra o JSON Schema definido para a listagem de produtos")
    void shouldMatchJsonSchema() {
        productService.getProducts()
                .then()
                .statusCode(200)
                .body(matchesJsonSchemaInClasspath("schemas/product-list-response-schema.json"));
    }
}
