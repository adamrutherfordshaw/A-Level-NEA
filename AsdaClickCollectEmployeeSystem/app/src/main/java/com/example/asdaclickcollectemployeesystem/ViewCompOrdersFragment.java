package com.example.asdaclickcollectemployeesystem;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class ViewCompOrdersFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        HomePage homePage = (HomePage)getActivity();
        homePage.setTitle("Picked Order: " + homePage.currentorderid); // sets title for toolbar

        return inflater.inflate(R.layout.fragment_view_comp_orders, container, false);
    }
}
