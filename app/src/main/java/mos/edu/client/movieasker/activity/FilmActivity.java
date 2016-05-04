package mos.edu.client.movieasker.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import junit.framework.Assert;

import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import mos.edu.client.movieasker.R;
import mos.edu.client.movieasker.activity.dialog.DialogManager;
import mos.edu.client.movieasker.adapter.FilmListAdapter;
import mos.edu.client.movieasker.adapter.PersonListAdapter;
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

    private TextView producersTextView;
    private TextView writersTextView;
    private TextView directorsTextView;

    private TextView descriptionTextView;

    private ProgressBar loadFilmProgressBar;

    private LoadFilmTask loadFilmTask = null;

    private PersonListAdapter adapter = new PersonListAdapter();

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
        initPersonsRecyclerView();

        posterImageView = (ImageView) findViewById(R.id.poster_film);
        alternativeNameTextView = (TextView) findViewById(R.id.alternative_name_film);
        originalNameTextView = (TextView) findViewById(R.id.original_name_film);
        yearTextView = (TextView) findViewById(R.id.year_film);
        genresTextView = (TextView) findViewById(R.id.genres_film);

        durationTextView = (TextView) findViewById(R.id.duration_film);
        countriesTextView = (TextView) findViewById(R.id.countries_film);

        sloganTextView = (TextView) findViewById(R.id.slogan_film);

        producersTextView = (TextView) findViewById(R.id.producers_film);
        writersTextView = (TextView) findViewById(R.id.writers_film);
        directorsTextView = (TextView) findViewById(R.id.directors_film);

        descriptionTextView = (TextView) findViewById(R.id.description_film);

        loadFilmProgressBar = (ProgressBar) findViewById(R.id.loading_film_progressbar);
    }

    private void initToolbar() {
        final Toolbar toolbar = (Toolbar) findViewById(R.id.film_toolbar);
        if (toolbar != null) {
            toolbar.setTitle(R.string.film_description_title);
            setSupportActionBar(toolbar);
        }

        final ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    private void initPersonsRecyclerView() {
        RecyclerView personRecyclerView = (RecyclerView) findViewById(R.id.persons_recycler_view);
        if (personRecyclerView != null) {
            personRecyclerView.setLayoutManager(
                    new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
            );
            personRecyclerView.setAdapter(adapter);
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

    private void bindViewFilm(FilmDTO film) {
        ImageLoader.getInstance().displayImage(
                Constants.URI.POSTERS + film.getPosterUrl(),
                posterImageView,
                FilmListAdapter.IMAGE_OPTIONS
        );

        alternativeNameTextView.setText(film.getAlternativeName());
        originalNameTextView.setText(film.getOriginalName());
        sloganTextView.setText(film.getSlogan());

        yearTextView.setText(String.valueOf(film.getYear()));
        genresTextView.setText(film.getGenresCommaDelimitedString());
        durationTextView.setText(film.getDurationPrettyFormatString());
        countriesTextView.setText(film.getCountriesCommaDelimitedString());

        producersTextView.setText(film.getProducersCommaDelimitedString());
        writersTextView.setText(film.getWritersCommaDelimitedString());
        directorsTextView.setText(film.getDirectorsCommaDelimitedString());

        adapter.setPersons(film.getActors());

        descriptionTextView.setText(film.getDescription());
    }

    private void showLoadingProgressBar(boolean show) {
        ScrollView contentScrollView = (ScrollView) findViewById(R.id.film_content_scroll_view);
        if (show) {
            if (contentScrollView != null) {
                contentScrollView.setVisibility(View.INVISIBLE);
            }
            loadFilmProgressBar.setVisibility(View.VISIBLE);
        }
        else {
            loadFilmProgressBar.setVisibility(View.GONE);
            if (contentScrollView != null) {
                contentScrollView.setVisibility(View.VISIBLE);
            }
        }
    }

    private static class LoadFilmTask extends AsyncTask<Integer, Void, FilmDTO> {

        private FilmActivity activity;

        public LoadFilmTask(FilmActivity activity) {
            attachActivity(activity);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            activity.showLoadingProgressBar(true);
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
                activity.bindViewFilm(film);
            }

            activity.showLoadingProgressBar(false);
            activity = null;
            super.onPostExecute(film);
        }

        public void attachActivity(FilmActivity activity) {
            this.activity = activity;
        }

    }

}
