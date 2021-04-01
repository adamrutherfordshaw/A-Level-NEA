package com.example.asdaclickcollectcustomersystem;

import android.app.AlertDialog;
import android.support.v4.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;

import static android.widget.LinearLayout.HORIZONTAL;
import static android.widget.LinearLayout.VERTICAL;

public class BasketFragment extends Fragment {

    @Override
    public Context getContext() {
        return super.getContext();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_basket, container, false);

        LinearLayout layout = view.findViewById(R.id.fragment_container); // establishes layouts and layoutparams for the basket fragment
        LinearLayout.LayoutParams cvParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        cvParams.setMargins(10, 10, 10, 10);
        LinearLayout.LayoutParams tvParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        tvParams.setMargins(5, 5, 5, 5);
        LinearLayout.LayoutParams btnParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        btnParams.setMargins(25, 5, 5, 5);
        LinearLayout.LayoutParams layout1Params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        LinearLayout.LayoutParams layout2Params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        layout2Params.setMargins(10, 10, 100, 10);
        LinearLayout.LayoutParams layout3Params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);

        String line = "";
        final HomePage homePage = (HomePage)getActivity();
        final String userid = homePage.getUserid();
        final String orderid = homePage.getOrderid();
        try {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            String retrieveBasketURL = "http://10.0.2.2/retrieveBasket.php";
            final String addOrderURL = "http://10.0.2.2/addOrder.php";
            final String deleteFromBasketURL = "http://10.0.2.2/deleteFromBasket.php";

            URL url = new URL(retrieveBasketURL); // queries database for relevant order, uses retrievebasket.php
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);
            OutputStream outputStream = httpURLConnection.getOutputStream();
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
            String post_data = URLEncoder.encode("orderid", "UTF-8") + "=" + URLEncoder.encode(orderid, "UTF-8");
            bufferedWriter.write(post_data);
            bufferedWriter.flush();
            bufferedWriter.close();
            InputStream inputStream = httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));
            String result = "";
            while ((line = bufferedReader.readLine()) != null) { // uses layouts and cardview to present each item
                if (line.length() < 2) {
                    break;
                }
                result += line;
                String[] parts = line.split("\\|");
                CardView cv = new CardView(getContext());
                LinearLayout layout1 = new LinearLayout(getContext());
                LinearLayout layout2 = new LinearLayout(getContext());
                LinearLayout layout3 = new LinearLayout(getContext());
                layout1.setOrientation(HORIZONTAL);
                layout2.setOrientation(VERTICAL);
                layout3.setOrientation(HORIZONTAL);
                TextView productName = new TextView(getContext());
                productName.setText(parts[1]);
                final String productid = parts[0];
                final String quantity = parts[2];

                Button deleteFromBasket = new Button(getContext());
                deleteFromBasket.setText("Delete from Basket");
                deleteFromBasket.setOnClickListener(new Button.OnClickListener() {
                    public void onClick(View v) { // removes product from basket, uses deletefrombasket.php
                        String result;
                        try {
                            URL url = new URL(deleteFromBasketURL); // deletes selected item
                            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                            httpURLConnection.setRequestMethod("POST");
                            httpURLConnection.setDoOutput(true);
                            httpURLConnection.setDoInput(true);
                            OutputStream outputStream = httpURLConnection.getOutputStream();
                            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                            String post_data = URLEncoder.encode("orderid", "UTF-8") + "=" + URLEncoder.encode(orderid, "UTF-8") + "&"
                                    + URLEncoder.encode("productid", "UTF-8") + "=" + URLEncoder.encode(productid, "UTF-8");
                            bufferedWriter.write(post_data);
                            bufferedWriter.flush();
                            bufferedWriter.close();
                            InputStream inputStream = httpURLConnection.getInputStream();
                            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));
                            result = "";
                            String line;
                            while ((line = bufferedReader.readLine()) != null) {
                                result += line;
                            }
                            bufferedReader.close();
                            inputStream.close();
                            httpURLConnection.disconnect();

                            homePage.refreshBasket(); // refreshes the basket fragment

                            if (result.contains("Error")) {
                                Toast toast = Toast.makeText(getContext(), result, Toast.LENGTH_SHORT);
                                toast.show();
                            }
                        } catch (MalformedURLException e) { // catches errors when querying the database
                            e.printStackTrace();
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        } catch (ProtocolException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
                layout1.setLayoutParams(layout1Params);
                layout2.setLayoutParams(layout2Params);
                layout3.setLayoutParams(layout3Params);
                layout3.setGravity(Gravity.RIGHT);
                deleteFromBasket.setLayoutParams(btnParams);
                productName.setLayoutParams(tvParams);

                layout3.addView(deleteFromBasket);
                layout2.addView(productName);
                layout1.addView(layout2);
                layout1.addView(layout3);
                cv.addView(layout1);
                cv.setElevation(15);
                cv.setRadius(15);
                cv.setLayoutParams(cvParams);
                layout.addView(cv);
            }
            if (result == "") { // output when basket is empty
                TextView tv = new TextView(getContext());
                tv.setTextSize(20);
                tv.setText("You haven't added anything to your basket yet. Add something and come back later.");
                tv.setGravity(Gravity.CENTER);
                tv.setLayoutParams(layout1Params);
                layout.addView(tv);
            } else { // output when basket contains items
                Button sendBtn = new Button(getContext()); // creates a new button to submit the order
                sendBtn.setText("Send Order");
                sendBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) { // submits order for picking, uses addorder.php
                        String result;
                        try {
                            URL url = new URL(addOrderURL); // checks for existing order, uses checkorder.php
                            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                            httpURLConnection.setRequestMethod("POST");
                            httpURLConnection.setDoOutput(true);
                            httpURLConnection.setDoInput(true);
                            OutputStream outputStream = httpURLConnection.getOutputStream();
                            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                            String post_data = URLEncoder.encode("orderid", "UTF-8") + "=" + URLEncoder.encode(orderid, "UTF-8");
                            bufferedWriter.write(post_data);
                            bufferedWriter.flush();
                            bufferedWriter.close();
                            InputStream inputStream = httpURLConnection.getInputStream();
                            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));
                            result = "";
                            String line;
                            while ((line = bufferedReader.readLine()) != null) {
                                result += line;
                            }
                            bufferedReader.close();
                            inputStream.close();
                            httpURLConnection.disconnect();

                            if (result.contains("Error")) {
                                Toast toast = Toast.makeText(getContext(), result, Toast.LENGTH_SHORT);
                                toast.show();
                            }

                            homePage.setOrderid("");
                            homePage.refreshBasket(); // refreshes the basketfragment

                        } catch (MalformedURLException e) {
                            e.printStackTrace();
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        } catch (ProtocolException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
                sendBtn.setLayoutParams(btnParams);
                sendBtn.setGravity(Gravity.RIGHT);
                layout.addView(sendBtn);
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
            }
        } catch (Exception e) {
            AlertDialog alertDialog = new AlertDialog.Builder(getContext()).create();
            alertDialog.setMessage(e.getMessage());
            alertDialog.show();
            e.printStackTrace();
        }
        return view;
    }
}
