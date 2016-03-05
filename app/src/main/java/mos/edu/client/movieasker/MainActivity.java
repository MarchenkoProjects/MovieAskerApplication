package mos.edu.client.movieasker;

import android.app.Activity;
import android.os.Bundle;

public class MainActivity extends Activity {
    private static final int MAIN_LAYOUT = R.layout.activity_main;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(MAIN_LAYOUT);
    }

}
