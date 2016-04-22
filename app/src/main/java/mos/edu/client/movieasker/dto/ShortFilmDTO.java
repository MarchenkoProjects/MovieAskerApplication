package mos.edu.client.movieasker.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import org.springframework.hateoas.PagedResources;
import org.springframework.hateoas.ResourceSupport;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ShortFilmDTO extends PagedResources<ShortFilmDTO> {
    
    private int idFilm;
    private String posterUrl;
    private String alternativeName;
    private String originalName;
    private int year;

    private RatingDTO rating;

    protected ShortFilmDTO() {}

    public int getIdFilm() {
        return idFilm;
    }

    public void setIdFilm(int idFilm) {
        this.idFilm = idFilm;
    }

    public String getPosterUrl() {
        return posterUrl;
    }

    public void setPosterUrl(String posterUrl) {
        this.posterUrl = posterUrl;
    }

    public String getAlternativeName() {
        return alternativeName;
    }

    public void setAlternativeName(String alternativeName) {
        this.alternativeName = alternativeName;
    }

    public String getOriginalName() {
        return originalName;
    }

    public void setOriginalName(String originalName) {
        this.originalName = originalName;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public RatingDTO getRating() {
        return rating;
    }

    public void setRating(RatingDTO rating) {
        this.rating = rating;
    }

    public static class RatingDTO extends ResourceSupport {

        private double rating;
        private int votesCount;

        protected RatingDTO() {}

        public double getRating() {
            return rating;
        }

        public void setRating(double rating) {
            this.rating = rating;
        }

        public int getVotesCount() {
            return votesCount;
        }

        public void setVotesCount(int votesCount) {
            this.votesCount = votesCount;
        }

    }

}
