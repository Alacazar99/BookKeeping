package com.example.mybookkeeping.frag_chart;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.mybookkeeping.R;
import com.example.mybookkeeping.db.ChartItemBean;
import com.example.mybookkeeping.db.DBManager;
import com.example.mybookkeeping.mAdapter.ChartItemAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class BaseComChartFragment extends Fragment {
    ListView chartLv;
    int year,month;
    List<ChartItemBean> mDatas; // 数据源；
    private ChartItemAdapter chartItemAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_out_com_chart, container, false);
        chartLv = view.findViewById(R.id.chart_fragment_lv);
        Bundle bundle = getArguments();
        year = bundle.getInt("year");
        month = bundle.getInt("month");
        // 设置数据源；
        mDatas = new ArrayList<>();
        // 设置适配器；
        chartItemAdapter = new ChartItemAdapter(getContext(), mDatas);
        chartLv.setAdapter(chartItemAdapter);
        return view;
    }

    public void setDate(int year,int month){
        this.year = year;
        this.month = month;
    }

    @Override
    public void onResume() {
        super.onResume();
        loadData(year,month,0);
    }

    public void loadData(int year, int month, int kind) {
        List<ChartItemBean> list = DBManager.getChartListFromAccounttb(year, month, kind);
        mDatas.clear();
        mDatas.addAll(list);
        chartItemAdapter.notifyDataSetChanged();
    }
}
