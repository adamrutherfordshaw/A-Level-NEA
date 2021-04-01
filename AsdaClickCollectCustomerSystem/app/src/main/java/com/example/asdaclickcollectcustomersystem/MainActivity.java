package com.example.asdaclickcollectcustomersystem;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportFragmentManager().beginTransaction().replace(R.id.mainfragment_container,
                new LoginFragment()).commit(); // sets default layout to the login fragment
    }

    public void custLoginLayout (View view) {
        getSupportFragmentManager().beginTransaction().replace(R.id.mainfragment_container,
                new LoginFragment()).commit(); // procedure to set layout to the login fragment
    }

    public void custRegister(View view) {
        getSupportFragmentManager().beginTransaction().replace(R.id.mainfragment_container,
                new RegisterFragment()).commit(); // procedure to set layout to the register fragment
    }

    public void createAccount(View view) {

        String fullname = ((EditText) (findViewById(R.id.et_fullname))).getText().toString(); // stores full name from register fragment
        String email = ((EditText) (findViewById(R.id.et_email))).getText().toString(); // stores email from register fragment
        String username = ((EditText) (findViewById(R.id.et_username))).getText().toString(); // stores username from register fragment
        String dateofbirth = String.valueOf(((EditText) findViewById(R.id.et_dateofbirth)).getText()); // stores date of birth from register fragment

        try { // sets layout of date of birth so it is compatible for the database
            dateofbirth = dateofbirth.split("/")[2] + "-" + dateofbirth.split("/")[0] + "-" + dateofbirth.split("/")[1];
        } catch (Exception e) {
            dateofbirth = "";
        }

        String password = createHash((EditText) findViewById(R.id.et_password)); // creates hash for password
        String passwordConfirmation = createHash((EditText) findViewById(R.id.et_passwordconfirmation)); // creates hash for password confirmation

        TextView pw = findViewById(R.id.et_password);
        pw.setText(""); // sets password text back to null
        TextView pwc = findViewById(R.id.et_passwordconfirmation);
        pwc.setText(""); // sets password confirmation text back to null

        BackgroundWorker backgroundWorker = new BackgroundWorker(this);
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(this.CONNECTIVITY_SERVICE);
        if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_ETHERNET).getState() == NetworkInfo.State.CONNECTED) {
            backgroundWorker.execute("custRegister", fullname, email, dateofbirth, username, password, passwordConfirmation); // checks integrity of network connection before executing
        } else {
            Toast.makeText(this, "Connection failed. Please check connection", Toast.LENGTH_SHORT);
        }

        fullname = null; // sets data to null
        email = null; // sets data to null
        dateofbirth = null; // sets data to null
        System.gc(); // attempts to clear unused variables
    }

    public void custLogin(View view) {
        String username = ((EditText) (findViewById(R.id.et_username))).getText().toString(); // stores username from login fragment
        String password = createHash((EditText) findViewById(R.id.et_password)); // creates hash for password

        TextView tv = findViewById(R.id.et_password);
        tv.setText(""); // sets password text back to null

        BackgroundWorker backgroundWorker = new BackgroundWorker(this);

        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(this.CONNECTIVITY_SERVICE);
        if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_ETHERNET).getState() == NetworkInfo.State.CONNECTED){
            backgroundWorker.execute("custLogin", username, password); // checks integrity of network connection before executing
        } else {
            Toast.makeText(this, "Connection failed. Please check connection", Toast.LENGTH_SHORT);
        }
    }

    public String createHash(EditText password) { // converts input text to a secure MD5 hash code
        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            digest.update(password.getText().toString().getBytes());
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
