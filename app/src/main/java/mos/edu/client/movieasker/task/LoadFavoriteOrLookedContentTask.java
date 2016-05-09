package mos.edu.client.movieasker.task;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import mos.edu.client.movieasker.app.ThisApplication;
import mos.edu.client.movieasker.dto.FavoriteDTO;
import mos.edu.client.movieasker.dto.ShortFilmDTO;
import mos.edu.client.movieasker.fragment.AbstractFragment;

public class LoadFavoriteOrLookedContentTask extends LoadContentTask {

    public LoadFavoriteOrLookedContentTask(AbstractFragment fragment, String contentUri) {
        super(fragment, contentUri);
    }

    @Override
    protected Collection<ShortFilmDTO> doInBackground(String... params) {
        final RestTemplate template = ThisApplication.getInstance().getRestTemplate();

        ResponseEntity<FavoriteDTO> response;
        try {
            response =
                    template.getForEntity(contentUri, FavoriteDTO.class, (Object[]) params);
        } catch (RestClientException e) {
            return null;
        }

        if (response.getStatusCode() == HttpStatus.NO_CONTENT) {
            return Collections.emptyList();
        }

        Collection<ShortFilmDTO> shortFilmCollection = new ArrayList<>();
        for (FavoriteDTO favorite: response.getBody().getContent()) {
            shortFilmCollection.add(favorite.getFilm());
        }
        return shortFilmCollection;
    }
}