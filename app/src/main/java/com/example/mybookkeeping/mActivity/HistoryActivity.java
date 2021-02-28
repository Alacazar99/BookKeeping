package com.example.mybookkeeping.mActivity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.mybookkeeping.R;
import com.example.mybookkeeping.db.AccountBean;
import com.example.mybookkeeping.db.DBManager;
import com.example.mybookkeeping.mAdapter.AccountAdapter;
import com.example.mybookkeeping.myDialog.CalendarDialog;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class HistoryActivity extends AppCompatActivity implements View.OnClickListener {
    ListView historyLv;
    TextView timeTv;

    List<AccountBean> mDatas;
    AccountAdapter adapter;
    int year,month;
    int dialogSelyear = -1;
    int dialogSelmonth = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        historyLv = findViewById(R.id.history_ListView);
        timeTv = findViewById(R.id.history_tv_time);

        // 初始化数据源；
        mDatas = new ArrayList<>();
        // 初始化适配器；
        adapter = new AccountAdapter(this,mDatas);
        historyLv.setAdapter(adapter);
        initTime();
        timeTv.setText(year+"年" + month + "月");
        loadData(year,month);

        // 设置长按事件；
        setLVClickListener();
    }
    // 设置长按事件；
    private void setLVClickListener() {
        historyLv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                AccountBean accountBean = mDatas.get(position);
                deleteItem(accountBean);
                return false;
            }
        });
    }
    private void deleteItem ( final AccountBean accountBean){
            final int deleteId = accountBean.getId();
            AlertDialog.Builder builder;
            builder = new AlertDialog.Builder(this);
            builder.setTitle("提示")
                    .setMessage("您确定要删除本条记录么？")
                    .setNegativeButton("取消", null)
                    .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            DBManager.deleteItemFromAccounttbById(deleteId);
                            mDatas.remove(accountBean);  // 实时刷新，从数据源中删除；
                            adapter.notifyDataSetChanged();
                        }
                    });
            builder.create().show();  // 显示对话框；
        }

    // 获取指定年份 月份 收支情况的列表；
    private void loadData(int year,int month) {
        List<AccountBean> list = DBManager.getAccountListOneMonthFromAccounttb(year, month);
        mDatas.clear();
        mDatas.addAll(list);
        adapter.notifyDataSetChanged();
    }

    private void initTime() {
        Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH)+1;
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.history_back:
                finish();
                break;
            case R.id.history_setting_time:
                CalendarDialog calendarDialog = new CalendarDialog(this,dialogSelyear,dialogSelmonth);
                calendarDialog.show();
                calendarDialog.setDialogSize();
                // 设置接口回调；
                calendarDialog.setOnRefreshListener(new CalendarDialog.OnRefreshListener() {
                    @Override
                    public void onRefresh(int seletedPos, int year, int month) {
                        timeTv.setText(year+"年"+month+"月");
                        loadData(year,month);
                        dialogSelyear = seletedPos;
                        dialogSelmonth = month;
                    }
                });
                break;

        }
    }
}
