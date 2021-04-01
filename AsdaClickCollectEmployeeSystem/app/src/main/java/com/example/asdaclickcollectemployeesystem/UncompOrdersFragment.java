package com.example.asdaclickcollectemployeesystem;

import android.app.AlertDialog;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import static android.widget.LinearLayout.HORIZONTAL;
import static android.widget.LinearLayout.VERTICAL;

public class  UncompOrdersFragment extends Fragment {

    int numOrders = 0;
    AlertDialog alertDialog;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        final HomePage homePage = (HomePage)getActivity();
        homePage.setTitle("Uncompleted Orders");

        View view = inflater.inflate(R.layout.fragment_uncomp_orders, container, false);
        LinearLayout layout = view.findViewById(R.id.fragment_container); // sets layout and layoutparams

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
        btnParams.setMargins(25, 5, 5, 10);
        LinearLayout.LayoutParams layout1Params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        LinearLayout.LayoutParams layout2Params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        layout2Params.setMargins(10, 10, 100, 10);
        LinearLayout.LayoutParams layout3Params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        String line = "";
        try {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            String retrieveUncompOrdersURL = "http://10.0.2.2/retrieveUncompOrders.php";
            URL url = new URL(retrieveUncompOrdersURL);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);
            InputStream inputStream = httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));
            while ((line = bufferedReader.readLine()) != null) { // sets layout and cardview for each order
                String[] parts = line.split("\\|");

                if (Integer.parseInt(parts[2]) == 0) {
                    break;
                }

                CardView cv = new CardView(getContext());
                LinearLayout layout1 = new LinearLayout(getContext());
                LinearLayout layout2 = new LinearLayout(getContext());
                LinearLayout layout3 = new LinearLayout(getContext());
                layout1.setOrientation(HORIZONTAL);
                layout2.setOrientation(VERTICAL);
                layout3.setOrientation(HORIZONTAL);

                final String orderid = parts[0];
                TextView orderID = new TextView(getContext());
                orderID.setText("Order ID: " + parts[0]);
                TextView orderDate = new TextView(getContext());
                orderDate.setText("Order Date: " + parts[1]);
                TextView numItems = new TextView(getContext());
                numItems.setText("Items Remaining: " + parts[2]);

                Button viewOrder = new Button(getContext());
                viewOrder.setText("View Order");
                viewOrder.setOnClickListener(new Button.OnClickListener() {
                    public void onClick(View v) {
                        homePage.currentorderid = orderid;
                        homePage.viewUncompOrder();
                    }
                });

                layout1.setLayoutParams(layout1Params);
                layout2.setLayoutParams(layout2Params);
                layout3.setLayoutParams(layout3Params);
                layout3.setGravity(Gravity.BOTTOM);
                viewOrder.setLayoutParams(btnParams);
                orderID.setLayoutParams(tvParams);
                orderDate.setLayoutParams(tvParams);
                numItems.setLayoutParams(tvParams);

                layout3.addView(viewOrder);
                layout2.addView(orderID);
                layout2.addView(orderDate);
                layout2.addView(numItems);
                layout1.addView(layout2);
                layout1.addView(layout3);
                cv.addView(layout1);
                cv.setElevation(15);
                cv.setRadius(15);
                cv.setLayoutParams(cvParams);
                layout.addView(cv);

                numOrders++;
            }
            bufferedReader.close();
            inputStream.close();
            httpURLConnection.disconnect();
        } catch (Exception e) {
            alertDialog = new AlertDialog.Builder(getContext()).create();
            alertDialog.setMessage(e.getMessage());
            alertDialog.show();
            e.printStackTrace();
        }

        if (numOrders == 0) {
            alertDialog = new AlertDialog.Builder(getContext()).create();
            alertDialog.setMessage("Nothing to see here.\nThere aren't any orders right now.");
            alertDialog.show();
        }

        homePage.graph = new WeightedGraph.Graph();
        createInitialGraph(homePage.graph);

        return view;
    }

    public void createInitialGraph(WeightedGraph.Graph graph) { // creates base graph of store
        for (int i = 1; i <= 14; i++) {
            graph.addEdge(2, i, 29 - i, true);
        }
        for (int i = 23; i <= 28; i++) {
            graph.addEdge(2, i, 57 - i, true);
        }
        for (int i = 1; i < 14; i++) {
            graph.addEdge(1, i, i + 1, true);
        }
        for (int i = 15; i < 28; i++) {
            graph.addEdge(1, i, i + 1, true);
        }
        for (int i = 29; i < 34; i++) {
            graph.addEdge(1, i, i + 1, true);
        }
        graph.addEdge(3, 0, 34, true);
        graph.addEdge(2, 0, 20, true);
        graph.addEdge(2, 35, 17, true);
    }
}
