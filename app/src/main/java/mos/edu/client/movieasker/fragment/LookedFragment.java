package mos.edu.client.movieasker.fragment;

import mos.edu.client.movieasker.app.Constants;
import mos.edu.client.movieasker.R;
import mos.edu.client.movieasker.app.ThisApplication;
import mos.edu.client.movieasker.db.User;

public class LookedFragment extends AbstractFragment {

    private static LookedFragment instance = new LookedFragment();

    public static LookedFragment getInstance() {
        return instance;
    }

    public LookedFragment() {
        super(ThisApplication.getInstance().getString(R.string.looked_item_title));
    }

    @Override
    protected boolean contentLoad(int page, int size) {
        final User user = ThisApplication.getInstance().getUser();
        if (user != null) {
            loadContentTask = new LoadContentTask(this, Constants.URI.USER_LOOKED);
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
            contentMessageTextView.setText(ThisApplication.getInstance().getString(R.string.looked_empty_content_text));
        }
        else {
            contentMessageTextView.setText(ThisApplication.getInstance().getString(R.string.user_not_registered_content_text));
        }
    }

}
