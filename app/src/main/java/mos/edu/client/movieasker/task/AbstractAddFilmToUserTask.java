package mos.edu.client.movieasker.task;

import android.os.AsyncTask;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

import mos.edu.client.movieasker.activity.FilmActivity;
import mos.edu.client.movieasker.app.ThisApplication;

public abstract class AbstractAddFilmToUserTask extends AsyncTask<String, Void, Boolean> {

    protected FilmActivity activity;
    private final String uri;

    public AbstractAddFilmToUserTask(FilmActivity activity, String uri) {
        this.activity = activity;
        this.uri = uri;
    }

    @Override
    protected Boolean doInBackground(String... params) {
        final RestTemplate template = ThisApplication.getInstance().getRestTemplate();

        ResponseEntity<String> response;
        try {
            response =
                    template.exchange(uri, HttpMethod.POST, prepareRequest(), String.class, (Object[]) params);
        } catch (RestClientException e) {
            return false;
        }

        return response.getStatusCode() == HttpStatus.CREATED;
    }

    @Override
    protected void onPostExecute(Boolean created) {
        activity = null;
        super.onPostExecute(created);
    }

    private HttpEntity<?> prepareRequest() {
        final HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new HttpEntity<>(headers);
    }

}
