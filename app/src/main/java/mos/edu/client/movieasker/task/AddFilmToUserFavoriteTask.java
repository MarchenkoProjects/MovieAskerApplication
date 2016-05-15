package mos.edu.client.movieasker.task;

import android.widget.Toast;

import mos.edu.client.movieasker.R;
import mos.edu.client.movieasker.activity.FilmActivity;
import mos.edu.client.movieasker.activity.dialog.DialogManager;
import mos.edu.client.movieasker.app.Constants;
import mos.edu.client.movieasker.fragment.FavoriteFragment;

public class AddFilmToUserFavoriteTask extends AbstractAddFilmToUserTask {

    public AddFilmToUserFavoriteTask(FilmActivity activity) {
        super(activity, Constants.URI.POST_USER_FAVORITE);
    }

    @Override
    protected void onPostExecute(Boolean created) {
        if (created) {
            FavoriteFragment.getInstance().updateAdapter();
            Toast.makeText(activity, R.string.film_to_favorite_text, Toast.LENGTH_SHORT).show();
            activity.setFavoriteImage(true);
        }

        super.onPostExecute(created);
    }

}
