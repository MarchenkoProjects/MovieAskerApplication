package mos.edu.client.movieasker.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import junit.framework.Assert;

import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import mos.edu.client.movieasker.R;
import mos.edu.client.movieasker.activity.dialog.DialogManager;
import mos.edu.client.movieasker.app.Constants;
import mos.edu.client.movieasker.app.ThisApplication;
import mos.edu.client.movieasker.dto.FilmDTO;

public class FilmActivity extends AppCompatActivity {
    private static final int LAYOUT = R.layout.activity_film;

    public static final String FILM_ID_PARAM = "filmIdParam";
    private static final int DEFAULT_FILM_ID = -1;

    private ImageView posterImageView;
    private TextView alternativeNameTextView;
    private TextView originalNameTextView;
    private TextView yearTextView;
    private TextView genresTextView;
    private TextView durationTextView;
    private TextView countriesTextView;
    private TextView sloganTextView;

    private TextView descriptionTextView;

    private LoadFilmTask loadFilmTask = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(LAYOUT);

        loadViews();

        final Intent intent = getIntent();
        final int filmId = intent.getIntExtra(FILM_ID_PARAM, DEFAULT_FILM_ID);
        loadFullDescriptionFilm(filmId);
    }

    @Override
    public Object onRetainCustomNonConfigurationInstance() {
        LoadFilmTask oldLoadFilmTask = null;
        if (loadFilmTask != null && loadFilmTask.getStatus().equals(AsyncTask.Status.RUNNING)) {
            oldLoadFilmTask = loadFilmTask;
        }
        return oldLoadFilmTask;
    }

    @Override
    protected void onDestroy() {
        if (loadFilmTask != null) {
            loadFilmTask.cancel(true);
            loadFilmTask = null;
        }
        super.onDestroy();
    }

    private void loadViews() {
        initToolbar();

        posterImageView = (ImageView) findViewById(R.id.poster_film);
        alternativeNameTextView = (TextView) findViewById(R.id.alternative_name_film);
        originalNameTextView = (TextView) findViewById(R.id.original_name_film);
        yearTextView = (TextView) findViewById(R.id.year_film);
        genresTextView = (TextView) findViewById(R.id.genres_film);

        durationTextView = (TextView) findViewById(R.id.duration_film);
        countriesTextView = (TextView) findViewById(R.id.countries_film);

        sloganTextView = (TextView) findViewById(R.id.slogan_film);

        descriptionTextView = (TextView) findViewById(R.id.description_film);
    }

    private void initToolbar() {
        final Toolbar toolbar = (Toolbar) findViewById(R.id.film_toolbar);
        if (toolbar != null) {
            toolbar.setTitle(R.string.film_description_title);
            setSupportActionBar(toolbar);
        }

        final ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeAsUpIndicator(R.mipmap.ic_arrow_back);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    private void loadFullDescriptionFilm(int filmId) {
        final Object lastCustomInstance = getLastCustomNonConfigurationInstance();
        if (lastCustomInstance == null) {
            loadFilmTask = new LoadFilmTask(this);
            loadFilmTask.execute(filmId);
        }
        else {
            loadFilmTask = (LoadFilmTask) lastCustomInstance;
            if (loadFilmTask.getStatus().equals(AsyncTask.Status.RUNNING)) {
                loadFilmTask.attachActivity(this);
            }
        }
    }

    private static class LoadFilmTask extends AsyncTask<Integer, Void, FilmDTO> {

        private FilmActivity activity;

        public LoadFilmTask(FilmActivity activity) {
            attachActivity(activity);
        }

        @Override
        protected FilmDTO doInBackground(Integer... params) {
            Assert.assertTrue("Params must be 1: filmId", params.length == 1);

            final RestTemplate template = ThisApplication.getInstance().getRestTemplate();
            ResponseEntity<FilmDTO> response;
            try {
                response = template.getForEntity(
                        Constants.URI.FILM_BY_ID,
                        FilmDTO.class,
                        (Object[]) params
                );
            } catch (RestClientException e) {
                return null;
            }

            return response.getBody();
        }

        @Override
        protected void onPostExecute(FilmDTO film) {

            if (film == null) {
                DialogManager.showDialog(activity, DialogManager.BAD_INTERNET_CONNECTION);
            }
            else {
                ImageLoader.getInstance().displayImage(
                        Constants.URI.POSTERS + film.getPosterUrl(),
                        activity.posterImageView
                );

                activity.alternativeNameTextView.setText(film.getAlternativeName());
                activity.originalNameTextView.setText(film.getOriginalName());
                activity.yearTextView.setText(String.valueOf(film.getYear()));

                List<FilmDTO.GenreDTO> genres = film.getGenres();
                List<String> genresName = new ArrayList<>();
                for (FilmDTO.GenreDTO genre : genres) {
                    genresName.add(genre.getGenreRu());
                }
                String joinGenres = StringUtils.collectionToDelimitedString(genresName, ", ");
                activity.genresTextView.setText(joinGenres);

                int duration = film.getDuration();
                long hour = TimeUnit.MINUTES.toHours(duration);
                long minute = duration - TimeUnit.HOURS.toMinutes(hour);
                String durationFormat = String.format(Locale.getDefault(), "%02d ч. %02d мин.", hour, minute);
                activity.durationTextView.setText(durationFormat);

                List<FilmDTO.CountryDTO> countries = film.getCountries();
                List<String> countriesName = new ArrayList<>();
                for (FilmDTO.CountryDTO country : countries) {
                    countriesName.add(country.getCountryRu());
                }
                String joinCountries = StringUtils.collectionToDelimitedString(countriesName, ", ");
                activity.countriesTextView.setText(joinCountries);

                activity.descriptionTextView.setText(film.getDescription());

                activity.sloganTextView.setText(film.getSlogan());
            }

            activity = null;

            super.onPostExecute(film);
        }

        public void attachActivity(FilmActivity activity) {
            this.activity = activity;
        }

    }

}
