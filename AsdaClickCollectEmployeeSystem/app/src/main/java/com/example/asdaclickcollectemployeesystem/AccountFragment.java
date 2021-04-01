package com.example.asdaclickcollectemployeesystem;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class AccountFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        HomePage homePage = (HomePage)getActivity();
        homePage.setTitle("Account Settings"); // sets title for toolbar

        return inflater.inflate(R.layout.fragment_account, container, false);
    }
}
