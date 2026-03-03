package com.sicredi.api.data;

import com.sicredi.api.config.Configuration;
import com.sicredi.api.model.auth.LoginRequest;
import com.sicredi.api.model.product.CreateProductRequest;

/**
 * Factory para criacao de objetos de teste com dados pre-configurados.
 * Centraliza a criacao de payloads para garantir consistencia nos testes.
 */
public final class TestDataFactory {

    private TestDataFactory() {
    }

    /**
     * Cria uma requisicao de login com credenciais validas do usuario padrao (emilys).
     *
     * @return LoginRequest com username e password validos
     */
    public static LoginRequest validLoginRequest() {
        Configuration config = Configuration.getInstance();
        return new LoginRequest(config.getDefaultUsername(), config.getDefaultPassword());
    }

    /**
     * Cria uma requisicao de login com credenciais invalidas.
     *
     * @return LoginRequest com username e password invalidos
     */
    public static LoginRequest invalidLoginRequest() {
        return new LoginRequest("invaliduser", "wrongpassword");
    }

    /**
     * Cria uma requisicao de login sem username (apenas password).
     *
     * @return LoginRequest com username nulo
     */
    public static LoginRequest loginRequestWithoutUsername() {
        return new LoginRequest(null, "emilyspass");
    }

    /**
     * Cria uma requisicao de login sem password (apenas username).
     *
     * @return LoginRequest com password nulo
     */
    public static LoginRequest loginRequestWithoutPassword() {
        return new LoginRequest("emilys", null);
    }

    /**
     * Cria uma requisicao de login com campos vazios.
     *
     * @return LoginRequest com username e password vazios
     */
    public static LoginRequest emptyCredentialsLoginRequest() {
        return new LoginRequest("", "");
    }

    /**
     * Cria uma requisicao de login com tempo de expiracao customizado.
     *
     * @param expiresInMins tempo de expiracao do token em minutos
     * @return LoginRequest com expiresInMins configurado
     */
    public static LoginRequest loginRequestWithCustomExpiry(int expiresInMins) {
        Configuration config = Configuration.getInstance();
        return new LoginRequest(config.getDefaultUsername(), config.getDefaultPassword(), expiresInMins);
    }

    /**
     * Cria uma requisicao de produto com todos os campos preenchidos.
     *
     * @return CreateProductRequest com dados completos
     */
    public static CreateProductRequest validFullProduct() {
        CreateProductRequest product = new CreateProductRequest();
        product.setTitle("Sicredi Test Product");
        product.setDescription("A high-quality test product for QE challenge");
        product.setCategory("electronics");
        product.setPrice(99.99);
        product.setDiscountPercentage(10.5);
        product.setRating(4.5);
        product.setStock(50);
        product.setBrand("Sicredi Brand");
        return product;
    }

    /**
     * Cria uma requisicao de produto com apenas o titulo (campos minimos).
     *
     * @return CreateProductRequest apenas com titulo
     */
    public static CreateProductRequest minimalProduct() {
        CreateProductRequest product = new CreateProductRequest();
        product.setTitle("Minimal Product");
        return product;
    }

    /**
     * Cria uma requisicao de produto vazia (sem campos).
     *
     * @return CreateProductRequest sem nenhum campo preenchido
     */
    public static CreateProductRequest emptyProduct() {
        return new CreateProductRequest();
    }
}
