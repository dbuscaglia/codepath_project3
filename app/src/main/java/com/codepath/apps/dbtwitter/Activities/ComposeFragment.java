package com.codepath.apps.dbtwitter.Activities;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.codepath.apps.dbtwitter.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ComposeFragment extends Fragment {


    public ComposeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_compose, container, false);
    }


}
