package com.example.asdaclickcollectemployeesystem;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
    }

    public void empLogin(View view) throws NoSuchAlgorithmException {
        String username = ((EditText) (findViewById(R.id.et_username))).getText().toString(); // gets username
        String password = createHash(); // creates hash of password

        TextView tv = findViewById(R.id.et_password);
        tv.setText(""); // sets password text back to null

        BackgroundWorker backgroundWorker = new BackgroundWorker(this);

        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(this.CONNECTIVITY_SERVICE);
        if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_ETHERNET).getState() == NetworkInfo.State.CONNECTED){
            backgroundWorker.execute("empLogin", username, password); // checks integrity of network connection before executing
        } else {
            Toast.makeText(this, "Connection failed. Please check connection", Toast.LENGTH_SHORT);
        }
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
