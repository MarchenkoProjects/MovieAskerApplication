package mos.edu.client.movieasker.fragment;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import mos.edu.client.movieasker.app.Constants;
import mos.edu.client.movieasker.R;
import mos.edu.client.movieasker.app.ThisApplication;
import mos.edu.client.movieasker.db.User;
import mos.edu.client.movieasker.dto.FavoriteDTO;
import mos.edu.client.movieasker.dto.ShortFilmDTO;

public class FavoriteFragment extends AbstractFragment {

    private static FavoriteFragment instance = new FavoriteFragment();

    public static FavoriteFragment getInstance() {
        return instance;
    }

    public FavoriteFragment() {
        super(ThisApplication.getInstance().getString(R.string.favorite_item_title));
    }

    @Override
    protected boolean contentLoad(int page, int size) {
        final User user = ThisApplication.getInstance().getUser();
        if (user != null) {
            loadContentTask = new LoadFavoriteContentTask(this, Constants.URI.USER_FAVORITE);
            loadContentTask.execute(
                    String.valueOf(user.getGlobalId()),
                    String.valueOf(page),
                    String.valueOf(size)
            );
        }
        return true;
    }

    @Override
    protected void setMessageContent() {
        final User user = ThisApplication.getInstance().getUser();
        if (user != null) {
            contentMessageTextView.setText(ThisApplication.getInstance().getString(R.string.favorite_empty_content_text));
        }
        else {
            contentMessageTextView.setText(ThisApplication.getInstance().getString(R.string.user_not_registered_content_text));
        }
    }

    public static class LoadFavoriteContentTask extends LoadContentTask {

        public LoadFavoriteContentTask(AbstractFragment fragment, String contentUri) {
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

}
