package com.example.mybookkeeping.mAdapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.mybookkeeping.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 历史账单界面，点击日历表，弹出对话框，设置GridView 中的适配器；
 */
public class CalerdarAdapter extends BaseAdapter {
    Context context;
    List<String> mDatas;
    public int year;
    public int selectPos = -1;

    public CalerdarAdapter(Context context, int year) {
        this.context = context;
        this.year = year;
        mDatas = new ArrayList<>();
        loadData(year);
    }

    private void loadData(int year) {
        for (int i = 1; i < 13 ; i++){
            String data = year + "/" + i;
            mDatas.add(data);
        }
    }

    public void setYear(int setYearNum) {
        this.year = setYearNum;
        mDatas.clear();
        loadData(year);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mDatas.size();
    }

    @Override
    public Object getItem(int position) {
        return mDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from(context).inflate(R.layout.item_dialog_gv,null);
        TextView gvTv = convertView.findViewById(R.id.item_dialogcal_gv_tv);
        gvTv.setText(mDatas.get(position));
        gvTv.setBackgroundResource(R.color.grey_bg);
        gvTv.setTextColor(Color.BLACK);

        if (position == selectPos) {
            gvTv.setBackgroundResource(R.color.green_006400);
            gvTv.setTextColor(Color.WHITE);
        }
        return convertView;
    }


}
