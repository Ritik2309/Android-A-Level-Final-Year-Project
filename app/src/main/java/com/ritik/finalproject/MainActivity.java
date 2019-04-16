package com.ritik.finalproject;

import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {


    private DrawerLayout drawer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.draw_layout);

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

//Below is the code for the default display fragment for when the app first opens or is closed then reopened. This is needed so that the user si not just displayed with nothing.
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new NotesFragement()).commit();
            navigationView.setCheckedItem(R.id.nav_notes);
        }

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        //Here the main fragment container is replaced with the fragment of whatever button is pressed.
        switch (item.getItemId()) {
            case R.id.nav_notes:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new NotesFragement()).commit();
                break;
            case R.id.nav_quiz:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new QuizFragement()).commit();
                break;
            case R.id.nav_photos:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new PhotosFragement()).commit();
                break;
            case R.id.nav_calender:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new CalendarViews()).commit();
                break;
            case R.id.nav_createquiz:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new CreateQuizFragement()).commit();
                break;
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        //This code is for when the back button at the bottom is pressed and if the navigation menu is displaying then this is closed rather than the app being exited.
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}
