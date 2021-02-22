package com.example.mybookkeeping.frag_chart;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mybookkeeping.R;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class inComChartFragment extends BaseComChartFragment {
    public int kind = 1;

    @Override
    public void onResume() {
        super.onResume();
        loadData(year,month,kind);
    }

    @Override
    public void setDate(int year, int month) {
        super.setDate(year, month);
        loadData(year,month,kind);
    }
}
