package mos.edu.client.movieasker.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import mos.edu.client.movieasker.R;
import mos.edu.client.movieasker.ThisApplication;

public class FavoriteFragment extends AbstractFragment {

    private static FavoriteFragment instance = new FavoriteFragment();

    public static FavoriteFragment getInstance() {
        return instance;
    }

    public FavoriteFragment() {
        setTitle(ThisApplication.getInstance().getString(R.string.favorite_item_title));
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
            messageContentEmpty = ThisApplication.getInstance().getString(R.string.favorite_empty_content_text);
        }
        contentEmptyTextView.setText(messageContentEmpty);

        return view;
    }

}
