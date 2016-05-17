package mos.edu.client.movieasker.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import junit.framework.Assert;

import mos.edu.client.movieasker.R;
import mos.edu.client.movieasker.adapter.TabsFragmentPagerAdapter;
import mos.edu.client.movieasker.app.Constants;
import mos.edu.client.movieasker.app.ThisApplication;
import mos.edu.client.movieasker.db.User;
import mos.edu.client.movieasker.fragment.FavoriteFragment;
import mos.edu.client.movieasker.fragment.LookedFragment;
import mos.edu.client.movieasker.fragment.NewFragment;

public class MainActivity extends AppCompatActivity {
    private static final int LAYOUT = R.layout.activity_main;
    private static final int MENU = R.menu.menu_main;

    private static final int NAVIGATION_HEADER_INDEX = 0;
    private static final String CURRENT_TAB_ITEM_PREFS = "currentTabItem";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(LAYOUT);

        loadViews();
    }

    @Override
    protected void onResume() {
        super.onResume();

        setSavedCurrentTabItem();
    }

    @Override
    protected void onPause() {
        ViewPager viewPager = (ViewPager) findViewById(R.id.main_view_pager);
        Assert.assertNotNull(viewPager);
        int currentTabItem = viewPager.getCurrentItem();
        saveCurrentTabItem(currentTabItem);

        super.onPause();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.main_drawer_layout);
        Assert.assertNotNull(drawerLayout);

        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    private void loadViews() {
        initToolbar();
        initNavigationDrawer();
        initTabs();
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        Assert.assertNotNull(toolbar);
        toolbar.setTitle(R.string.app_name);
        toolbar.inflateMenu(MENU);
    }

    private void initNavigationDrawer() {
        DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.main_drawer_layout);
        Assert.assertNotNull(drawerLayout);

        Toolbar toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        Assert.assertNotNull(toolbar);

        ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(
                this,
                drawerLayout,
                toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close
        );
        drawerToggle.syncState();

        drawerLayout.addDrawerListener(drawerToggle);

        NavigationView navigationView = (NavigationView) findViewById(R.id.main_navigation_view);
        Assert.assertNotNull(navigationView);
        initUserProfileInNavigationHeader(navigationView);
        navigationView.setNavigationItemSelectedListener(navigationItemListener);
    }

    private void initUserProfileInNavigationHeader(NavigationView navigationView) {
        final User user = ThisApplication.getInstance().getUser();
        if (user != null) {
            View navigationHeader = navigationView.getHeaderView(NAVIGATION_HEADER_INDEX);
            TextView profileLoginTextView = (TextView) navigationHeader.findViewById(R.id.profile_login_text_view);
            profileLoginTextView.setText(user.getLogin());

            TextView profileEmailTextView = (TextView) navigationHeader.findViewById(R.id.profile_email_text_view);
            profileEmailTextView.setText(user.getEmail());

            Menu navigationMenu = navigationView.getMenu();
            MenuItem registrationItem = navigationMenu.findItem(R.id.registration_navigation_item);
            registrationItem.setVisible(false);
        }
    }

    private final NavigationView.OnNavigationItemSelectedListener navigationItemListener =
            new NavigationView.OnNavigationItemSelectedListener() {

                @Override
                public boolean onNavigationItemSelected(MenuItem item) {
                    DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.main_drawer_layout);
                    Assert.assertNotNull(drawerLayout);
                    drawerLayout.closeDrawers();

                    switch (item.getItemId()) {
                        case R.id.registration_navigation_item:
                            showRegistrationActivity();
                            break;
                        case R.id.new_navigation_item:
                            showTabByItem(Constants.TAB.NEW_ITEM);
                            break;
                        case R.id.favorite_navigation_item:
                            showTabByItem(Constants.TAB.FAVORITE_ITEM);
                            break;
                        case R.id.looked_navigation_item:
                            showTabByItem(Constants.TAB.LOOKED_ITEM);
                            break;
                        case R.id.settings_navigation_item:
                            break;
                        case R.id.about_navigation_item:
                            showAboutActivity();
                            break;
                    }

                    return true;
                }

                private void showRegistrationActivity() {
                    final Intent registrationActivity =
                            new Intent(MainActivity.this, RegistrationActivity.class);
                    startActivity(registrationActivity);
                }

                private void showAboutActivity() {
                    final Intent aboutActivity =
                            new Intent(MainActivity.this, AboutActivity.class);
                    startActivity(aboutActivity);
                }

            };

    private void initTabs() {
        TabsFragmentPagerAdapter tabsAdapter = new TabsFragmentPagerAdapter(getSupportFragmentManager());
        tabsAdapter.addFragment(NewFragment.getInstance());
        tabsAdapter.addFragment(FavoriteFragment.getInstance());
        tabsAdapter.addFragment(LookedFragment.getInstance());

        ViewPager viewPager = (ViewPager) findViewById(R.id.main_view_pager);
        Assert.assertNotNull(viewPager);
        viewPager.setAdapter(tabsAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.main_tabs_layout);
        Assert.assertNotNull(tabLayout);
        tabLayout.setupWithViewPager(viewPager);
    }

    private void saveCurrentTabItem(int currentTabItem) {
        SharedPreferences preferences = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor preferencesEditor = preferences.edit();
        preferencesEditor.putInt(CURRENT_TAB_ITEM_PREFS, currentTabItem);
        preferencesEditor.apply();
    }

    private void setSavedCurrentTabItem() {
        SharedPreferences preferences = getPreferences(MODE_PRIVATE);
        int currentTabItem = preferences.getInt(CURRENT_TAB_ITEM_PREFS, Constants.TAB.NEW_ITEM);
        showTabByItem(currentTabItem);
    }

    private void showTabByItem(int item) {
        ViewPager viewPager = (ViewPager) findViewById(R.id.main_view_pager);
        Assert.assertNotNull(viewPager);
        viewPager.setCurrentItem(item);
    }

}
