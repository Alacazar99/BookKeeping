package com.example.mybookkeeping.mFragment;

import android.inputmethodservice.KeyboardView;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Handler;
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

import com.example.mybookkeeping.R;
import com.example.mybookkeeping.db.AccountBean;
import com.example.mybookkeeping.db.TypeBean;
import com.example.mybookkeeping.mAdapter.TypeBaseAdapter;
import com.example.mybookkeeping.myDialog.BeiZhuDialog;
import com.example.mybookkeeping.utils.KeyBoardUtils;
import com.example.mybookkeeping.myDialog.SelectTimeDialog;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public abstract class BaseRecordFragment extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    KeyboardView keyboardView;
    EditText moneyEdit;
    ImageView typeIv;
    TextView typeTv,beizhuTv,timeTv;
    GridView typeGv;
    public ArrayList typeList;
    public TypeBaseAdapter adapter;
    public KeyBoardUtils boardUtils;
    int year,month,day;

    AccountBean accountBean;
    private String time;


    public BaseRecordFragment() {
        // Required empty public constructor
    }


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
        View view = inflater.inflate(R.layout.fragment_base_record, container, false);

        timeTv = view.findViewById(R.id.frag_record_tv_time);

        // 获取当前时间；
        setInitTime();
        // 初始化
        initView(view);

        // GridView 填充数据；
        loadDataToGV();

        //设置GridView 的点击事件；
        setGvListener();

        // 设置软键盘确定按钮的点击事件；
        initKeyBoardOnclickListener();

        return view;
    }

    private void initKeyBoardOnclickListener() {
        // 设置接口，监听按钮被点击；
        boardUtils.setOnEnsureListener(new KeyBoardUtils.OnEnsureListener(){
            @Override
            public void onEnsure() {
                // 点击确定按钮;
                String moneyStr = moneyEdit.getText().toString();
                if (TextUtils.isEmpty(moneyStr) || moneyStr.equals("0")){
                    // getActivity().finish();
                    Toast.makeText(getActivity(), "金额不可为空！", Toast.LENGTH_SHORT).show();
                    return;
                }else{
                    float money = Float.parseFloat(moneyStr);
                    // 当按下 确定，将数据源存入数据库中；
                    accountBean.setMoney(money);
                    accountBean.setTypename((String) typeTv.getText());
                    accountBean.setTime(time);
                    accountBean.setYear(year);
                    accountBean.setMonth(month);
                    accountBean.setDay(day);

                    String showBeizhuText = String.valueOf(beizhuTv.getText());
                    if (showBeizhuText == "添加备注") {
                        showBeizhuText = "无备注";
                    }
                    accountBean.setBeizhu(showBeizhuText);
                    // 获取记录信息，保持数据库中；
                    Toast.makeText(getActivity(), "添加成功^.^", Toast.LENGTH_SHORT).show();
                    //获取记录的信息，保存在数据库当中
                    saveAccountToDB();
                    // 返回上一级；
                    getActivity().finish();
                }
            }
        });
    }


    // 设置初始化世间
    private void setInitTime() {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 HH:mm");
        time = sdf.format(date);

        timeTv.setText(time);

        accountBean.setTime(time);
        Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        accountBean.setYear(year);
        accountBean.setMonth(month);
        accountBean.setDay(day);
    }

    // 设置GV视图的点击事件；
    private void setGvListener(){
        typeGv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                adapter.selectPos = position;
                adapter.notifyDataSetInvalidated();   //提示绘制发生变化；
                TypeBean typeBean = (TypeBean) typeList.get(position);
                String typename = typeBean.getTypename();
                typeTv.setText(typename);

                // 上面的图也跟着切换；
                int simagId = typeBean.getSimageId();
                typeIv.setImageResource(simagId);
                accountBean.setsImageId(simagId);
            }
        });
    }

    // 加载Gv视图的数据源；
    public void loadDataToGV(){
        typeList = new ArrayList<>();
        adapter = new TypeBaseAdapter(getContext(),typeList);
        typeGv.setAdapter(adapter);
    }
    // 初始化视图；
    private void initView(final View view){
        keyboardView = view.findViewById(R.id.frag_record_keyboard);
        moneyEdit = view.findViewById(R.id.frag_record_et_money);
        typeIv = view.findViewById(R.id.frag_record_iv);
        typeGv = view.findViewById(R.id.frag_record_gv);

        typeTv = view.findViewById(R.id.frag_record_out_Tv);


        beizhuTv = view.findViewById(R.id.frag_record_tv_beizhu);

        beizhuTv.setOnClickListener(this);
        timeTv.setOnClickListener(this);

        // 显示自定义键盘；
        boardUtils = new KeyBoardUtils(keyboardView,moneyEdit);
        boardUtils.showKeyboard();

    }

    protected abstract void saveAccountToDB();

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.frag_record_tv_time:
                // 弹出显示时间对话框；
                showTimeDialog();
                break;
            case R.id.frag_record_tv_beizhu:
                // 弹出 添加备注对话框；
                showBDDialog();
                break;
        }
    }

    private void showTimeDialog() {
        final SelectTimeDialog dialog = new SelectTimeDialog(getContext());
        dialog.show();
        //设定确定按钮 被点击了的监听器;
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

    private void showBDDialog() {
        // 弹出备注对话框前先隐藏自定义按键；
        boardUtils.hideKeyboard();

        final BeiZhuDialog dialog = new BeiZhuDialog(Objects.requireNonNull(getContext()));
        dialog.show();
        dialog.setDialogSize();

        dialog.setOnEnsureListener(new BeiZhuDialog.OnEnsureListener() {
            @Override
            public void onEnsure() {
                String msg = dialog.getEditText();
                if (!TextUtils.isEmpty(msg)){
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
                },300);

            }
        });
    }



}
