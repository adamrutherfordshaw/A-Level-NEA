package com.example.asdaclickcollectcustomersystem;

import android.app.AlertDialog;
import android.support.v4.app.Fragment;
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

public class ProductsFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_products, container, false);

        LinearLayout layout = view.findViewById(R.id.fragment_container); // establishes layouts and layoutparams
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
        try {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            String retrieveProductsURL = "http://10.0.2.2/retrieveProducts.php";
            final String addToBasketURL = "http://10.0.2.2/addToBasket.php";
            final String checkOrderURL = "http://10.0.2.2/checkOrder.php";
            final String createOrderURL = "http://10.0.2.2/createOrder.php";
            URL url = new URL(retrieveProductsURL); // queries database for relevant products, uses retrieveproducts.php
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);
            InputStream inputStream = httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));
            while ((line = bufferedReader.readLine()) != null) { // uses layouts and cardview to present each item
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
                final String quantity = "1"; // sets default quantity to 1

                Button addToBasket = new Button(getContext());
                addToBasket.setText("Add to Basket");
                addToBasket.setOnClickListener(new Button.OnClickListener() {
                    public void onClick(View v) { // sends product to basket
                        HomePage homePage = (HomePage)getActivity();
                        String userid = homePage.getUserid();
                        String orderid = homePage.getOrderid();
                        String result = "";
                        if (orderid == "") {
                            try {
                                URL url = new URL(checkOrderURL); // checks for existing order, uses checkorder.php
                                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                                httpURLConnection.setRequestMethod("POST");
                                httpURLConnection.setDoOutput(true);
                                httpURLConnection.setDoInput(true);
                                OutputStream outputStream = httpURLConnection.getOutputStream();
                                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                                String post_data = URLEncoder.encode("userid", "UTF-8") + "=" + URLEncoder.encode(userid, "UTF-8");
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

                                if (result.contains("No orders found")) {
                                    try {
                                        url = new URL(createOrderURL); // creates new order in database, uses createorder.php
                                        httpURLConnection = (HttpURLConnection) url.openConnection();
                                        httpURLConnection.setRequestMethod("POST");
                                        httpURLConnection.setDoOutput(true);
                                        httpURLConnection.setDoInput(true);
                                        outputStream = httpURLConnection.getOutputStream();
                                        bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                                        post_data = URLEncoder.encode("userid", "UTF-8") + "=" + URLEncoder.encode(userid, "UTF-8");
                                        bufferedWriter.write(post_data);
                                        bufferedWriter.flush();
                                        bufferedWriter.close();
                                        inputStream = httpURLConnection.getInputStream();
                                        bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));
                                        result = "";
                                        while ((line = bufferedReader.readLine()) != null) {
                                            result += line;
                                        }
                                        bufferedReader.close();
                                        inputStream.close();
                                        httpURLConnection.disconnect();
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
                            } catch (MalformedURLException e) {
                                e.printStackTrace();
                            } catch (UnsupportedEncodingException e) {
                                e.printStackTrace();
                            } catch (ProtocolException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            homePage.setOrderid(result); // returns result to homepage
                            orderid = homePage.getOrderid();
                        }
                        try {
                            URL url = new URL(addToBasketURL); // adds item to basket, uses addtobasket.php
                            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                            httpURLConnection.setRequestMethod("POST");
                            httpURLConnection.setDoOutput(true);
                            httpURLConnection.setDoInput(true);
                            OutputStream outputStream = httpURLConnection.getOutputStream();
                            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                            String post_data = URLEncoder.encode("orderid", "UTF-8") + "=" + URLEncoder.encode(orderid, "UTF-8") + "&"
                                    + URLEncoder.encode("productid", "UTF-8") + "=" + URLEncoder.encode(productid, "UTF-8");
                            //+ URLEncoder.encode("quantity", "UTF-8") + "=" + URLEncoder.encode(quantity, "UTF-8");
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
                            if (result != "") {
                                Toast toast = Toast.makeText(getContext(), result, Toast.LENGTH_LONG);
                                toast.show();
                            }
                            bufferedReader.close();
                            inputStream.close();
                            httpURLConnection.disconnect();
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

                layout1.setLayoutParams(layout1Params);
                layout2.setLayoutParams(layout2Params);
                layout3.setLayoutParams(layout3Params);
                layout3.setGravity(Gravity.RIGHT);
                addToBasket.setLayoutParams(btnParams);
                productName.setLayoutParams(tvParams);

                layout3.addView(addToBasket);
                layout2.addView(productName);
                layout1.addView(layout2);
                layout1.addView(layout3);
                cv.addView(layout1);
                cv.setElevation(15);
                cv.setRadius(15);
                cv.setLayoutParams(cvParams);
                layout.addView(cv);
            }
            bufferedReader.close();
            inputStream.close();
            httpURLConnection.disconnect();
        } catch (Exception e) {
            AlertDialog alertDialog = new AlertDialog.Builder(getContext()).create();
            alertDialog.setMessage(e.getMessage());
            alertDialog.show();
            e.printStackTrace();
        }
        return view;
    }
}