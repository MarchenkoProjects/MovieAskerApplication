package mos.edu.client.movieasker.fragment;

import android.content.Context;
import android.support.v4.app.Fragment;

public abstract class AbstractFragment extends Fragment {

    private String title;
    protected Context context;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
