package mos.edu.client.movieasker.task;

import android.os.AsyncTask;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import mos.edu.client.movieasker.activity.dialog.DialogManager;
import mos.edu.client.movieasker.app.ThisApplication;
import mos.edu.client.movieasker.dto.ShortFilmDTO;
import mos.edu.client.movieasker.fragment.AbstractFragment;

public class LoadContentTask extends AsyncTask<String, Void, Collection<ShortFilmDTO>> {

    private AbstractFragment fragment;
    protected final String contentUri;

    public LoadContentTask(AbstractFragment fragment, String contentUri) {
        this.fragment = fragment;
        this.contentUri = contentUri;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        fragment.showRepeatLoadingButton(false);
        fragment.showLoadingProgressBar(true);
    }

    @Override
    protected Collection<ShortFilmDTO> doInBackground(String... params) {
        final RestTemplate template = ThisApplication.getInstance().getRestTemplate();

        final ResponseEntity<ShortFilmDTO> response;
        try {
            response = template.getForEntity(contentUri, ShortFilmDTO.class, (Object[]) params);
        } catch (RestClientException e) {
            return null;
        }

        if (response.getStatusCode() == HttpStatus.NO_CONTENT) {
            return Collections.emptyList();
        }

        return response.getBody().getContent();
    }

    @Override
    protected void onPostExecute(Collection<ShortFilmDTO> films) {
        fragment.showLoadingProgressBar(false);

        if (films == null) {
            DialogManager.createAndShowDialog(fragment.getContext(), DialogManager.BAD_INTERNET_CONNECTION);
            fragment.showRepeatLoadingButton(true);
        } else if (!films.isEmpty()) {
            fragment.getAdapter().addFilms(new ArrayList<>(films));
            fragment.showMessageContent();
        }

        super.onPostExecute(films);
    }

}