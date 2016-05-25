package mos.edu.client.movieasker.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import junit.framework.Assert;

import mos.edu.client.movieasker.R;
import mos.edu.client.movieasker.activity.dialog.DialogManager;
import mos.edu.client.movieasker.adapter.FilmListAdapter;
import mos.edu.client.movieasker.adapter.PersonListAdapter;
import mos.edu.client.movieasker.app.Constants;
import mos.edu.client.movieasker.app.ThisApplication;
import mos.edu.client.movieasker.db.User;
import mos.edu.client.movieasker.dto.FilmDTO;
import mos.edu.client.movieasker.task.FilmToFavoriteTask;
import mos.edu.client.movieasker.task.FilmToLookedTask;
import mos.edu.client.movieasker.task.LoadFilmTask;

public class FilmActivity extends AppCompatActivity implements View.OnClickListener {
    private static final int LAYOUT = R.layout.activity_film;

    public static final String FILM_ID_PARAM = "filmIdParam";
    public static final int DEFAULT_FILM_ID = -1;

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
                new FilmToFavoriteTask(this)
                        .execute(String.valueOf(user.getGlobalId()), String.valueOf(getFilmId()));
                break;
            case R.id.looked_film:
                new FilmToLookedTask(this)
                        .execute(String.valueOf(user.getGlobalId()), String.valueOf(getFilmId()), "1");
                break;
            case R.id.review_film:
                showReviewActivity();
                break;
        }
    }

    public void bindViewFilm(FilmDTO film) {
        ImageView posterImageView = (ImageView) findViewById(R.id.poster_film);
        Assert.assertNotNull(posterImageView);
        ImageLoader.getInstance().displayImage(
                Constants.URI.POSTERS + film.getPosterUrl(),
                posterImageView,
                FilmListAdapter.IMAGE_OPTIONS
        );

        TextView alternativeNameTextView = (TextView) findViewById(R.id.alternative_name_film);
        Assert.assertNotNull(alternativeNameTextView);
        alternativeNameTextView.setText(film.getAlternativeName());

        TextView originalNameTextView = (TextView) findViewById(R.id.original_name_film);
        Assert.assertNotNull(originalNameTextView);
        originalNameTextView.setText(film.getOriginalName());

        TextView yearTextView = (TextView) findViewById(R.id.year_film);
        Assert.assertNotNull(yearTextView);
        yearTextView.setText(String.valueOf(film.getYear()));

        TextView genresTextView = (TextView) findViewById(R.id.genres_film);
        Assert.assertNotNull(genresTextView);
        genresTextView.setText(film.getGenresCommaDelimitedString());

        TextView durationTextView = (TextView) findViewById(R.id.duration_film);
        Assert.assertNotNull(durationTextView);
        durationTextView.setText(film.getDurationPrettyFormatString());

        TextView countriesTextView = (TextView) findViewById(R.id.countries_film);
        Assert.assertNotNull(countriesTextView);
        countriesTextView.setText(film.getCountriesCommaDelimitedString());

        TextView sloganTextView = (TextView) findViewById(R.id.slogan_film);
        Assert.assertNotNull(sloganTextView);
        sloganTextView.setText(film.getSlogan());

        TextView producersTextView = (TextView) findViewById(R.id.producers_film);
        Assert.assertNotNull(producersTextView);
        producersTextView.setText(film.getProducersCommaDelimitedString());

        TextView writersTextView = (TextView) findViewById(R.id.writers_film);
        Assert.assertNotNull(writersTextView);
        writersTextView.setText(film.getWritersCommaDelimitedString());

        TextView directorsTextView = (TextView) findViewById(R.id.directors_film);
        Assert.assertNotNull(directorsTextView);
        directorsTextView.setText(film.getDirectorsCommaDelimitedString());

        TextView descriptionTextView = (TextView) findViewById(R.id.description_film);
        Assert.assertNotNull(descriptionTextView);
        descriptionTextView.setText(film.getDescription());

        RatingBar ratingRatingBar = (RatingBar) findViewById(R.id.rating_film);
        Assert.assertNotNull(ratingRatingBar);
        ratingRatingBar.setRating((float) film.getRating().getRating());

        TextView votesCountTextView = (TextView) findViewById(R.id.votes_count_film);
        Assert.assertNotNull(votesCountTextView);
        votesCountTextView.setText(String.valueOf(film.getRating().getVotesCount()));

        actorsAdapter.setPersons(film.getActors());
    }

    public void showProgressBar(boolean isShow) {
        ProgressBar loadFilmProgressBar = (ProgressBar) findViewById(R.id.loading_film_progressbar);
        Assert.assertNotNull(loadFilmProgressBar);

        if (isShow) {
            loadFilmProgressBar.setVisibility(View.VISIBLE);
        } else {
            loadFilmProgressBar.setVisibility(View.GONE);
        }
    }

    public void showContent(boolean isShow) {
        ScrollView contentScrollView = (ScrollView) findViewById(R.id.film_content_scroll_view);
        Assert.assertNotNull(contentScrollView);

        if (isShow) {
            contentScrollView.setVisibility(View.VISIBLE);
        } else {
            contentScrollView.setVisibility(View.INVISIBLE);
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

    public void setReviewImage(boolean isAdded) {
        if (isAdded) {
            reviewImageView.setImageResource(R.mipmap.ic_review_on);
        } else {
            reviewImageView.setImageResource(R.mipmap.ic_review_off);
        }
    }

    private void loadViews() {
        initToolbar();
        initActorsRecyclerView();

        favoriteImageView = (ImageView) findViewById(R.id.favorite_film);
        Assert.assertNotNull(favoriteImageView);
        favoriteImageView.setOnClickListener(this);

        lookedImageView = (ImageView) findViewById(R.id.looked_film);
        Assert.assertNotNull(lookedImageView);
        lookedImageView.setOnClickListener(this);

        reviewImageView = (ImageView) findViewById(R.id.review_film);
        Assert.assertNotNull(reviewImageView);
        reviewImageView.setOnClickListener(this);
    }

    private void initToolbar() {
        final Toolbar toolbar = (Toolbar) findViewById(R.id.film_toolbar);
        Assert.assertNotNull(toolbar);
        toolbar.setTitle(R.string.film_description_title);
    }

    private void initActorsRecyclerView() {
        RecyclerView actorsRecyclerView = (RecyclerView) findViewById(R.id.actors_recycler_view);
        Assert.assertNotNull(actorsRecyclerView);
        actorsRecyclerView.setLayoutManager(
                new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        );
        actorsRecyclerView.setAdapter(actorsAdapter);
    }

    private void loadFullDescriptionFilm(int filmId) {
        final Object lastCustomInstance = getLastCustomNonConfigurationInstance();
        if (lastCustomInstance == null) {
            loadFilmTask = new LoadFilmTask(this);
            loadFilmTask.execute(filmId);
        } else {
            loadFilmTask = (LoadFilmTask) lastCustomInstance;
            if (loadFilmTask.getStatus().equals(AsyncTask.Status.RUNNING)) {
                loadFilmTask.attachActivity(this);
            }
        }
    }

    private int getFilmId() {
        return getIntent().getIntExtra(FILM_ID_PARAM, DEFAULT_FILM_ID);
    }

    private void showReviewActivity() {
        final Intent reviewActivity = new Intent(this, ReviewActivity.class);
        reviewActivity.putExtra(FilmActivity.FILM_ID_PARAM, getFilmId());
        startActivity(reviewActivity);
    }

}
