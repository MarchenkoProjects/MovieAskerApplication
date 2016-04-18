package mos.edu.client.movieasker.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import mos.edu.client.movieasker.R;
import mos.edu.client.movieasker.ThisApplication;

public class LookedFragment extends AbstractFragment {

    private static LookedFragment instance = new LookedFragment();

    public static LookedFragment getInstance() {
        return instance;
    }

    public LookedFragment() {
        setTitle(ThisApplication.getInstance().getString(R.string.looked_item_title));
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);

        String messageContentEmpty;
        if (ThisApplication.getInstance().getUser() == null) {
            messageContentEmpty = ThisApplication.getInstance().getString(R.string.user_not_registered_content_text);
        }
        else {
            messageContentEmpty = ThisApplication.getInstance().getString(R.string.looked_empty_content_text);
        }
        contentEmptyTextView.setText(messageContentEmpty);

        return view;
    }
}
