package mos.edu.client.movieasker.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import org.springframework.hateoas.PagedResources;

@JsonIgnoreProperties(ignoreUnknown = true)
public class FavoriteDTO extends PagedResources<FavoriteDTO> {

    private ShortFilmDTO film;
    private boolean looked;

    protected FavoriteDTO() {}

    public ShortFilmDTO getFilm() {
        return film;
    }

    public void setFilm(ShortFilmDTO film) {
        this.film = film;
    }

    public boolean isLooked() {
        return looked;
    }

    public void setLooked(boolean looked) {
        this.looked = looked;
    }

}
