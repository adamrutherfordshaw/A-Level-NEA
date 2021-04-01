package com.example.asdaclickcollectemployeesystem;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

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
    String type = "";

    BackgroundWorker(Context ctx) {
        context = ctx;
    }

    @Override
    protected String doInBackground(String... voids) {
        type = voids[0];
        String password;
        String line;
        String empLoginURL = "http://10.0.2.2/empLogin.php";
        String empRegisterURL = "http://10.0.2.2/empRegister.php";
        String updateNameURL = "http://10.0.2.2/updateName.php";
        if (type.equals("empLogin")) { // employee log in, uses emplogin.php
            try {
                username = voids[1];
                password = voids[2];
                URL url = new URL(empLoginURL);
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
                    result += line;
                }
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                return result;
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (type.equals("empNewAccount")) { // employee register, uses empregister.php
            try {
                username = voids[1];
                password = voids[2];
                String employeeid = voids[3];
                String fullname = voids[4];
                String admin = voids[5];
                URL url = new URL(empRegisterURL);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String post_data = URLEncoder.encode("username", "UTF-8") + "=" + URLEncoder.encode(username, "UTF-8") + "&"
                        + URLEncoder.encode("password", "UTF-8") + "=" + URLEncoder.encode(password, "UTF-8") + "&"
                        + URLEncoder.encode("employeeid", "UTF-8") + "=" + URLEncoder.encode(employeeid, "UTF-8") + "&"
                        + URLEncoder.encode("fullname", "UTF-8") + "=" + URLEncoder.encode(fullname, "UTF-8") + "&"
                        + URLEncoder.encode("admin", "UTF-8") + "=" + URLEncoder.encode(admin, "UTF-8");
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
                        Log.e("Error", e.toString());
                    }
                }
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                return result;
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (type.equals("updateUsername")) { // update username, uses updatename.php
            try {
                username = voids[1];
                String newusername = voids[2];
                password = voids[3];
                URL url = new URL(updateNameURL);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String post_data = URLEncoder.encode("username", "UTF-8") + "=" + URLEncoder.encode(username, "UTF-8") + "&"
                        + URLEncoder.encode("newusername", "UTF-8") + "=" + URLEncoder.encode(newusername, "UTF-8") + "&"
                        + URLEncoder.encode("password", "UTF-8") + "=" + URLEncoder.encode(password, "UTF-8");
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
                        Log.e("Error", e.toString());
                    }
                }
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                return result;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    protected void onPreExecute() {
        alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setTitle("Connection Status");
    }

    @Override
    protected void onPostExecute(String result) { // determines whether querying was successful
        if (type.equals("empLogin")) {
            if (result.contains("Login not successful")) { // responds with appropriate error message
                alertDialog.setMessage("Incorrect credentials");
                alertDialog.show();
            } else {
                StartHome(result);
            }
        } else if (type.equals("empNewAccount")){
            if (result.contains("Register not successful")) { // responds with appropriate error message
                alertDialog.setMessage("Register not successful" );
                alertDialog.show();
            } else if ((result == "") || result.isEmpty()) { // responds with appropriate error message
                alertDialog.setMessage("Something went wrong. Please try again");
                alertDialog.show();
            }
            else {
                Toast toast = Toast.makeText(context, "Account Created Successfully", Toast.LENGTH_LONG);
                toast.show();
            }
        } else {
            if (result.contains("An error occurred") || (result == "") || result.isEmpty()) { // responds with appropriate error message
                alertDialog.setMessage("Something went wrong. Please try again");
                alertDialog.show();
            }
            else {
                Toast toast = Toast.makeText(context, "Account Updated Successfully", Toast.LENGTH_LONG);
                toast.show();
            }
        }
    }

    public void StartHome(String result) {
        String admin = result.split("\\|")[1];
        Intent intent = new Intent(context, HomePage.class); // adds bundle to store username and admin access
        intent.putExtra("username", username);
        intent.putExtra("admin", admin);
        context.startActivity(intent);
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }
}