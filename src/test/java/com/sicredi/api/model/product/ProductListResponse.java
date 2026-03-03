package com.sicredi.api.model.product;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;

/**
 * POJO para a resposta de listagem de produtos com paginacao.
 *
 * @see <a href="https://dummyjson.com/docs/products">Products API</a>
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProductListResponse {

    private List<Product> products;
    private Integer total;
    private Integer skip;
    private Integer limit;

    /** @return a lista de produtos */
    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    /** @return o total de produtos disponiveis */
    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    /** @return a quantidade de registros pulados (offset) */
    public Integer getSkip() {
        return skip;
    }

    public void setSkip(Integer skip) {
        this.skip = skip;
    }

    /** @return o limite de registros por pagina */
    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }
}
