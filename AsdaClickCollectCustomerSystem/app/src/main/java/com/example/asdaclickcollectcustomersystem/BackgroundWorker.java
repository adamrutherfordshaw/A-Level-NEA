package com.example.asdaclickcollectcustomersystem;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class BackgroundWorker extends AsyncTask<String, Void, String> {
    Context context;
    AlertDialog alertDialog;
    public String username = "";
    public String userid = "";
    String type = "";
    BackgroundWorker(Context ctx) {
        context = ctx;
    }

    @Override
    protected String doInBackground(String... voids) {
        type = voids[0];
        String password;
        String line;
        String custLogin = "http://10.0.2.2/custLogin.php";
        String custRegister = "http://10.0.2.2/custRegister.php";

        if (type.equals("custLogin")) { // queries database for user details, uses custlogin.php
            try {
                username = voids[1];
                password = voids[2];
                if (!username.isEmpty() && username != "" && !password.isEmpty() && password != "") { // logs customer in, uses custlogin.php
                    URL url = new URL(custLogin);
                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                    httpURLConnection.setRequestMethod("POST");
                    httpURLConnection.setDoOutput(true);
                    httpURLConnection.setDoInput(true);
                    OutputStream outputStream = httpURLConnection.getOutputStream();
                    BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                    String post_data = URLEncoder.encode("username", "UTF-8") + "=" + URLEncoder.encode(username, "UTF-8") + "&"
                            + URLEncoder.encode("password", "UTF-8") + "=" + URLEncoder.encode(password, "UTF-8");
                    bufferedWriter.write(post_data);
                    bufferedWriter.flush();
                    bufferedWriter.close();
                    InputStream inputStream = httpURLConnection.getInputStream();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));
                    String result = "";
                    while ((line = bufferedReader.readLine()) != null) {
                        result += line; // reads in echo message
                    }
                    bufferedReader.close();
                    inputStream.close();
                    httpURLConnection.disconnect();
                    String[] parts = result.split("\\|"); // stores userid and returns result
                    userid = parts[1];
                    result = parts[0];
                    return result;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (type.equals("custRegister")) { // creates new customer account, uses custregister.php
            try {
                String fullname = voids[1];
                String email = voids[2];
                String dateofbirth = voids[3];
                username = voids[4];
                password = voids[5];
                String passwordconfirmation = voids[6];

                if (password.equals(passwordconfirmation)) {
                    URL url = new URL(custRegister);
                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                    httpURLConnection.setRequestMethod("POST");
                    httpURLConnection.setDoOutput(true);
                    httpURLConnection.setDoInput(true);
                    OutputStream outputStream = httpURLConnection.getOutputStream();
                    BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                    String post_data = URLEncoder.encode("username", "UTF-8") + "=" + URLEncoder.encode(username, "UTF-8") + "&"
                            + URLEncoder.encode("password", "UTF-8") + "=" + URLEncoder.encode(password, "UTF-8") + "&"
                            + URLEncoder.encode("email", "UTF-8") + "=" + URLEncoder.encode(email, "UTF-8") + "&"
                            + URLEncoder.encode("fullname", "UTF-8") + "=" + URLEncoder.encode(fullname, "UTF-8") + "&"
                            + URLEncoder.encode("dateofbirth", "UTF-8") + "=" + URLEncoder.encode(dateofbirth, "UTF-8");
                    bufferedWriter.write(post_data);
                    bufferedWriter.flush();
                    bufferedWriter.close();
                    InputStream inputStream = httpURLConnection.getInputStream();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));
                    String result = "";
                    while ((line = bufferedReader.readLine()) != null) {
                        try {
                            result += line; // reads in echo message
                        } catch (Exception e) {
                            Log.e("Error 2", e.toString());
                        }
                    }
                    bufferedReader.close();
                    inputStream.close();
                    httpURLConnection.disconnect();
                    String[] parts = result.split("\\|"); // stores userid and returns result
                    result = parts[0];
                    return result;
                } else {
                    alertDialog = new AlertDialog.Builder(context).create();
                    alertDialog.setTitle("The passwords do not match.\nPlease try again.");
                    alertDialog.show();
                }
            } catch (Exception e) { // catches any errors
                Log.e("Error", e.toString()); // logs error
                e.printStackTrace();
            }
        }

        return "";
    }

    @Override
    protected void onPreExecute() {
        alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setTitle("Connection Status");
    }

    @Override
    protected void onPostExecute(String result) { // determines whether the query was successful
        if (result.contains("Login not successful")) {
            alertDialog.setMessage("Incorrect credentials");
            alertDialog.show();
        } else if (result.contains("Register not successful")) {
            alertDialog.setMessage("Register not successful" );
            alertDialog.show();
        } else if ((result == "") || result.isEmpty()) {
            alertDialog.setMessage("Something went wrong. Please try again" );
            alertDialog.show();
        } else {
            StartHome();
        }
    }

    public void StartHome() { // starts new homepage activity
        Intent intent = new Intent(context, HomePage.class);
        Bundle extras = new Bundle(); // adds bundle to store username and userid
        extras.putString("username", username);
        extras.putString("userid", userid);
        intent.putExtras(extras);
        context.startActivity(intent);
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }
}