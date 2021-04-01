package com.example.asdaclickcollectcustomersystem;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

public class HomePage extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawer;
    public String userid;
    public String orderid = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String username = getIntent().getExtras().getString("username"); // gets bundle extra from mainactivity
        userid = getIntent().getExtras().getString("userid"); // gets bundle extra from mainactivity

        setContentView(R.layout.activity_home_page); // assigns relevant layout

        final NavigationView navigationView = findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        TextView navUsername = headerView.findViewById(R.id.username_text);
        navUsername.setText(username); // sets username in layout to stored username

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar); // sets toolbar for navigation

        drawer = findViewById(R.id.drawer_layout); // sets drawer layout for navigation
        navigationView.setNavigationItemSelectedListener(this);

        ImageButton img = (ImageButton) findViewById(R.id.image_button); // toolbar button for viewbasket
        img.setOnClickListener(new View.OnClickListener() { // defines onclicklistener for viewbasket button
            public void onClick(View v) {
                navigationView.setCheckedItem(R.id.nav_basket);
                getSupportActionBar().setTitle("Your Basket");
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new BasketFragment()).commit(); // replaces current view with new layout
            }
        });

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.nav_drawer_open, R.string.nav_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        if (savedInstanceState == null) {
            getSupportActionBar().setTitle("Products");
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new ProductsFragment()).commit(); // replaces current view with new layout
            navigationView.setCheckedItem(R.id.nav_products);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.nav_products:
                getSupportActionBar().setTitle("View Products");
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new ProductsFragment()).commit(); // replaces current view with new layout
                break;
            case R.id.nav_basket:
                getSupportActionBar().setTitle("Your Basket");
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new BasketFragment()).commit(); // replaces current view with new layout
                break;
            case R.id.nav_logOut:
                this.finish(); // ends current activity if user chooses to log out
                break;
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() { // used stack to store previous fragments so that navigation can be used properly
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    public String getUserid() {
        return userid;
    } // returns userid

    public String getOrderid() {
        return orderid;
    } // returns orderid

    public void setOrderid(String Orderid) {
        orderid = Orderid;
    } // sets orderid

    public void refreshBasket() { // refreshes basket to show updated layout after item is deleted or order is submitted
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new BasketFragment()).commit();
    }
}
