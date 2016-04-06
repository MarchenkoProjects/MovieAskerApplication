package mos.edu.client.movieasker.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import mos.edu.client.movieasker.R;
import mos.edu.client.movieasker.ThisApplication;

public class FavoriteFragment extends AbstractFragment {
    private static final int FRAGMENT_LAYOUT = R.layout.fragment_new;

    private static FavoriteFragment instance = new FavoriteFragment();

    public static FavoriteFragment getInstance() {
        return instance;
    }

    public FavoriteFragment() {
        setTitle(ThisApplication.getInstance().getString(R.string.favorite_item_title));
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(FRAGMENT_LAYOUT, container, false);
    }
}
