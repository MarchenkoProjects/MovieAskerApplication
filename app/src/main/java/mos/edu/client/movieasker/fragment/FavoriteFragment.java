package mos.edu.client.movieasker.fragment;

import mos.edu.client.movieasker.app.Constants;
import mos.edu.client.movieasker.R;
import mos.edu.client.movieasker.app.ThisApplication;
import mos.edu.client.movieasker.db.User;

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
            loadContentTask = new LoadContentTask(this, Constants.URI.USER_FAVORITE);
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

}
