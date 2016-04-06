package mos.edu.client.movieasker.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import mos.edu.client.movieasker.Constants;
import mos.edu.client.movieasker.R;
import mos.edu.client.movieasker.adapter.TabsFragmentPagerAdapter;
import mos.edu.client.movieasker.fragment.FavoriteFragment;
import mos.edu.client.movieasker.fragment.LookedFragment;
import mos.edu.client.movieasker.fragment.NewFragment;

public class MainActivity extends AppCompatActivity {
    private static final int LAYOUT = R.layout.activity_main;
    private static final int MENU = R.menu.menu_main;

    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(LAYOUT);

        loadViews();
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START))
            drawerLayout.closeDrawer(GravityCompat.START);
        else
            super.onBackPressed();
    }

    private void loadViews() {
        initToolbar();
        initNavigationDrawer();
        initTabs();
    }

    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        toolbar.setTitle(R.string.app_name);
        toolbar.inflateMenu(MENU);
    }

    private void initNavigationDrawer() {
        drawerLayout = (DrawerLayout) findViewById(R.id.main_drawer_layout);
        ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(
            this,
            drawerLayout,
            toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        );
        drawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.main_navigation_view);
        navigationView.setNavigationItemSelectedListener(navigationItemListener);
    }

    private NavigationView.OnNavigationItemSelectedListener navigationItemListener =
            new NavigationView.OnNavigationItemSelectedListener() {

                @Override
                public boolean onNavigationItemSelected(MenuItem item) {
                    drawerLayout.closeDrawers();

                    switch (item.getItemId()) {
                        case R.id.registration_navigation_item:
                            showRegistrationActivity();
                            break;
                        case R.id.new_navigation_item:
                            showNewTab();
                            break;
                        case R.id.favorite_navigation_item:
                            showFavoriteTab();
                            break;
                        case R.id.looked_navigation_item:
                            showLookedTab();
                            break;
                        case R.id.settings_navigation_item:
                            break;
                        case R.id.about_navigation_item:
                            break;
                    }

                    return true;
                }

                private void showRegistrationActivity() {
                    Intent registrationActivity =
                            new Intent(MainActivity.this, RegistrationActivity.class);
                    startActivity(registrationActivity);
                }

                private void showNewTab() {
                    viewPager.setCurrentItem(Constants.NEW_TAB_POSITION);
                }

                private void showFavoriteTab() {
                    viewPager.setCurrentItem(Constants.FAVORITE_TAB_POSITION);
                }

                private void showLookedTab() {
                    viewPager.setCurrentItem(Constants.LOOKED_TAB_POSITION);
                }

            };

    private void initTabs() {
        TabsFragmentPagerAdapter tabsAdapter =
                new TabsFragmentPagerAdapter(getSupportFragmentManager());
        tabsAdapter.addFragment(NewFragment.getInstance());
        tabsAdapter.addFragment(FavoriteFragment.getInstance());
        tabsAdapter.addFragment(LookedFragment.getInstance());

        viewPager = (ViewPager) findViewById(R.id.main_view_pager);
        viewPager.setAdapter(tabsAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.main_tabs_layout);
        tabLayout.setupWithViewPager(viewPager);
    }

}
