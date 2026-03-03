package com.sicredi.api.model.product;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * POJO representando uma avaliacao de produto.
 *
 * @see <a href="https://dummyjson.com/docs/products">Products API</a>
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Review {

    private Integer rating;
    private String comment;
    private String date;
    private String reviewerName;
    private String reviewerEmail;

    /** @return a nota da avaliacao */
    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    /** @return o comentario da avaliacao */
    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    /** @return a data da avaliacao */
    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    /** @return o nome do avaliador */
    public String getReviewerName() {
        return reviewerName;
    }

    public void setReviewerName(String reviewerName) {
        this.reviewerName = reviewerName;
    }

    /** @return o email do avaliador */
    public String getReviewerEmail() {
        return reviewerEmail;
    }

    public void setReviewerEmail(String reviewerEmail) {
        this.reviewerEmail = reviewerEmail;
    }
}
