package mos.edu.client.movieasker.activity;

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

import mos.edu.client.movieasker.R;
import mos.edu.client.movieasker.activity.dialog.DialogManager;
import mos.edu.client.movieasker.adapter.FilmListAdapter;
import mos.edu.client.movieasker.adapter.PersonListAdapter;
import mos.edu.client.movieasker.app.Constants;
import mos.edu.client.movieasker.app.ThisApplication;
import mos.edu.client.movieasker.db.User;
import mos.edu.client.movieasker.dto.FilmDTO;
import mos.edu.client.movieasker.task.AddFilmToUserFavoriteTask;
import mos.edu.client.movieasker.task.AddFilmToUserLookedTask;
import mos.edu.client.movieasker.task.LoadFilmTask;

public class FilmActivity extends AppCompatActivity implements View.OnClickListener {
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

    private ImageView favoriteImageView;
    private ImageView lookedImageView;
    private ImageView reviewImageView;

    private LoadFilmTask loadFilmTask = null;

    private PersonListAdapter actorsAdapter = new PersonListAdapter();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(LAYOUT);

        loadViews();

        loadFullDescriptionFilm(getFilmId());
    }

    @Override
    public Object onRetainCustomNonConfigurationInstance() {
        if (loadFilmTask != null && loadFilmTask.getStatus().equals(AsyncTask.Status.RUNNING)) {
            return loadFilmTask;
        }
        return null;
    }

    @Override
    protected void onDestroy() {
        if (loadFilmTask != null) {
            loadFilmTask.cancel(true);
            loadFilmTask = null;
        }
        super.onDestroy();
    }

    @Override
    public void onClick(View view) {
        final User user = ThisApplication.getInstance().getUser();
        if (user == null) {
            DialogManager.createAndShowDialog(this, DialogManager.USER_NOT_REGISTERED);
            return;
        }

        switch (view.getId()) {
            case R.id.favorite_film:
                new AddFilmToUserFavoriteTask(this)
                        .execute(String.valueOf(user.getGlobalId()), String.valueOf(getFilmId()));
                break;
            case R.id.looked_film:
                new AddFilmToUserLookedTask(this)
                        .execute(String.valueOf(user.getGlobalId()), String.valueOf(getFilmId()), "1");
                break;
            case R.id.review_film:
                break;
        }
    }

    public void bindViewFilm(FilmDTO film) {
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

        actorsAdapter.setPersons(film.getActors());

        descriptionTextView.setText(film.getDescription());
    }

    public void showLoadingProgressBar(boolean isShow) {
        ScrollView contentScrollView = (ScrollView) findViewById(R.id.film_content_scroll_view);
        if (contentScrollView != null) {
            if (isShow) {
                contentScrollView.setVisibility(View.INVISIBLE);
                loadFilmProgressBar.setVisibility(View.VISIBLE);
            } else {
                loadFilmProgressBar.setVisibility(View.GONE);
                contentScrollView.setVisibility(View.VISIBLE);
            }
        }
    }

    public void setFavoriteImage(boolean isAdded) {
        if (isAdded) {
            favoriteImageView.setImageResource(R.mipmap.ic_heart_on);
        } else {
            favoriteImageView.setImageResource(R.mipmap.ic_heart_off);
        }
    }

    public void setLookedImage(boolean isAdded) {
        if (isAdded) {
            lookedImageView.setImageResource(R.mipmap.ic_eye_on);
        } else {
            lookedImageView.setImageResource(R.mipmap.ic_eye_off);
        }
    }

    private void loadViews() {
        initToolbar();
        initActorsRecyclerView();

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

        favoriteImageView = (ImageView) findViewById(R.id.favorite_film);
        if (favoriteImageView != null) {
            favoriteImageView.setOnClickListener(this);
        }

        lookedImageView = (ImageView) findViewById(R.id.looked_film);
        if (lookedImageView != null) {
            lookedImageView.setOnClickListener(this);
        }

        reviewImageView = (ImageView) findViewById(R.id.review_film);
        if (reviewImageView != null) {
            reviewImageView.setOnClickListener(this);
        }
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

    private void initActorsRecyclerView() {
        RecyclerView actorsRecyclerView = (RecyclerView) findViewById(R.id.actors_recycler_view);
        if (actorsRecyclerView != null) {
            actorsRecyclerView.setLayoutManager(
                    new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
            );
            actorsRecyclerView.setAdapter(actorsAdapter);
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

    private int getFilmId() {
        return getIntent().getIntExtra(FILM_ID_PARAM, DEFAULT_FILM_ID);
    }

}
