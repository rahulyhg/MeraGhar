package com.dudefinanceindia.meraghar;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.github.ybq.android.spinkit.SpinKitView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Home extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener{

    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    private NavigationView navigationView;
    private ImageView background_iv;

    private RecyclerView recyclerView;
    private ImageButton back_ib;

    // Creating RecyclerView.Adapter.
    private RecyclerView.Adapter adapter ;
    private List<DataForRecyclerView> list = new ArrayList<>();

    private static final String PICK_UP_BASE = "pick_up_base";
    public Button add_new_b;

    SpinKitView sv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar =  findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        navigationView =  findViewById(R.id.navigation);
        navigationView.setNavigationItemSelectedListener(this);


        TextView search_et = findViewById(R.id.ch_search_tv);
        //sv = findViewById(R.id.ep_spin);


        recyclerView = findViewById(R.id.ch_recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.isDuplicateParentStateEnabled();
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(Home.this);
        // Setting RecyclerView layout as LinearLayout.
        recyclerView.setLayoutManager(mLayoutManager);


    }

//    getting properties for rent
    private void getPropertiesForRent() {
        databaseReference.child("properties").child("rent").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(list!=null) {
                    list.clear();  // v v v v important (eliminate duplication of data)
                }
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    DataForRecyclerView data = postSnapshot.getValue(DataForRecyclerView.class);
                    Objects.requireNonNull(data).setKey(postSnapshot.getKey());
                    list.add(data);

                }

                adapter = new RentRecyclerViewAdapter(Home.this, list);
                recyclerView.setAdapter(adapter);

                if (list.isEmpty()){
                    Toast.makeText(Home.this, "Nothing to show", Toast.LENGTH_SHORT).show();
                }
                //dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
    //    getting properties for rent

    public void onStart(){
        super.onStart();
       // checkIfDealerLoggedIn();

        getPropertiesForRent();

        MySharedPrefs mySharedPrefs = new MySharedPrefs(Home.this);
        String logged_in_or_not = mySharedPrefs.getLoggedInOrNot();

        if (TextUtils.equals(logged_in_or_not, "true")){
            final String[] menus = {"Dealer Profile", "Add Property", "Logout"};
            ifLoggedIn(menus);
        }
        else {
            final String[] menus = {"Dealer Login"};
            ifLoggedIn(menus);
        }


    }

    private void ifLoggedIn(final String[] menus){
        //final String[] menus = {"Profile"};
        //CustomAdapter we created for Customize Navigation
        ListView navlist = (ListView) findViewById(R.id.list);
        NavPanelListAdapter adapter = new NavPanelListAdapter(this, menus);
        navlist.setAdapter(adapter);

        navlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                String selectedMenu = menus[i];

                switch (selectedMenu) {
                    case "Dealer Login":
                        startActivity(new Intent(Home.this, DealerPhoneLogin.class));
                        break;
                    case "Dealer Profile":
                        startActivity(new Intent(Home.this, DealerProfile.class));
                        break;
                    case "Add Property":
                        MySharedPrefs mySharedPrefs = new MySharedPrefs(Home.this);
                        mySharedPrefs.clearMapLocation();
                        startActivity(new Intent(Home.this, AddPropertyForRent.class));
                        break;
                    case "Logout":
                        Logout();
                        break;
                    default:
                        break;

                }

                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);


            }
        });
    }

    private void Logout(){

        new MaterialDialog.Builder(this)
                .title("Logout")
                .titleColor(Color.BLACK)
                .content("Are You Sure to Logout?")
                .contentColor(Color.BLACK)
                .positiveText("Yes")
                .positiveColor(getResources().getColor(R.color.red))
                .negativeText("No")
                .icon(getResources().getDrawable(R.drawable.ic_logout_black))
                .backgroundColor(getResources().getColor(R.color.white))
                .negativeColor(getResources().getColor(R.color.lightGreen))
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull final MaterialDialog dialog, @NonNull DialogAction which) {

                        FirebaseAuth mAuth = FirebaseAuth.getInstance();
                        mAuth.signOut();
                        MySharedPrefs mySharedPrefs = new MySharedPrefs(Home.this);
                        mySharedPrefs.clearAllPrefs();
                        //finishAffinity();
                      //  Home.this.recreate();

                        Home.this.finish();
                        finishAffinity();
                        startActivity(new Intent(Home.this, Home.class));

                        // TODO
                    }
                })
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        // TODO
                    }
                }) .onNeutral(new MaterialDialog.SingleButtonCallback() {
            @Override
            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                // TODO
            }
        })
                .show();

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