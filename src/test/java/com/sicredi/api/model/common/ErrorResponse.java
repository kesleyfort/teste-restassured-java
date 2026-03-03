package com.sicredi.api.model.common;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * POJO para respostas de erro da API.
 *
 * @see <a href="https://dummyjson.com/docs">DummyJSON API</a>
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ErrorResponse {

    private String message;

    /** @return a mensagem de erro retornada pela API */
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
