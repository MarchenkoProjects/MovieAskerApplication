package mos.edu.client.movieasker.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import junit.framework.Assert;

import mos.edu.client.movieasker.R;

public class AboutActivity extends AppCompatActivity {
    private static final int LAYOUT = R.layout.activity_about;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(LAYOUT);

        loadViews();
    }

    private void loadViews() {
        initToolbar();
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.about_toolbar);
        Assert.assertNotNull(toolbar);
        toolbar.setTitle(R.string.about_title);
    }

}
