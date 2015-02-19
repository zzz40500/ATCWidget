package com.mingle.widget;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mingle.ui.fragment.CarryFragment;


/**
 * A simple {@link Fragment} subclass.
 */
public class BlankFragment extends CarryFragment {



    private String name;

    public BlankFragment(String name) {
        // Required empty public constructor
        this.name=name;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view
        =inflater.inflate(R.layout.fragment_blank, container, false);


        TextView textView= (TextView) view.findViewById(R.id.textView);

        textView.setText(name);




        return  view;
    }


}
