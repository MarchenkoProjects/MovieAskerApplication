package mos.edu.client.movieasker;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import mos.edu.client.movieasker.adapter.TabsFragmentPagerAdapter;
import mos.edu.client.movieasker.fragment.FavoriteFragment;
import mos.edu.client.movieasker.fragment.LookedFragment;
import mos.edu.client.movieasker.fragment.NewFragment;

public class MainActivity extends AppCompatActivity {
    private static final int MAIN_LAYOUT = R.layout.activity_main;
    private static final int MAIN_MENU = R.menu.menu_main;

    private Toolbar mainToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(MAIN_LAYOUT);

        loadViews();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout mainDrawerLayout = (DrawerLayout) findViewById(R.id.main_drawer_layout);
        if (mainDrawerLayout.isDrawerOpen(GravityCompat.START))
            mainDrawerLayout.closeDrawer(GravityCompat.START);
        else
            super.onBackPressed();
    }

    private void loadViews() {
        initToolbar();
        initNavigationDrawer();
        initTabs();
    }

    private void initToolbar() {
        mainToolbar = (Toolbar) findViewById(R.id.main_toolbar);
        mainToolbar.setTitle(R.string.app_name);
        mainToolbar.inflateMenu(MAIN_MENU);
    }

    private void initNavigationDrawer() {
        DrawerLayout mainDrawerLayout = (DrawerLayout) findViewById(R.id.main_drawer_layout);
        ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(
                this, mainDrawerLayout, mainToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mainDrawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();
    }

    private void initTabs() {
        TabsFragmentPagerAdapter tabsAdapter = new TabsFragmentPagerAdapter(getSupportFragmentManager());
        tabsAdapter.addFragment(NewFragment.getInstance(this));
        tabsAdapter.addFragment(FavoriteFragment.getInstance(this));
        tabsAdapter.addFragment(LookedFragment.getInstance(this));

        ViewPager mainViewPager = (ViewPager) findViewById(R.id.main_view_pager);
        mainViewPager.setAdapter(tabsAdapter);

        TabLayout mainTabLayout = (TabLayout) findViewById(R.id.main_tabs_layout);
        mainTabLayout.setupWithViewPager(mainViewPager);
    }

}
