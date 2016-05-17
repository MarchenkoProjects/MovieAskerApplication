package mos.edu.client.movieasker.task;

import android.widget.Toast;

import mos.edu.client.movieasker.R;
import mos.edu.client.movieasker.activity.FilmActivity;
import mos.edu.client.movieasker.app.Constants;
import mos.edu.client.movieasker.fragment.LookedFragment;

public class FilmToLookedTask extends AbstractFilmToUserTask {

    public FilmToLookedTask(FilmActivity activity) {
        super(activity, Constants.URI.POST_USER_LOOKED);
    }

    @Override
    protected void onPostExecute(Boolean created) {
        if (created) {
            LookedFragment.getInstance().updateAdapter();
            Toast.makeText(activity, R.string.film_to_looked_text, Toast.LENGTH_SHORT).show();
            activity.setLookedImage(true);
        }

        super.onPostExecute(created);
    }
}
