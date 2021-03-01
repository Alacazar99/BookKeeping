package com.example.mybookkeeping.mFragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mybookkeeping.R;
import com.example.mybookkeeping.db.AccountBean;
import com.example.mybookkeeping.db.TypeBean;
import com.example.mybookkeeping.mAdapter.TypeBaseAdapter;
import com.example.mybookkeeping.myDialog.BeiZhuDialog;
import com.example.mybookkeeping.myDialog.SelectTimeDialog;
import com.example.mybookkeeping.utils.KeyBoardUtils;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */


import android.inputmethodservice.KeyboardView;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public abstract class BaseComFragment extends Fragment implements View.OnClickListener{
    /**
     * 记录页面当中的支出模块
     */

        KeyboardView keyboardView;
        EditText moneyEt;
        ImageView typeIv;
        TextView typeTv, beizhuTv, timeTv;
        GridView typeGv;
        public KeyBoardUtils boardUtils;
        List<TypeBean> typeList;
        TypeBaseAdapter adapter;
        AccountBean accountBean;   //将需要插入到记账本当中的数据保存成对象的形式

        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            accountBean = new AccountBean();   //创建对象
            accountBean.setTypename("其他");
            accountBean.setsImageId(R.mipmap.ic_qita_fs);
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            // Inflate the layout for this fragment
            View view = inflater.inflate(R.layout.fragment_outcome, container, false);
            initView(view);
            setInitTime();
            //给GridView填充数据的方法
            loadDataToGV();
            setGVListener(); //设置GridView每一项的点击事件

            // 设置软键盘确定按钮的点击事件；
            initKeyBoardOnclickListener();
            return view;
        }
    private void initKeyBoardOnclickListener() {
//        //让自定义软键盘显示出来
//        KeyBoardUtils boardUtils = new KeyBoardUtils(keyboardView, moneyEt);
//        boardUtils.showKeyboard();
        //设置接口，监听确定按钮按钮被点击了
        boardUtils.setOnEnsureListener(new KeyBoardUtils.OnEnsureListener() {
            @Override
            public void onEnsure() {
                //获取输入钱数
                String moneyStr = moneyEt.getText().toString();
                if (TextUtils.isEmpty(moneyStr) || moneyStr.equals("0")) {
                    getActivity().finish();
                    return;
                }
                float money = Float.parseFloat(moneyStr);
                accountBean.setMoney(money);
                //获取记录的信息，保存在数据库当中
                saveAccountToDB();
                // 返回上一级页面
                getActivity().finish();
            }
        });
    }

        /* 获取当前时间，显示在timeTv上*/
        private void setInitTime() {
            Date date = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 HH:mm");
            String time = sdf.format(date);
            timeTv.setText(time);
            accountBean.setTime(time);

            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH) + 1;
            int day = calendar.get(Calendar.DAY_OF_MONTH);
            accountBean.setYear(year);
            accountBean.setMonth(month);
            accountBean.setDay(day);
        }

        /* 设置GridView每一项的点击事件*/
        private void setGVListener() {
            typeGv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    adapter.selectPos = position;
                    adapter.notifyDataSetInvalidated();  //提示绘制发生变化了
                    TypeBean typeBean = typeList.get(position);
                    String typename = typeBean.getTypename();
                    typeTv.setText(typename);
                    accountBean.setTypename(typename);
                    int simageId = typeBean.getSimageId();
                    typeIv.setImageResource(simageId);
                    accountBean.setsImageId(simageId);
                }
            });
        }

        /* 给 GridView 填出 数据 的方法*/
        public void loadDataToGV() {
            typeList = new ArrayList<>();
            adapter = new TypeBaseAdapter(getContext(), typeList);
            typeGv.setAdapter(adapter);
        }

        private void initView(View view) {
            keyboardView = view.findViewById(R.id.frag_record_keyboard);
            moneyEt = view.findViewById(R.id.frag_record_et_money);
            typeIv = view.findViewById(R.id.frag_record_iv);
            typeGv = view.findViewById(R.id.frag_record_gv);
            typeTv = view.findViewById(R.id.frag_record_tv_type);
            beizhuTv = view.findViewById(R.id.frag_record_tv_beizhu);
            timeTv = view.findViewById(R.id.frag_record_tv_time);
            beizhuTv.setOnClickListener(this);
            timeTv.setOnClickListener(this);

            // 显示自定义键盘；
            boardUtils = new KeyBoardUtils(keyboardView,moneyEt);
            boardUtils.showKeyboard();

        }

        /* 让子类一定要重写这个方法*/
        public abstract void saveAccountToDB();

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.frag_record_tv_time:
                    showTimeDialog();
                    break;
                case R.id.frag_record_tv_beizhu:
                    showBZDialog();
                    break;
            }
        }

        /* 弹出显示时间的对话框*/
        private void showTimeDialog() {
            SelectTimeDialog dialog = new SelectTimeDialog(getContext());
            dialog.show();
            //设定确定按钮被点击了的监听器
            dialog.setOnEnsureListener(new SelectTimeDialog.OnEnsureListener() {
                @Override
                public void onEnsure(String time, int year, int month, int day) {
                    timeTv.setText(time);
                    accountBean.setTime(time);
                    accountBean.setYear(year);
                    accountBean.setMonth(month);
                    accountBean.setDay(day);
                }
            });
        }

        /* 弹出备注对话框*/
        public void showBZDialog() {
            // 弹出备注对话框前先隐藏自定义按键；
            boardUtils.hideKeyboard();

            final BeiZhuDialog dialog = new BeiZhuDialog(getContext());
            dialog.show();
            dialog.setDialogSize();
            dialog.setOnEnsureListener(new BeiZhuDialog.OnEnsureListener() {
                @Override
                public void onEnsure() {
                    String msg = dialog.getEditText();
                    if (!TextUtils.isEmpty(msg)) {
                        beizhuTv.setText(msg);
                        accountBean.setBeizhu(msg);
                    }
                    dialog.cancel();
                    // 设置延时操作；
                    initDelayed();
                }

                @Override
                public void onCancel() {
                    // 点击取消按钮；
                    dialog.cancel();
                    // 设置延时操作；
                    initDelayed();
                }
                private void initDelayed() {
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            // 自定义对话框销毁，则自定义按键弹出；
                            boardUtils.showKeyboard();
                        }
                    }, 300);
                }
            });
    }
}


