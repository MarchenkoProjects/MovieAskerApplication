package mos.edu.client.movieasker.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import mos.edu.client.movieasker.R;

public class FavoriteFragment extends AbstractFragment {
    private static final int FRAGMENT_LAYOUT = R.layout.fragment_new;

    private static FavoriteFragment instance = new FavoriteFragment();

    public static FavoriteFragment getInstance(Context context) {
        instance.setTitle(context.getString(R.string.favorite_item_title));
        return instance;
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
