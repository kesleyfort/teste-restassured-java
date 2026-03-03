package com.sicredi.api.tests;

import com.sicredi.api.base.BaseTest;
import com.sicredi.api.model.common.ErrorResponse;
import com.sicredi.api.model.product.Product;
import com.sicredi.api.service.ProductService;
import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Testes para o endpoint GET /products/{id}.
 *
 * @see <a href="https://dummyjson.com/docs/products">Products API</a>
 */
@Feature("Product By ID")
@DisplayName("GET /products/{id} - Produto por ID")
class ProductByIdTest extends BaseTest {

    private ProductService productService;

    @BeforeEach
    void setUp() {
        productService = new ProductService();
    }

    /**
     * Verifica que o endpoint /products/1 retorna status 200
     * com os dados corretos do primeiro produto.
     *
     * @see <a href="https://dummyjson.com/docs/products">Products API</a>
     */
    @Test
    @DisplayName("Deve retornar produto por ID com status 200")
    @Description("GET /products/1 retorna 200 com dados do produto")
    void shouldReturnProductByIdSuccessfully() {
        Product product = productService.getProductById(1)
                .then()
                .statusCode(200)
                .extract()
                .as(Product.class);

        assertThat(product.getId()).isEqualTo(1);
        assertThat(product.getTitle()).isNotBlank();
        assertThat(product.getPrice()).isPositive();
    }

    /**
     * Verifica dados especificos do produto 1 conforme a API DummyJSON.
     *
     * @see <a href="https://dummyjson.com/docs/products">Products API</a>
     */
    @Test
    @DisplayName("Deve retornar dados especificos do produto 1")
    @Description("Verifica que o produto 1 possui titulo, categoria e campos preenchidos")
    void shouldReturnCorrectDataForProduct1() {
        Product product = productService.getProductById(1)
                .then()
                .statusCode(200)
                .extract()
                .as(Product.class);

        assertThat(product.getId()).isEqualTo(1);
        assertThat(product.getTitle()).isNotBlank();
        assertThat(product.getDescription()).isNotBlank();
        assertThat(product.getCategory()).isNotBlank();
        assertThat(product.getPrice()).isPositive();
        assertThat(product.getBrand()).isNotNull();
    }

    /**
     * Verifica que o produto retornado contem o campo reviews (avaliacoes)
     * como uma lista nao nula.
     *
     * @see <a href="https://dummyjson.com/docs/products">Products API</a>
     */
    @Test
    @DisplayName("Deve retornar produto com reviews")
    @Description("Verifica que o produto possui campo reviews como lista nao nula")
    void shouldReturnProductWithReviews() {
        Product product = productService.getProductById(1)
                .then()
                .statusCode(200)
                .extract()
                .as(Product.class);

        assertThat(product.getReviews()).isNotNull();
        assertThat(product.getReviews()).allSatisfy(review -> {
            assertThat(review.getRating()).isBetween(1, 5);
            assertThat(review.getComment()).isNotBlank();
            assertThat(review.getReviewerName()).isNotBlank();
            assertThat(review.getReviewerEmail()).isNotBlank();
        });
    }

    /**
     * Verifica que o produto retornado contem o campo dimensions
     * com width, height e depth.
     *
     * @see <a href="https://dummyjson.com/docs/products">Products API</a>
     */
    @Test
    @DisplayName("Deve retornar produto com dimensions")
    @Description("Verifica que o produto possui campo dimensions com width, height e depth")
    void shouldReturnProductWithDimensions() {
        Product product = productService.getProductById(1)
                .then()
                .statusCode(200)
                .extract()
                .as(Product.class);

        assertThat(product.getDimensions()).isNotNull();
        assertThat(product.getDimensions().getWidth()).isPositive();
        assertThat(product.getDimensions().getHeight()).isPositive();
        assertThat(product.getDimensions().getDepth()).isPositive();
    }

    /**
     * Verifica que o produto retornado contem metadados (meta)
     * com createdAt, updatedAt e barcode.
     *
     * @see <a href="https://dummyjson.com/docs/products">Products API</a>
     */
    @Test
    @DisplayName("Deve retornar produto com metadados")
    @Description("Verifica que o produto possui campo meta com createdAt, updatedAt e barcode")
    void shouldReturnProductWithMeta() {
        Product product = productService.getProductById(1)
                .then()
                .statusCode(200)
                .extract()
                .as(Product.class);

        assertThat(product.getMeta()).isNotNull();
        assertThat(product.getMeta().getCreatedAt()).isNotBlank();
        assertThat(product.getMeta().getUpdatedAt()).isNotBlank();
        assertThat(product.getMeta().getBarcode()).isNotBlank();
    }

    /**
     * Verifica que IDs inexistentes retornam 404 com mensagem de erro adequada.
     * Testa parametrizado com IDs: 0, -1, 9999.
     *
     * @param id ID inexistente do produto
     * @see <a href="https://dummyjson.com/docs/products">Products API</a>
     */
    @ParameterizedTest(name = "ID = {0}")
    @ValueSource(ints = {0, -1, 9999})
    @DisplayName("Deve retornar 404 para ID inexistente")
    @Description("GET /products/{id} com ID inexistente retorna 404 com mensagem de erro")
    void shouldReturn404ForNonExistentId(int id) {
        ErrorResponse error = productService.getProductById(id)
                .then()
                .statusCode(404)
                .extract()
                .as(ErrorResponse.class);

        assertThat(error.getMessage()).isNotBlank();
    }

    /**
     * Verifica o boundary entre o ultimo ID valido (194)
     * e o primeiro ID invalido (195).
     *
     * @see <a href="https://dummyjson.com/docs/products">Products API</a>
     */
    @Test
    @DisplayName("Deve retornar 200 para ultimo ID valido (194)")
    @Description("Verifica boundary: produto 194 existe e retorna 200")
    void shouldReturn200ForLastValidId() {
        Product product = productService.getProductById(194)
                .then()
                .statusCode(200)
                .extract()
                .as(Product.class);

        assertThat(product.getId()).isEqualTo(194);
    }

    /**
     * Verifica que o primeiro ID invalido (195) retorna 404.
     *
     * @see <a href="https://dummyjson.com/docs/products">Products API</a>
     */
    @Test
    @DisplayName("Deve retornar 404 para primeiro ID invalido (195)")
    @Description("Verifica boundary: produto 195 nao existe e retorna 404")
    void shouldReturn404ForFirstInvalidId() {
        productService.getProductById(195)
                .then()
                .statusCode(404);
    }

    /**
     * Valida que a resposta do endpoint /products/{id} esta em conformidade
     * com o JSON Schema definido em product-response-schema.json.
     *
     * @see <a href="https://dummyjson.com/docs/products">Products API</a>
     */
    @Test
    @DisplayName("Deve estar em conformidade com o JSON Schema")
    @Description("Valida a resposta contra o JSON Schema definido para produto individual")
    void shouldMatchJsonSchema() {
        productService.getProductById(1)
                .then()
                .statusCode(200)
                .body(matchesJsonSchemaInClasspath("schemas/product-response-schema.json"));
    }
}
