package mos.edu.client.movieasker;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

public class MainActivity extends Activity {
    private static final int MAIN_LAYOUT = R.layout.activity_main;
    private static final int MAIN_MENU = R.menu.menu_main;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(MAIN_LAYOUT);

        loadViews();
    }

    private void loadViews() {
        Toolbar mainToolbar = (Toolbar) findViewById(R.id.mainToolbar);
        mainToolbar.setTitle(R.string.app_name);
        mainToolbar.inflateMenu(MAIN_MENU);
    }

}
