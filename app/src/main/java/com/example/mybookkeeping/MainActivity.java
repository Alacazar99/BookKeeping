package com.example.mybookkeeping;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.mybookkeeping.db.AccountBean;
import com.example.mybookkeeping.db.DBManager;
import com.example.mybookkeeping.mActivity.MonthChartActivity;
import com.example.mybookkeeping.mActivity.RecordActivity;
import com.example.mybookkeeping.mActivity.SearchActivity;
import com.example.mybookkeeping.mAdapter.AccountAdapter;
import com.example.mybookkeeping.myDialog.BudgetDialog;
import com.example.mybookkeeping.myDialog.MoreDialog;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    ListView todayLv;  // 展示今日收支情况；
    ImageView searchIv;
    Button editBtn;
    ImageButton moreBtn;

    // 声明数据源；

    List<AccountBean> mDatas;
    // 年份，时间；
    int year,month,day;

    AccountAdapter adapter;

    // 头布局；
    View headerView;
    TextView topOutTv,topInTv,topBudgetTv,topConTv,topTvToInfo;
    ImageView topShowIv;
    SharedPreferences preferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 初始化时间
        initTime();


        preferences = getSharedPreferences("budget", Context.MODE_PRIVATE);

        // 添加今日ListView布局；
        todayLv = findViewById(R.id.main_lv);
        addLVHeaderView();
        mDatas = new ArrayList<>();

        setLVLongClickListener();

        //设置适配器：加载每一行数据到列表当中
        adapter = new AccountAdapter(this, mDatas);
        todayLv.setAdapter(adapter);

    }

    private void setLVLongClickListener() {
        todayLv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    return false;
                }
                int pos = position-1;
                // 获取正在被点击的该项；
                AccountBean clickBean = mDatas.get(pos);
                // 弹出提示对话框；
                showDeleteItemDialog(clickBean);
                return false;
            }
        });
    }

    private void showDeleteItemDialog(final AccountBean clickBean) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("您确认删除本条记录吗？")
                .setNegativeButton("取消",null)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 获取id;
                        int clickId = clickBean.getId();
                        // 执行删除操作；
                        DBManager.deleteItemFromAccounttbById(clickId);
                        mDatas.remove(clickBean);  // 实时刷新，移除集合中的数据；
                        adapter.notifyDataSetChanged();  // 提示适配器更新数据；
                        setTopTvShow();   // 改变头布局中显示的内容；
                    }
                });
        builder.create().show();  // 显示对话框；
    }


    private void addLVHeaderView() {
        headerView = getLayoutInflater().inflate(R.layout.item_mainlv_top,null);
        todayLv.addHeaderView(headerView);

        // 查找头布局可用控件；
        topOutTv = headerView.findViewById(R.id.item_mainlv_top_out);
        topInTv = headerView.findViewById(R.id.item_mainlv_top_in);
        topBudgetTv = headerView.findViewById(R.id.item_mainlv_top_budget);
        topConTv = headerView.findViewById(R.id.item_mainlv_top_tv_day);
        topShowIv = headerView.findViewById(R.id.item_mainlv_show);
        topTvToInfo = headerView.findViewById(R.id.item_mainlv_top_tvToInfo);

        // 控件设置点击事件；
        topBudgetTv.setOnClickListener(this);
        topConTv.setOnClickListener(this);
        topShowIv.setOnClickListener(this);
        topTvToInfo.setOnClickListener(this);
    }


    private void initTime() {
        Calendar calendar = Calendar.getInstance();
        year = calendar.get(calendar.YEAR);
        month = calendar.get(calendar.MONTH)+1;
        day = calendar.get(calendar.DAY_OF_MONTH);
    }



    // 获取焦点时，会调用的方法；
    @Override
    protected void onResume() {
        super.onResume();
        loadDBData();
        setTopTvShow();
    }

    /* 设置头布局当中文本内容的显示*/
    private void setTopTvShow() {
        //获取今日支出和收入总金额，显示在view当中
        float incomeOneDay = DBManager.getSumMoneyOneDay(year, month, day, 1);
        float outcomeOneDay = DBManager.getSumMoneyOneDay(year, month, day, 0);
        String infoOneDay = "今日支出 ￥"+outcomeOneDay+"  收入 ￥"+incomeOneDay;
        topConTv.setText(infoOneDay);
//        获取本月收入和支出总金额
        float incomeOneMonth = DBManager.getSumMoneyOneMonth(year, month, 1);
        float outcomeOneMonth = DBManager.getSumMoneyOneMonth(year, month, 0);
        topInTv.setText("￥ "+incomeOneMonth);
        topOutTv.setText("￥ "+outcomeOneMonth);

//    设置显示运算剩余
        float bmoney = preferences.getFloat("bmoney", 0);// 预算
        if (bmoney == 0) {
            topBudgetTv.setText("￥ 0");
        }else{
            float syMoney = bmoney - outcomeOneMonth;
            topBudgetTv.setText("￥"+syMoney);
        }
    }

    private void loadDBData() {
        List<AccountBean> list = DBManager.getAccountListOneDayFromAccounttb(year, month, day);
        mDatas.clear();
        mDatas.addAll(list);
//        Log.d("测试",""+mDatas);
        adapter.notifyDataSetChanged();
    }

    // Button 按钮的监听事件；
    public void onButtonClick(View view) {
        switch (view.getId()){
            case R.id.main_iv_search:
                Intent intentToSearch = new Intent(this, SearchActivity.class);
                startActivity(intentToSearch);
                break;
            case R.id.main_btn_edit:
                Intent intentToEdit = new Intent(this, RecordActivity.class);
                startActivity(intentToEdit);
                break;
            case R.id.main_btn_more:
                showMoreDialog();
                break;
        }
    }

    private void showMoreDialog() {
        MoreDialog moreDialog = new MoreDialog(this);
        moreDialog.show();
        moreDialog.setDialogSize();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.item_mainlv_top_budget:
                //  设置本月预算；
                showBudgetDialog();
                break;
            case R.id.item_mainlv_top_tvToInfo:
                Intent intentToInfo = new Intent(this, MonthChartActivity.class);
                startActivity(intentToInfo);
                break;
            case R.id.item_mainlv_show:
                // 显示 隐藏；切换TextView；
                toggleShow();
                break;
        }
    }
    boolean isShow = true;
    /**
     * 点击头布局眼睛，如果数据是明文，则加密；
     * */
    private void toggleShow() {
        if (isShow) {
            PasswordTransformationMethod showToHide = PasswordTransformationMethod.getInstance();
            topOutTv.setTransformationMethod(showToHide);
            topInTv.setTransformationMethod(showToHide);
            topBudgetTv.setTransformationMethod(showToHide);
            topShowIv.setImageResource(R.mipmap.ih_hide);
            isShow = false;
        }else{
            HideReturnsTransformationMethod hideToShow = HideReturnsTransformationMethod.getInstance();
            topOutTv.setTransformationMethod(hideToShow);
            topInTv.setTransformationMethod(hideToShow);
            topBudgetTv.setTransformationMethod(hideToShow);
            topShowIv.setImageResource(R.mipmap.ih_show);
            isShow = true;
        }
    }

    private void showBudgetDialog() {
        BudgetDialog budgetDialog = new BudgetDialog(this);
        budgetDialog.show();
        budgetDialog.setDialogSize();

        budgetDialog.setOnEnsureListener(new BudgetDialog.OnEnsureListener() {
            @Override
            public void onEnsure(float money) {
                SharedPreferences.Editor editor = preferences.edit();
                editor.putFloat("bmoney",money);
                editor.commit();

                // 计算剩余金额；
                float outComeOnMonth = DBManager.getSumMoneyOneMonth(year,month,0);
                float syMoney = money - outComeOnMonth;
                topBudgetTv.setText("￥ "+syMoney);
            }
        });
    }
}
