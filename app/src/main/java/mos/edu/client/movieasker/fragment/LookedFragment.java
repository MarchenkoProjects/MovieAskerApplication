package mos.edu.client.movieasker.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import mos.edu.client.movieasker.R;

public class LookedFragment extends AbstractFragment {
    private static final int FRAGMENT_LAYOUT = R.layout.fragment_new;

    private static LookedFragment instance = new LookedFragment();

    public static LookedFragment getInstance(Context context) {
        instance.setTitle(context.getString(R.string.tab_looked));
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
