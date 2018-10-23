package com.dudefinanceindia.meraghar;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class Home extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    private NavigationView navigationView;
    private ImageView background_iv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar =  findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();


        navigationView =  findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        background_iv = navigationView.getHeaderView(0).findViewById(R.id.nh_background_iv);
        Picasso.with(Home.this)
                .load(R.drawable.navigation_background)
                .centerCrop()
                .fit()
                .into(background_iv);

    }

    public void onStart(){
        super.onStart();
        checkIfDealerLoggedIn();
    }


    private void checkIfDealerLoggedIn() {
        //            hide login item
        Menu menu = navigationView.getMenu();
        MenuItem dealer_login_item, dealer_profile_item;
        dealer_login_item = menu.findItem(R.id.nav_dealer_login);
        dealer_profile_item = menu.findItem(R.id.nav_profile);

        MySharedPrefs mySharedPrefs = new MySharedPrefs(Home.this);
        String logged_in_or_not = mySharedPrefs.getLoggedInOrNot();

        if (TextUtils.equals(logged_in_or_not, "true")){
            dealer_profile_item.setVisible(true);
            dealer_login_item.setVisible(false);
        }
        else {
            dealer_profile_item.setVisible(false);
            dealer_login_item.setVisible(true);
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_dealer_login) {
            startActivity(new Intent(Home.this, DealerPhoneLogin.class));
        } else if (id == R.id.nav_profile) {
            startActivity(new Intent(Home.this, DealerProfile.class));

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer =  findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}