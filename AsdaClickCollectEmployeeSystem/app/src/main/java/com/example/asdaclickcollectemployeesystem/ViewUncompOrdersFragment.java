package com.example.asdaclickcollectemployeesystem;

import android.app.AlertDialog;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.util.Pair;
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
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Stack;

import static android.widget.LinearLayout.HORIZONTAL;
import static android.widget.LinearLayout.VERTICAL;

public class ViewUncompOrdersFragment extends Fragment {

    public Stack<Integer> fastestPath = new Stack<>();
    List<Integer> requiredV = new ArrayList<>();
    Stack<Integer> tempFastestPath;
    AlertDialog alertDialog;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        final HomePage homePage = (HomePage)getActivity();
        homePage.setTitle("Picking Order: " + homePage.currentorderid);

        View view = inflater.inflate(R.layout.fragment_view_uncomp_orders, container, false);
        LinearLayout layout = view.findViewById(R.id.vuo_fragment_container); // sets layout and layoutparams

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

        final String orderid = homePage.currentorderid;

        List<String[]> productList = new ArrayList<>();

        try {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            String retrieveOrderProductsURL = "http://10.0.2.2/retrieveOrderProducts.php";
            String updateOrderURL = "http://10.0.2.2/updateOrder.php";
            final String pickItemURL = "http://10.0.2.2/pickItem.php";
            URL url = new URL(retrieveOrderProductsURL);
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
            while ((line = bufferedReader.readLine()) != null) { // uses layouts and cardview to present each item
                if (line.length() < 2) {
                    break;
                }

                String[] parts = line.split("\\|");
                String[] tempArray = {(parts[0]), (parts[1]), (parts[2]), (parts[3]), (parts[4])};
                productList.add(tempArray); // stores information about each product
            }

            requiredV.add(0); // adds a 0 'start' node
            requiredV.add(35); // adds a 35 'end' node

            for (String[] s : productList) {
                if (!requiredV.contains(Integer.parseInt(s[3]))) {
                    requiredV.add(Integer.parseInt(s[3])); // adds each node in productlist
                }
            }

            createSmallGraph(homePage.graph); // creates smaller graph of required products
            List<String[]> orderedList = new ArrayList<>(); // stores the fastest order of traversal

            if (productList.size() > 1) {
                while (!tempFastestPath.isEmpty()) {
                    int currentAisle = tempFastestPath.pop(); // stores current aisle to visit

                    for (String[] s : productList) {
                        if (Integer.parseInt(s[3]) == currentAisle) {
                            orderedList.add(s); // adds current aisle to fastest path
                        }
                    }
                }
            }
            else if (productList.size() == 1) {
                orderedList.add(productList.get(0));
            } else {
                alertDialog = new AlertDialog.Builder(getContext()).create();
                alertDialog.setMessage("There are no more products in this order");
                alertDialog.show();

                url = new URL(updateOrderURL);
                httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                outputStream = httpURLConnection.getOutputStream();
                bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                post_data = URLEncoder.encode("orderid", "UTF-8") + "=" + URLEncoder.encode(orderid, "UTF-8") + "&"
                        + URLEncoder.encode("username", "UTF-8") + "=" + URLEncoder.encode(homePage.username, "UTF-8");
                bufferedWriter.write(post_data);
                bufferedWriter.flush();
                bufferedWriter.close();
                inputStream = httpURLConnection.getInputStream();
                bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));
                if (bufferedReader.readLine() != null) {
                    Toast toast = Toast.makeText(getContext(), bufferedReader.readLine(), Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
            Collections.reverse(orderedList);

            for (String[] s : orderedList) {
                CardView cv = new CardView(getContext());
                LinearLayout layout1 = new LinearLayout(getContext());
                LinearLayout layout2 = new LinearLayout(getContext());
                LinearLayout layout3 = new LinearLayout(getContext());
                layout1.setOrientation(HORIZONTAL);
                layout2.setOrientation(VERTICAL);
                layout3.setOrientation(HORIZONTAL);
                final String upc = s[1];
                TextView orderID = new TextView(getContext());
                TextView orderDate = new TextView(getContext());
                TextView itemQuantity = new TextView(getContext());
                TextView itemAisle = new TextView(getContext());
                TextView itemLocation = new TextView(getContext());
                try { // sets layout for each item in order
                    orderID.setText("Name: " + s[0]);
                    orderDate.setText("UPC: " + s[1]);
                    itemQuantity.setText("Quantity: " + s[2]);
                    itemAisle.setText("Aisle: " + s[3]);
                    itemLocation.setText("Location: " + s[4]);
                    Pair<String, Integer> thisItem = new Pair<>(s[1], Integer.parseInt(s[3]));
                    Button pickItem = new Button(getContext());
                    pickItem.setText("Pick Item");
                    pickItem.setOnClickListener(new Button.OnClickListener() {
                        public void onClick(View v) {
                            try {
                                URL url = new URL(pickItemURL);
                                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                                httpURLConnection.setRequestMethod("POST");
                                httpURLConnection.setDoOutput(true);
                                httpURLConnection.setDoInput(true);
                                OutputStream outputStream = httpURLConnection.getOutputStream();
                                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                                String post_data = URLEncoder.encode("orderid", "UTF-8") + "=" + URLEncoder.encode(orderid, "UTF-8") + "&"
                                        + URLEncoder.encode("upc", "UTF-8") + "=" + URLEncoder.encode(upc, "UTF-8");
                                bufferedWriter.write(post_data);
                                bufferedWriter.flush();
                                bufferedWriter.close();
                                InputStream inputStream = httpURLConnection.getInputStream();
                                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));
                                if (bufferedReader.readLine() != null) {
                                    Toast toast = Toast.makeText(getContext(), bufferedReader.readLine(), Toast.LENGTH_SHORT);
                                    toast.show();
                                } else {
                                    homePage.refreshViewUncompOrder();
                                }
                            } catch (Exception e) {
                                AlertDialog alertDialog = new AlertDialog.Builder(getContext()).create();
                                alertDialog.setMessage(e.getMessage());
                                alertDialog.show();
                                e.printStackTrace();
                            }
                        }
                    });

                    layout1.setLayoutParams(layout1Params);
                    layout2.setLayoutParams(layout2Params);
                    layout3.setLayoutParams(layout3Params);
                    layout3.setGravity(Gravity.RIGHT);
                    pickItem.setLayoutParams(btnParams);
                    pickItem.setGravity(Gravity.RIGHT);
                    orderID.setLayoutParams(tvParams);
                    orderDate.setLayoutParams(tvParams);
                    itemQuantity.setLayoutParams(tvParams);
                    itemAisle.setLayoutParams(tvParams);
                    itemLocation.setLayoutParams(tvParams);

                    layout3.addView(pickItem);
                    layout2.addView(orderID);
                    layout2.addView(orderDate);
                    layout2.addView(itemQuantity);
                    layout2.addView(itemAisle);
                    layout2.addView(itemLocation);
                    layout1.addView(layout2);
                    layout1.addView(layout3);
                    cv.addView(layout1);
                    cv.setElevation(15);
                    cv.setRadius(15);
                    cv.setLayoutParams(cvParams);
                    layout.addView(cv);
                } catch (Exception e) {
                    AlertDialog alertDialog = new AlertDialog.Builder(getContext()).create();
                    alertDialog.setMessage("Some products were not located on the system");
                    alertDialog.show();
                    e.printStackTrace();
                    layout.removeView(cv);
                }
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

        return view;
    }

    public void createSmallGraph(WeightedGraph.Graph graph) { // creates smaller subgraph of required nodes

        WeightedGraph.Graph newGraph = new WeightedGraph.Graph();
        int[][] fastPathArray;
        int a = 0;

        for (int x : requiredV) { // iterates through each required vertex and adds path
            fastPathArray = graph.fastPath(x);
            for (int i = 1; i <= 35; i++) {
                if (requiredV.contains(fastPathArray[i][0])) {
                    newGraph.addEdge(fastPathArray[i][1], fastPathArray[0][0], fastPathArray[i][0], false);
                    a++;
                }
            }
        }

        requiredV.remove(Integer.valueOf(35)); // removes 35 'end' node
        tempFastestPath = newGraph.calculateFastestPathAroundStore(requiredV); // calculates fastest path for each required vertex
    }
}
