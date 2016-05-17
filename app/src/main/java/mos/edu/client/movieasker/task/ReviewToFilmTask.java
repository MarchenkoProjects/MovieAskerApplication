package mos.edu.client.movieasker.task;

import android.os.AsyncTask;
import android.widget.Toast;

import junit.framework.Assert;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import mos.edu.client.movieasker.R;
import mos.edu.client.movieasker.activity.ReviewActivity;
import mos.edu.client.movieasker.activity.dialog.DialogManager;
import mos.edu.client.movieasker.app.Constants;
import mos.edu.client.movieasker.app.ThisApplication;
import mos.edu.client.movieasker.dto.ReviewDTO;

public class ReviewToFilmTask extends AsyncTask<String, Void, Boolean> {

    private ReviewActivity activity;
    private final ReviewDTO review;

    public ReviewToFilmTask(ReviewActivity activity, ReviewDTO review) {
        this.activity = activity;
        this.review = review;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        activity.showProgressBar(true);
    }

    @Override
    protected Boolean doInBackground(String... params) {
        Assert.assertTrue("Params must be 2: userId, filmId", params.length == 2);

        final RestTemplate template = ThisApplication.getInstance().getRestTemplate();

        ResponseEntity<ReviewDTO> response;
        try {
            response = template.postForEntity(
                            Constants.URI.POST_USER_REVIEW,
                            review,
                            ReviewDTO.class,
                            (Object[]) params);
        } catch (RestClientException e) {
            return false;
        }

        return response.getStatusCode() == HttpStatus.CREATED;
    }

    @Override
    protected void onPostExecute(Boolean added) {
        activity.showProgressBar(false);
        if (added) {
            Toast.makeText(activity, R.string.review_added_film_text, Toast.LENGTH_SHORT).show();
            activity.finish();
        } else {
            DialogManager.createAndShowDialog(activity, DialogManager.BAD_INTERNET_CONNECTION);
        }

        super.onPostExecute(added);
    }

}
