package mos.edu.client.movieasker.fragment;

import mos.edu.client.movieasker.Constants;
import mos.edu.client.movieasker.R;
import mos.edu.client.movieasker.ThisApplication;

public class NewFragment extends AbstractFragment {

    private static NewFragment instance = new NewFragment();

    public static NewFragment getInstance() {
        return instance;
    }

    public NewFragment() {
        super(ThisApplication.getInstance().getString(R.string.new_item_title));
    }

    @Override
    protected boolean contentLoad(int page, int size) {
        loadContentTask = new LoadContentTask(this, Constants.URI.FILMS_URI);
        loadContentTask.execute(String.valueOf(page), String.valueOf(size));
        return true;
    }

    @Override
    protected void setMessageContent() {
        contentMessageTextView.setText(ThisApplication.getInstance().getString(R.string.new_empty_content_text));
    }

}
