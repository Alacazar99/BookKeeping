package com.example.mybookkeeping.frag_chart;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.mybookkeeping.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class outComChartFragment extends BaseComChartFragment {
    int kind = 0;
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
