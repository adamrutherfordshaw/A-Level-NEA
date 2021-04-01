package com.example.asdaclickcollectemployeesystem;

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
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class HomePage extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawer;
    public String currentorderid; // stores current orderid for picking
    public WeightedGraph.Graph graph; // stores the large graph of the products in the store
    public String username; // stores username
    public boolean admin; // stores access level
    public BackgroundWorker backgroundWorker = new BackgroundWorker(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        username = getIntent().getStringExtra("username"); // gets username from bundle extras
        if (getIntent().getStringExtra("admin").contains("0")) { // gets admin from bundle extras
            admin = false;
        } else {
            admin = true;
        }

        setContentView(R.layout.activity_home_page);

        NavigationView navigationView = findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        TextView navUsername = headerView.findViewById(R.id.username_text);
        navUsername.setText(username);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar); // sets toolbar for navigation

        drawer = findViewById(R.id.drawer_layout); // sets drawer for navigation
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.nav_drawer_open, R.string.nav_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        if (savedInstanceState == null) {
            getSupportActionBar().setTitle("Uncompleted Orders");
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new UncompOrdersFragment()).addToBackStack(null).commit(); // starts uncomporders fragment
            navigationView.setCheckedItem(R.id.nav_uncompOrders);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.nav_uncompOrders:
                getSupportActionBar().setTitle("Uncompleted Orders");
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new UncompOrdersFragment()).addToBackStack(null).commit(); // starts uncomporders fragment
                break;
            case R.id.nav_compOrders:
                if (admin == true) {
                    getSupportActionBar().setTitle("Completed Orders");
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                            new CompOrdersFragment()).addToBackStack(null).commit(); // starts comporders fragment
                } else { // verifies access level
                    Toast.makeText(this, "This page is only accessible by administrators.\nTalk to your manager or section leader for more information.", Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.nav_account:
                getSupportActionBar().setTitle("Account");
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new AccountFragment()).addToBackStack(null).commit(); // starts account fragment
                break;
            case R.id.nav_logOut:
                this.finish(); // ends activity when user logs out
                break;
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() { // stores previous fragments for navigation
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if(getFragmentManager().getBackStackEntryCount() == 0) {
            super.onBackPressed();
        }
        else {
            getFragmentManager().popBackStack();
        }
    }

    public void setTitle(String title) {
        getSupportActionBar().setTitle(title); // sets current title for toolbar
    }

    public void viewUncompOrder() {
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                new ViewUncompOrdersFragment()).addToBackStack(null).commit(); // starts uncomporder fragment
    }

    public void viewCompOrder() {
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                new ViewCompOrdersFragment()).addToBackStack(null).commit(); // starts comporder fragment
    }

    public void refreshViewUncompOrder() { // refreshes uncomporder fragment after completed order
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ViewUncompOrdersFragment()).commit();
    }

    public void updateAccount (View view) { // updates username
        if ((((EditText) findViewById(R.id.et_password)).getText().toString().isEmpty()) || (((EditText) findViewById(R.id.et_password)).getText().toString() == "") ||
                (((EditText) findViewById(R.id.et_password)).getText().toString().isEmpty()) || (((EditText) findViewById(R.id.et_password)).getText().toString() == "")) {
            Toast toast = Toast.makeText(this, "Enter a valid username or password", Toast.LENGTH_LONG);
            toast.show();
        } else {
            String password = createHash(); // creates hash of password confirmation
            String newUsername = ((EditText) findViewById(R.id.et_username)).getText().toString(); // stores new username
            backgroundWorker.execute("updateUsername", username, newUsername, password); // executes background worker
        }
    }

    public void createAccount (View view) { // starts new account fragment
        if (admin == true) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new NewAccountFragment()).addToBackStack(null).commit();
        } else { // verifies access level
            Toast.makeText(this, "This page is only accessible by administrators.\nTalk to your manager or section leader for more information.", Toast.LENGTH_LONG).show();
        }
    }

    public void createNewAccount (View view) { // creates new account
        String newEmployeeID = ((EditText) (findViewById(R.id.et_employeeID))).getText().toString(); // stores new employeeid
        String newFullName = ((EditText) (findViewById(R.id.et_fullname))).getText().toString(); // stores new full name
        String newUsername = ((EditText) (findViewById(R.id.et_username))).getText().toString(); // stores new username
        String newAdmin; // stores new admin
        if (((Switch) findViewById(R.id.switch_admin)).isChecked()) {
            newAdmin = "1";
        } else {
            newAdmin = "0";
        }
        String newPassword = createHash();

        backgroundWorker.execute("empNewAccount", newUsername, newPassword, newEmployeeID, newFullName, newAdmin); // executes background worker
    }

    public String createHash() { // converts password text to a secure MD5 hash code
        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            digest.update((((EditText) (findViewById(R.id.et_password))).getText().toString()).getBytes());
            byte messageDigest[] = digest.digest();

            StringBuffer hexString = new StringBuffer();
            for (int i = 0; i < messageDigest.length; i++) {
                hexString.append(Integer.toHexString(0xFF & messageDigest[i]));
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }
}
