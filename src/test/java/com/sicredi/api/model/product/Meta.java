package com.sicredi.api.model.product;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * POJO representando os metadados de um produto.
 *
 * @see <a href="https://dummyjson.com/docs/products">Products API</a>
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Meta {

    private String createdAt;
    private String updatedAt;
    private String barcode;
    private String qrCode;

    /** @return a data de criacao do produto */
    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    /** @return a data de atualizacao do produto */
    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    /** @return o codigo de barras do produto */
    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    /** @return a URL do QR Code do produto */
    public String getQrCode() {
        return qrCode;
    }

    public void setQrCode(String qrCode) {
        this.qrCode = qrCode;
    }
}
