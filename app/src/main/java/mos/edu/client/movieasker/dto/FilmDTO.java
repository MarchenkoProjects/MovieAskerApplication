package mos.edu.client.movieasker.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import org.springframework.hateoas.ResourceSupport;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class FilmDTO extends ResourceSupport {

    private int idFilm;
    private String posterUrl;
    private String alternativeName;
    private String originalName;
    private int year;
    private String description;
    private byte duration;
    private String slogan;
    private long budget;
    private long worldFees;

    private List<GenreDTO> genres;
    private List<CountryDTO> countries;

    private List<PersonDTO> writers;
    private List<PersonDTO> producers;
    private List<PersonDTO> directors;
    private List<PersonDTO> actors;

    private ShortFilmDTO.RatingDTO rating;

    protected FilmDTO() {}

    public FilmDTO(int idFilm, String posterUrl, String alternativeName, int year) {
        this.idFilm = idFilm;
        this.posterUrl = posterUrl;
        this.alternativeName = alternativeName;
        this.year = year;
    }

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public byte getDuration() {
        return duration;
    }

    public void setDuration(byte duration) {
        this.duration = duration;
    }

    public String getSlogan() {
        return slogan;
    }

    public void setSlogan(String slogan) {
        this.slogan = slogan;
    }

    public long getBudget() {
        return budget;
    }

    public void setBudget(long budget) {
        this.budget = budget;
    }

    public long getWorldFees() {
        return worldFees;
    }

    public void setWorldFees(long worldFees) {
        this.worldFees = worldFees;
    }

    public List<GenreDTO> getGenres() {
        return genres;
    }

    public void setGenres(List<GenreDTO> genres) {
        this.genres = genres;
    }

    public List<CountryDTO> getCountries() {
        return countries;
    }

    public void setCountries(List<CountryDTO> countries) {
        this.countries = countries;
    }

    public List<PersonDTO> getWriters() {
        return writers;
    }

    public void setWriters(List<PersonDTO> writers) {
        this.writers = writers;
    }

    public List<PersonDTO> getProducers() {
        return producers;
    }

    public void setProducers(List<PersonDTO> producers) {
        this.producers = producers;
    }

    public List<PersonDTO> getDirectors() {
        return directors;
    }

    public void setDirectors(List<PersonDTO> directors) {
        this.directors = directors;
    }

    public List<PersonDTO> getActors() {
        return actors;
    }

    public void setActors(List<PersonDTO> actors) {
        this.actors = actors;
    }

    public ShortFilmDTO.RatingDTO getRating() {
        return rating;
    }

    public void setRating(ShortFilmDTO.RatingDTO rating) {
        this.rating = rating;
    }

    public static class GenreDTO extends ResourceSupport {

        private int idGenre;
        private String genreRu;

        protected GenreDTO() {}

        public int getIdGenre() {
            return idGenre;
        }

        public void setIdGenre(int idGenre) {
            this.idGenre = idGenre;
        }

        public String getGenreRu() {
            return genreRu;
        }

        public void setGenreRu(String genreRu) {
            this.genreRu = genreRu;
        }

    }

    public static class CountryDTO extends ResourceSupport {

        private int idCountry;
        private String countryRu;

        protected CountryDTO() {}

        public int getIdCountry() {
            return idCountry;
        }

        public void setIdCountry(int idCountry) {
            this.idCountry = idCountry;
        }

        public String getCountryRu() {
            return countryRu;
        }

        public void setCountryRu(String countryRu) {
            this.countryRu = countryRu;
        }

    }

    public static class PersonDTO extends ResourceSupport {

        private int idPerson;
        private String fotoUrl;
        private String nameRu;
        private String nameEn;

        protected PersonDTO() {}

        public int getIdPerson() {
            return idPerson;
        }

        public void setIdPerson(int idPerson) {
            this.idPerson = idPerson;
        }

        public String getFotoUrl() {
            return fotoUrl;
        }

        public void setFotoUrl(String fotoUrl) {
            this.fotoUrl = fotoUrl;
        }

        public String getNameRu() {
            return nameRu;
        }

        public void setNameRu(String nameRu) {
            this.nameRu = nameRu;
        }

        public String getNameEn() {
            return nameEn;
        }

        public void setNameEn(String nameEn) {
            this.nameEn = nameEn;
        }

    }

}
