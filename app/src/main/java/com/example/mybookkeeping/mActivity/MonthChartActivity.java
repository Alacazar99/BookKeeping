package com.example.mybookkeeping.mActivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mybookkeeping.R;
import com.example.mybookkeeping.db.AccountBean;
import com.example.mybookkeeping.db.DBManager;
import com.example.mybookkeeping.frag_chart.inComChartFragment;
import com.example.mybookkeeping.frag_chart.BaseComChartFragment;
import com.example.mybookkeeping.frag_chart.outComChartFragment;
import com.example.mybookkeeping.mAdapter.AccountAdapter;
import com.example.mybookkeeping.mAdapter.ChartVPAdapter;
import com.example.mybookkeeping.myDialog.CalendarDialog;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static com.example.mybookkeeping.frag_chart.inComChartFragment.*;

public class MonthChartActivity extends AppCompatActivity implements View.OnClickListener {
    ImageView chartBack,chartRili;
    TextView dataTv,outTv,inTv;
    ViewPager chartVp;
    Button inBtn,outBtn;
    private int year,month;

    List<AccountBean> mDatas;
    AccountAdapter adapter;
    int dialogSelyear = -1;
    int dialogSelmonth = -1;
    inComChartFragment inComChartFragment;
    outComChartFragment outComChartFragment;

    List<Fragment> chartFragList;
    private ChartVPAdapter chartVPAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_month_chart);
        // 初始化数据源；
        mDatas = new ArrayList<>();

        initView();
        initTime();
        initStatistics(year,month);
        initFrag();

        // 滑动事件的监听；
        setVPSelectListener();
    }

    private void setVPSelectListener() {
        chartVp.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener(){
            @Override
            public void onPageSelected(int position) {
                setButtonStyle(position);
            }
        });
    }

    private void initFrag() {
        chartFragList = new ArrayList<>();
        // 添加fragment 的对象；
         inComChartFragment = new inComChartFragment();
         outComChartFragment = new outComChartFragment();
        // 传入年月； 添加数据到fragment中去；
        Bundle bundle = new Bundle();
        bundle.putInt("year",year);
        bundle.putInt("month",month);
        inComChartFragment.setArguments(bundle);
        outComChartFragment.setArguments(bundle);
        // 将Fragment添加到数据源当中去；
        chartFragList.add(outComChartFragment);
        chartFragList.add(inComChartFragment);
        // 设置适配器；
        chartVPAdapter = new ChartVPAdapter(getSupportFragmentManager(),chartFragList);
        chartVp.setAdapter(chartVPAdapter);
        // 将Fragmen 加载到Activity中；



    }


    // 初始化统计数据；
    private void initStatistics(int year,int month) {
        float inMoneyOneMonth = DBManager.getSumMoneyOneMonth(year, month, 1);// 月收入总钱数；
        float outMoneyOneMonth = DBManager.getSumMoneyOneMonth(year, month, 0);// 月支出总钱数；
        int incountItemOneMonth = DBManager.getCountItemOneMonth(year, month, 1);// 月收入多少笔；
        int outcountItemOneMonth = DBManager.getCountItemOneMonth(year, month, 0);// 月支出多少笔；
        dataTv.setText(year+"年"+month+"月账单");
        outTv.setText("共支出"+ outcountItemOneMonth + "笔, 总计消费：￥" + outMoneyOneMonth);
        inTv.setText("共收入"+ incountItemOneMonth + "笔, 总计收入：￥" + inMoneyOneMonth);
    }

    // 初始化时间；
    private void initTime() {
        Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH)+1;
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
                showCalendarDialog();
                break;
            case R.id.chart_btn_in:
                setButtonStyle(1);
                chartVp.setCurrentItem(1);
                break;
            case R.id.chart_btn_out:
                setButtonStyle(0);
                chartVp.setCurrentItem(0);
                break;
        }
    }

    private void showCalendarDialog() {
        CalendarDialog calendarDialog = new CalendarDialog(this, dialogSelyear, dialogSelmonth);
        calendarDialog.show();
        calendarDialog.setDialogSize();
        // 设置接口回调；
        calendarDialog.setOnRefreshListener(new CalendarDialog.OnRefreshListener() {
            @Override
            public void onRefresh(int seletedPos, int year, int month) {
                MonthChartActivity.this.dialogSelyear = seletedPos;
                MonthChartActivity.this.dialogSelmonth = 6;
                initStatistics(year,month);
                inComChartFragment.setDate(year,month);
                outComChartFragment.setDate(year,month);
            }
        });
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
