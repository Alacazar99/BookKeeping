package com.example.mybookkeeping.myDialog;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.example.mybookkeeping.mActivity.AboutActivity;
import com.example.mybookkeeping.R;
import com.example.mybookkeeping.mActivity.HistoryActivity;
import com.example.mybookkeeping.mActivity.MonthChartActivity;
import com.example.mybookkeeping.mActivity.SettingActivity;

import static androidx.core.content.ContextCompat.startActivity;


public class MoreDialog extends Dialog implements View.OnClickListener {
    Button aboutBtn,settingBtn,historyBtn,infoBtn,shareBtn;
    ImageView mycancel;

    public MoreDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_more);
        initView();

    }

    private void initView() {
        aboutBtn = findViewById(R.id.dialog_more_btn_about);
        settingBtn = findViewById(R.id.dialog_more_btn_setting);
        historyBtn = findViewById(R.id.dialog_more_btn_record);
        infoBtn = findViewById(R.id.dialog_more_btn_info);
        mycancel = findViewById(R.id.dialog_more_cancel);
        shareBtn = findViewById(R.id.dialog_more_btn_share);

        aboutBtn.setOnClickListener(this);
        settingBtn.setOnClickListener(this);
        historyBtn.setOnClickListener(this);
        infoBtn.setOnClickListener(this);
        mycancel.setOnClickListener(this);
        shareBtn.setOnClickListener(this);
    }
    // 设置Dialog的尺寸和屏幕尺寸一致；
    public void setDialogSize(){
        // 获取当前窗口对象；
        Window window  = getWindow();
        // 获取窗口对象参数；
        WindowManager.LayoutParams wlp = window.getAttributes();
        // 获取屏幕宽度；
        Display dWidth = window.getWindowManager().getDefaultDisplay();
        wlp.width = dWidth.getWidth();
        wlp.gravity = Gravity.BOTTOM;
        window.setBackgroundDrawableResource(android.R.color.transparent);
        window.setAttributes(wlp);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.dialog_more_btn_about:
                intent.setClass(getContext(), AboutActivity.class);
                getContext().startActivity(intent);
                break;
            case R.id.dialog_more_btn_setting:
                intent.setClass(getContext(), SettingActivity.class);
                getContext().startActivity(intent);
                break;
            case R.id.dialog_more_btn_record:
                intent.setClass(getContext(), HistoryActivity.class);
                getContext().startActivity(intent);
                break;
            case R.id.dialog_more_btn_info:
                intent.setClass(getContext(), MonthChartActivity.class);
                getContext().startActivity(intent);
                break;
            case R.id.dialog_more_btn_share:
                shareSoftMsg("简易-记账笔记，是一款设计及其简约的手机记账软件，历史账单也可查询哦！快快下载吧？");
                break;
            case R.id.dialog_more_cancel:
                cancel();
                break;

            default:
                throw new IllegalStateException("Unexpected value: " + v.getId());
        }
    }

    private void shareSoftMsg(String s) {
        // 分享软件的函数；
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT,s);
        Intent.createChooser(intent,"简易-记账笔记");
        getContext(). startActivity(intent);
    }
}
