package com.example.mybookkeeping.myDialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.media.Image;
import android.os.Bundle;
import android.text.style.IconMarginSpan;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.mybookkeeping.R;
import com.example.mybookkeeping.db.DBManager;
import com.example.mybookkeeping.mAdapter.CalerdarAdapter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class CalendarDialog extends Dialog implements View.OnClickListener {
    ImageView cancelIv;
    GridView dialogCalendarGv;
    LinearLayout hsvLayout;

    public  CalerdarAdapter Adapter;

    List<TextView> hsvViewList;
    List<Integer> yearList;
    int selectPos = -1;  // 正被点击的年份的 位置坐标；
    int selectMonthPos = -1;  // 正被点击的月份的 选中坐标；
    public int year,month;

    public interface OnRefreshListener{
        public void onRefresh(int seletedPos,int year,int month);
    }
    OnRefreshListener onRefreshListener;

    public void setOnRefreshListener(OnRefreshListener onRefreshListener) {
        this.onRefreshListener = onRefreshListener;
    }

    public CalendarDialog(@NonNull Context context,int selectPos,int selectMonthPos) {
        super(context);
        this.selectPos = selectPos;
        this.selectMonthPos = selectMonthPos;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dailog_cal_rili);

        dialogCalendarGv = findViewById(R.id.dialog_canlendar_gv);
//        cancelIv = findViewById(R.id.dialog_canlendar_cancel);
        hsvLayout = findViewById(R.id.dialog_canlendar_layout);
        initTime();
        addViewToLayout();
        initGridView();
        // 设置GridView中每个Item的点击时间；
        setGvListener();
    }

    private void setGvListener() {
        dialogCalendarGv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Adapter.selectPos = position;
                Adapter.notifyDataSetChanged();
                int setmonth = position+1;
                int setyear = Adapter.year;
                // 获取到被选中的年份和月份；
                onRefreshListener.onRefresh(selectPos,setyear,setmonth);
                cancel();
            }
        });
    }

    private void initTime() {
        Calendar instance = Calendar.getInstance();
        year = instance.get(Calendar.YEAR);
        month = instance.get(Calendar.MONTH)+1;
    }

    private void initGridView() {
        int setYear = yearList.get(selectPos);
        Adapter = new CalerdarAdapter(getContext(), setYear);
        if (selectMonthPos == -1) {
            Adapter.selectPos = month-1;
        }else{
            Adapter.selectPos = month;
        }
        dialogCalendarGv.setAdapter(Adapter);
    }

    // 设置Dialog的尺寸 和 屏幕尺寸一致；
    public void setDialogSize(){
        // 获取当前窗口对象；
        Window window  = getWindow();
        // 获取窗口对象参数；
        WindowManager.LayoutParams wlp = window.getAttributes();
        // 获取屏幕宽度；
        Display dWidth = window.getWindowManager().getDefaultDisplay();
        wlp.width = dWidth.getWidth();
        wlp.gravity = Gravity.TOP;
        window.setBackgroundDrawableResource(android.R.color.transparent);
        window.setAttributes(wlp);
    }

    private void addViewToLayout() {
        hsvViewList = new ArrayList<>(); // 添加进入线性布局中的TextView；
        yearList = DBManager.getYearListFromAccounttb();  // 获取数据库中有多少个年份；
        if (yearList.size() == 0) {
            // year = Calendar.getInstance().get(Calendar.YEAR);
            yearList.add(year);
        }
        for (int i = 0; i < yearList.size();i++){
            int year = yearList.get(i);
            View view = getLayoutInflater().inflate(R.layout.item_dialog_hsv,null);
            hsvLayout.addView(view);  // 将year 年份添加到布局中；
            TextView hsvTv = view.findViewById(R.id.item_dialogcal_hsv_tv);
            hsvTv.setText(year + "");
            hsvViewList.add(hsvTv);
        }
        if (selectPos == -1){
            selectPos = hsvViewList.size()-1;  // 默认最后一个位置被选中；
        }
        changeRvbg(selectPos);   // 将年份 最后一个位置选中；
        setHSVClickListener();  // 每个view 点击时间的监听；
    }

    private void setHSVClickListener() {
        for (int i = 0;i < hsvViewList.size();i++){
            TextView view = hsvViewList.get(i);
            final int pos = i;
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    changeRvbg(pos);
                    selectPos = pos;
                    // 获取被选中的年份； 然后数据源发生变化；、
                    int setYearNum = yearList.get(selectPos);
                    Adapter.setYear(setYearNum);
                }
            });
        }
    }

    private void changeRvbg(int selectPos) {
        for (int i = 0;i < hsvViewList.size();i++){
            TextView textView = hsvViewList.get(i);
            textView.setBackgroundResource(R.drawable.dialog_cancel_bg);
            textView.setTextColor(Color.BLACK);
        }
        TextView setView = hsvViewList.get(selectPos);
        setView.setBackgroundResource(R.drawable.dialog_sure_bg);
        setView.setTextColor(Color.WHITE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

        }
    }
}
