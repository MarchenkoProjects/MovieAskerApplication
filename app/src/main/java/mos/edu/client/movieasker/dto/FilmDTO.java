package mos.edu.client.movieasker.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import org.springframework.hateoas.ResourceSupport;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

@JsonIgnoreProperties(ignoreUnknown = true)
public class FilmDTO extends ResourceSupport {

    private int idFilm;
    private String posterUrl;
    private String alternativeName;
    private String originalName;
    private int year;
    private String description;
    private int duration;
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

    public int getDuration() {
        return duration;
    }

    public String getDurationPrettyFormatString() {
        final long hour = TimeUnit.MINUTES.toHours(duration);
        final long minute = duration - TimeUnit.HOURS.toMinutes(hour);
        return String.format(Locale.getDefault(), "%d ч. %02d мин.", hour, minute);
    }

    public void setDuration(int duration) {
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

    public String getGenresCommaDelimitedString() {
        final List<String> genreNames = new ArrayList<>();
        for (final FilmDTO.GenreDTO genre : genres) {
            genreNames.add(genre.getGenreRu());
        }
        return StringUtils.collectionToDelimitedString(genreNames, ", ");
    }

    public void setGenres(List<GenreDTO> genres) {
        this.genres = genres;
    }

    public List<CountryDTO> getCountries() {
        return countries;
    }

    public String getCountriesCommaDelimitedString() {
        final List<String> countryNames = new ArrayList<>();
        for (final FilmDTO.CountryDTO country : countries) {
            countryNames.add(country.getCountryRu());
        }
        return StringUtils.collectionToDelimitedString(countryNames, ", ");
    }

    public void setCountries(List<CountryDTO> countries) {
        this.countries = countries;
    }

    public List<PersonDTO> getWriters() {
        return writers;
    }

    public String getWritersCommaDelimitedString() {
        final List<String> writerNames = new ArrayList<>();
        for (final FilmDTO.PersonDTO writer : writers) {
            writerNames.add(writer.getNameRu());
        }
        return StringUtils.collectionToDelimitedString(writerNames, ", ");
    }

    public void setWriters(List<PersonDTO> writers) {
        this.writers = writers;
    }

    public List<PersonDTO> getProducers() {
        return producers;
    }

    public String getProducersCommaDelimitedString() {
        final List<String> producerNames = new ArrayList<>();
        for (final FilmDTO.PersonDTO producer : producers) {
            producerNames.add(producer.getNameRu());
        }
        return StringUtils.collectionToDelimitedString(producerNames, ", ");
    }

    public void setProducers(List<PersonDTO> producers) {
        this.producers = producers;
    }

    public List<PersonDTO> getDirectors() {
        return directors;
    }

    public String getDirectorsCommaDelimitedString() {
        final List<String> directorNames = new ArrayList<>();
        for (final FilmDTO.PersonDTO director : directors) {
            directorNames.add(director.getNameRu());
        }
        return StringUtils.collectionToDelimitedString(directorNames, ", ");
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
