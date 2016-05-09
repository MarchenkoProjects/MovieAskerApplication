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
    protected void onResume() {
        super.onResume();

        setSavedCurrentTabItem();
    }

    @Override
    protected void onPause() {
        int currentTabItem = viewPager.getCurrentItem();
        saveCurrentTabItem(currentTabItem);

        super.onPause();
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        else {
            super.onBackPressed();
        }
    }

    private void loadViews() {
        initToolbar();
        initNavigationDrawer();
        initTabs();
    }

    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        if (toolbar != null) {
            toolbar.setTitle(R.string.app_name);
            toolbar.inflateMenu(MENU);
        }
    }

    private void initNavigationDrawer() {
        drawerLayout = (DrawerLayout) findViewById(R.id.main_drawer_layout);
        if (drawerLayout != null) {
            final ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(
                    this,
                    drawerLayout,
                    toolbar,
                    R.string.navigation_drawer_open,
                    R.string.navigation_drawer_close
            );
            drawerToggle.syncState();

            drawerLayout.addDrawerListener(drawerToggle);
        }

        final NavigationView navigationView = (NavigationView) findViewById(R.id.main_navigation_view);
        if (navigationView != null) {
            initUserProfileInNavigationHeader(navigationView);
            navigationView.setNavigationItemSelectedListener(navigationItemListener);
        }
    }

    private void initUserProfileInNavigationHeader(NavigationView navigationView) {
        final User user = ThisApplication.getInstance().getUser();
        if (user != null) {
            final View navigationHeader = navigationView.getHeaderView(NAVIGATION_HEADER_INDEX);
            TextView profileLoginTextView = (TextView) navigationHeader.findViewById(R.id.profile_login_text_view);
            profileLoginTextView.setText(user.getLogin());

            TextView profileEmailTextView = (TextView) navigationHeader.findViewById(R.id.profile_email_text_view);
            profileEmailTextView.setText(user.getEmail());

            final Menu navigationMenu = navigationView.getMenu();
            final MenuItem registrationItem = navigationMenu.findItem(R.id.registration_navigation_item);
            registrationItem.setVisible(false);
        }
    }

    private final NavigationView.OnNavigationItemSelectedListener navigationItemListener =
            new NavigationView.OnNavigationItemSelectedListener() {

                @Override
                public boolean onNavigationItemSelected(MenuItem item) {
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
        final TabsFragmentPagerAdapter tabsAdapter =
                new TabsFragmentPagerAdapter(getSupportFragmentManager());
        tabsAdapter.addFragment(NewFragment.getInstance());
        tabsAdapter.addFragment(FavoriteFragment.getInstance());
        tabsAdapter.addFragment(LookedFragment.getInstance());

        viewPager = (ViewPager) findViewById(R.id.main_view_pager);
        if (viewPager != null) {
            viewPager.setAdapter(tabsAdapter);
        }

        final TabLayout tabLayout = (TabLayout) findViewById(R.id.main_tabs_layout);
        if (tabLayout != null) {
            tabLayout.setupWithViewPager(viewPager);
        }
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
        viewPager.setCurrentItem(item);
    }

}
