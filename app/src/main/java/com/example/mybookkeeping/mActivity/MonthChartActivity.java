package com.example.mybookkeeping.mActivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mybookkeeping.R;

import java.util.Calendar;

public class MonthChartActivity extends AppCompatActivity implements View.OnClickListener {
    ImageView chartBack,chartRili;
    TextView dataTv,outTv,inTv;
    ViewPager chartVp;
    Button inBtn,outBtn;
    private int year,month;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_month_chart);
        initView();
        initTime();
        initStatistics(year,month);
        initDatas();
    }

    // 初始化统计数据；
    private void initStatistics(int year,int month) {

    }

    // 初始化时间；
    private void initTime() {
        Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH)+1;
    }

    private void initDatas() {

    }

    private void initView() {
        dataTv = findViewById(R.id.chart_tv_date);
        outTv = findViewById(R.id.chart_tv_out);
        inTv = findViewById(R.id.chart_tv_in);
        chartVp = findViewById(R.id.chart_vp);
        inBtn = findViewById(R.id.chart_btn_in);
        outBtn = findViewById(R.id.chart_btn_out);

    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.chart_iv_back:
                finish();
                break;
            case R.id.chart_iv_rili:
                break;
            case R.id.chart_btn_in:
                setButtonStyle(1);
                break;
            case R.id.chart_btn_out:
                setButtonStyle(0);
                break;
        }
    }

    private void setButtonStyle(int kind) {
        if (kind == 0) {
            outBtn.setBackgroundResource(R.drawable.chart_btn_in_bg);
            outBtn.setTextColor(Color.WHITE);
            inBtn.setBackgroundResource(R.drawable.chart_btn_out_bg);
            inBtn.setTextColor(Color.BLACK);
        }else if (kind == 1){
            inBtn.setBackgroundResource(R.drawable.chart_btn_in_bg);
            inBtn.setTextColor(Color.WHITE);
            outBtn.setBackgroundResource(R.drawable.chart_btn_out_bg);
            outBtn.setTextColor(Color.BLACK);
        }
    }
}
