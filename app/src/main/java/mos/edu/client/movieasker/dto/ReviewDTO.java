package mos.edu.client.movieasker.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import org.springframework.hateoas.ResourceSupport;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ReviewDTO extends ResourceSupport {

    private String header;
    private String review;

    protected ReviewDTO() {}

    public ReviewDTO(String header, String review) {
        this.header = header;
        this.review = review;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

}
