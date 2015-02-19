package com.mingle.ui.fragment;


import android.support.v4.app.Fragment;

import com.mingle.widget.CarryFragmentLayout;


/**
 */
public class CarryFragment extends Fragment {


    public CarryFragment() {
    }


    private CarryFragmentLayout carryFragmentLayout;


    public CarryFragmentLayout getCarryFragmentLayout() {
        return carryFragmentLayout;
    }

    public void setCarryFragmentLayout(CarryFragmentLayout carryFragmentLayout) {
        this.carryFragmentLayout = carryFragmentLayout;
    }


    public void close(){
        carryFragmentLayout.closeFragment();
    }








}
