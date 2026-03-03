package com.sicredi.api.model.product;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * POJO representando as dimensoes de um produto.
 *
 * @see <a href="https://dummyjson.com/docs/products">Products API</a>
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Dimensions {

    private Double width;
    private Double height;
    private Double depth;

    /** @return a largura do produto */
    public Double getWidth() {
        return width;
    }

    public void setWidth(Double width) {
        this.width = width;
    }

    /** @return a altura do produto */
    public Double getHeight() {
        return height;
    }

    public void setHeight(Double height) {
        this.height = height;
    }

    /** @return a profundidade do produto */
    public Double getDepth() {
        return depth;
    }

    public void setDepth(Double depth) {
        this.depth = depth;
    }
}
