package com.sicredi.api.model.product;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * POJO para o corpo da requisicao de criacao de produto.
 *
 * @see <a href="https://dummyjson.com/docs/products#products-add">Products Add API</a>
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CreateProductRequest {

    private String title;
    private String description;
    private String category;
    private Double price;
    private Double discountPercentage;
    private Double rating;
    private Integer stock;
    private String brand;

    /** @return o titulo do produto */
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    /** @return a descricao do produto */
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    /** @return a categoria do produto */
    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    /** @return o preco do produto */
    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    /** @return o percentual de desconto */
    public Double getDiscountPercentage() {
        return discountPercentage;
    }

    public void setDiscountPercentage(Double discountPercentage) {
        this.discountPercentage = discountPercentage;
    }

    /** @return a avaliacao do produto */
    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    /** @return a quantidade em estoque */
    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    /** @return a marca do produto */
    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }
}
