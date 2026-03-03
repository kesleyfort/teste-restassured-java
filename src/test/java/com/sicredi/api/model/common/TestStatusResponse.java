package com.sicredi.api.model.common;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * POJO para a resposta do endpoint de health check (GET /test).
 *
 * @see <a href="https://dummyjson.com/docs">DummyJSON API</a>
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class TestStatusResponse {

    private String status;
    private String method;

    /** @return o status da API (esperado: "ok") */
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    /** @return o metodo HTTP utilizado (esperado: "GET") */
    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }
}
