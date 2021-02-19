package com.example.mybookkeeping.mActivity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.mybookkeeping.R;
import com.example.mybookkeeping.db.AccountBean;
import com.example.mybookkeeping.db.DBManager;
import com.example.mybookkeeping.mAdapter.AccountAdapter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class HistoryActivity extends AppCompatActivity implements View.OnClickListener {
    ListView historyLv;
    TextView timeTv;

    List<AccountBean> mDatas;
    AccountAdapter adapter;
    int year,month;

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
        loadData();
    }

    // 获取指定年份 月份 收支情况的列表；
    private void loadData() {
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
                break;

        }
    }
}
