package mos.edu.client.movieasker.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import junit.framework.Assert;

import mos.edu.client.movieasker.R;
import mos.edu.client.movieasker.app.ThisApplication;
import mos.edu.client.movieasker.db.User;
import mos.edu.client.movieasker.dto.ReviewDTO;
import mos.edu.client.movieasker.task.ReviewToFilmTask;

public class ReviewActivity extends AppCompatActivity {
    private static final int LAYOUT = R.layout.activity_review;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(LAYOUT);

        loadViews();
    }

    public void addReviewClick(View view) {
        EditText headerEditText = (EditText) findViewById(R.id.header_edit_text);
        Assert.assertNotNull(headerEditText);
        EditText reviewEditText = (EditText) findViewById(R.id.review_edit_text);
        Assert.assertNotNull(reviewEditText);

        final String header = headerEditText.getText().toString();
        if (header.isEmpty()) {
            Toast.makeText(this, R.string.header_empty_warning, Toast.LENGTH_SHORT).show();
        } else {
            final String textReview = reviewEditText.getText().toString();
            if (textReview.isEmpty()) {
                Toast.makeText(this, R.string.review_empty_warning, Toast.LENGTH_SHORT).show();
            } else {
                final ReviewDTO review = new ReviewDTO(header, textReview);

                final User user = ThisApplication.getInstance().getUser();
                final int userId = user.getGlobalId();

                final int filmId = getIntent().getIntExtra(FilmActivity.FILM_ID_PARAM, FilmActivity.DEFAULT_FILM_ID);
                Assert.assertTrue(filmId != FilmActivity.DEFAULT_FILM_ID);

                new ReviewToFilmTask(this, review).execute(String.valueOf(userId), String.valueOf(filmId));
            }
        }
    }

    public void showProgressBar(boolean isShow) {
        ProgressBar loadFilmProgressBar = (ProgressBar) findViewById(R.id.adding_review_progressbar);
        Assert.assertNotNull(loadFilmProgressBar);

        if (isShow) {
            loadFilmProgressBar.setVisibility(View.VISIBLE);
        } else {
            loadFilmProgressBar.setVisibility(View.GONE);
        }
    }

    private void loadViews() {
        initToolbar();
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.review_toolbar);
        Assert.assertNotNull(toolbar);
        toolbar.setTitle(R.string.review_title);
    }

}
