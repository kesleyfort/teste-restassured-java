package com.sicredi.api.tests;

import com.sicredi.api.base.BaseTest;
import com.sicredi.api.data.TestDataFactory;
import com.sicredi.api.model.product.CreateProductRequest;
import com.sicredi.api.model.product.Product;
import com.sicredi.api.service.ProductService;
import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Testes para o endpoint POST /products/add.
 *
 * @see <a href="https://dummyjson.com/docs/products#products-add">Products Add API</a>
 */
@Feature("Product Create")
@DisplayName("POST /products/add - Criacao de Produtos")
class ProductCreateTest extends BaseTest {

    private ProductService productService;

    @BeforeEach
    void setUp() {
        productService = new ProductService();
    }

    /**
     * Verifica que a criacao de produto com todos os campos retorna 201
     * com o produto criado contendo um ID gerado.
     *
     * @see <a href="https://dummyjson.com/docs/products#products-add">Products Add API</a>
     */
    @Test
    @DisplayName("Deve criar produto com todos os campos e retornar 201")
    @Description("POST /products/add com body completo retorna 201 com produto criado")
    void shouldCreateProductWithAllFieldsSuccessfully() {
        CreateProductRequest request = TestDataFactory.validFullProduct();

        Product response = productService.createProduct(request)
                .then()
                .statusCode(201)
                .extract()
                .as(Product.class);

        assertThat(response.getId()).isPositive();
        assertThat(response.getTitle()).isEqualTo(request.getTitle());
        assertThat(response.getPrice()).isEqualTo(request.getPrice());
        assertThat(response.getCategory()).isEqualTo(request.getCategory());
    }

    /**
     * Verifica que a criacao de produto com campos minimos (apenas titulo)
     * retorna 201 com sucesso.
     *
     * @see <a href="https://dummyjson.com/docs/products#products-add">Products Add API</a>
     */
    @Test
    @DisplayName("Deve criar produto com campos minimos (apenas titulo)")
    @Description("POST /products/add com apenas titulo retorna 201")
    void shouldCreateProductWithMinimalFields() {
        CreateProductRequest request = TestDataFactory.minimalProduct();

        Product response = productService.createProduct(request)
                .then()
                .statusCode(201)
                .extract()
                .as(Product.class);

        assertThat(response.getId()).isPositive();
        assertThat(response.getTitle()).isEqualTo("Minimal Product");
    }

    /**
     * BUG: Verifica que o body vazio e aceito pela API, retornando 201
     * com apenas o ID. Isso indica falta de validacao no backend.
     *
     * @see <a href="https://dummyjson.com/docs/products#products-add">Products Add API</a>
     */
    @Test
    @DisplayName("[BUG] Deve aceitar body vazio sem validacao")
    @Description("BUG: POST /products/add com body vazio retorna 201 com apenas ID (sem validacao)")
    void shouldAcceptEmptyBodyWithoutValidation() {
        Product response = productService.createProductWithEmptyBody()
                .then()
                .statusCode(201)
                .extract()
                .as(Product.class);

        assertThat(response.getId()).isPositive();
    }

    /**
     * Verifica que os campos enviados no request body batem com os campos
     * retornados na resposta da criacao.
     *
     * @see <a href="https://dummyjson.com/docs/products#products-add">Products Add API</a>
     */
    @Test
    @DisplayName("Deve retornar campos iguais aos enviados no request")
    @Description("Verifica que o produto criado reflete os dados enviados no request body")
    void shouldReturnFieldsMatchingRequest() {
        CreateProductRequest request = TestDataFactory.validFullProduct();

        Product response = productService.createProduct(request)
                .then()
                .statusCode(201)
                .extract()
                .as(Product.class);

        assertThat(response.getTitle()).isEqualTo(request.getTitle());
        assertThat(response.getDescription()).isEqualTo(request.getDescription());
        assertThat(response.getPrice()).isEqualTo(request.getPrice());
        assertThat(response.getDiscountPercentage()).isEqualTo(request.getDiscountPercentage());
        assertThat(response.getRating()).isEqualTo(request.getRating());
        assertThat(response.getStock()).isEqualTo(request.getStock());
        assertThat(response.getBrand()).isEqualTo(request.getBrand());
        assertThat(response.getCategory()).isEqualTo(request.getCategory());
    }

    /**
     * BUG: Verifica que o produto criado NAO persiste na API.
     * O ID retornado (195) nao e encontrado em GET /products/{id} (404).
     * Isso e comportamento esperado do DummyJSON (mock server).
     *
     * @see <a href="https://dummyjson.com/docs/products#products-add">Products Add API</a>
     */
    @Test
    @DisplayName("Produto criado nao persiste (mock behavior)")
    @Description("Verifica que o produto criado via POST nao e persistido - GET retorna 404")
    void shouldNotPersistCreatedProduct() {
        CreateProductRequest request = TestDataFactory.validFullProduct();

        Product created = productService.createProduct(request)
                .then()
                .statusCode(201)
                .extract()
                .as(Product.class);

        productService.getProductById(created.getId())
                .then()
                .statusCode(404);
    }

    /**
     * BUG: Verifica que o ID do produto criado e 195 (total + 1),
     * diferente do que a documentacao sugere (101).
     *
     * @see <a href="https://dummyjson.com/docs/products#products-add">Products Add API</a>
     */
    @Test
    @DisplayName("[BUG] ID do produto criado e 195 ao inves de 101")
    @Description("BUG: O ID retornado e 195 (total+1) ao inves de 101 conforme spec")
    void shouldReturnIdAs195InsteadOf101() {
        CreateProductRequest request = TestDataFactory.validFullProduct();

        Product response = productService.createProduct(request)
                .then()
                .statusCode(201)
                .extract()
                .as(Product.class);

        assertThat(response.getId()).isEqualTo(195);
    }

    /**
     * Verifica que o produto criado com todos os campos possui
     * description e brand preenchidos corretamente.
     *
     * @see <a href="https://dummyjson.com/docs/products#products-add">Products Add API</a>
     */
    @Test
    @DisplayName("Deve retornar descricao e marca do produto criado")
    @Description("Verifica que description e brand sao retornados corretamente na criacao")
    void shouldReturnDescriptionAndBrandOnCreate() {
        CreateProductRequest request = TestDataFactory.validFullProduct();

        Product response = productService.createProduct(request)
                .then()
                .statusCode(201)
                .extract()
                .as(Product.class);

        assertThat(response.getDescription()).isEqualTo(request.getDescription());
        assertThat(response.getBrand()).isEqualTo(request.getBrand());
    }

    /**
     * Verifica que o stock e rating enviados sao retornados corretamente
     * na resposta de criacao.
     *
     * @see <a href="https://dummyjson.com/docs/products#products-add">Products Add API</a>
     */
    @Test
    @DisplayName("Deve retornar stock e rating do produto criado")
    @Description("Verifica que stock e rating sao retornados corretamente na criacao")
    void shouldReturnStockAndRatingOnCreate() {
        CreateProductRequest request = TestDataFactory.validFullProduct();

        Product response = productService.createProduct(request)
                .then()
                .statusCode(201)
                .extract()
                .as(Product.class);

        assertThat(response.getStock()).isEqualTo(request.getStock());
        assertThat(response.getRating()).isEqualTo(request.getRating());
    }

    /**
     * Valida que a resposta de criacao de produto esta em conformidade
     * com o JSON Schema definido em create-product-response-schema.json.
     *
     * @see <a href="https://dummyjson.com/docs/products#products-add">Products Add API</a>
     */
    @Test
    @DisplayName("Deve estar em conformidade com o JSON Schema")
    @Description("Valida a resposta contra o JSON Schema de criacao de produto")
    void shouldMatchCreateProductJsonSchema() {
        CreateProductRequest request = TestDataFactory.validFullProduct();

        productService.createProduct(request)
                .then()
                .statusCode(201)
                .body(matchesJsonSchemaInClasspath("schemas/create-product-response-schema.json"));
    }
}
