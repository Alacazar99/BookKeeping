package com.example.mybookkeeping.mActivity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mybookkeeping.R;
import com.example.mybookkeeping.db.AccountBean;
import com.example.mybookkeeping.db.DBManager;
import com.example.mybookkeeping.mAdapter.AccountAdapter;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {
    ListView searchLv;
    EditText searchEt;
    TextView emptyTv;
    List<AccountBean> mDatas;
    AccountAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        initView();
        // 初始化数据源；
        mDatas = new ArrayList<>();
        // 初始化适配器；
        adapter = new AccountAdapter(this,mDatas);
        // 配置 适配器；
        searchLv.setAdapter(adapter);
        searchLv.setEmptyView(emptyTv);  // 设置无数据时，显示的控件；
    }

    private void initView() {
        searchLv = findViewById(R.id.search_lv);
        searchEt = findViewById(R.id.search_et);
        emptyTv = findViewById(R.id.search_tv_empty);
    }


    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.search_iv_back:
                finish();
                break;
            case R.id.search_iv_sh:
                //搜索操作；
                String msg = searchEt.getText().toString().trim();
                // 判断msg 是否为空，如果为空，则提示不可搜索；
                if (TextUtils.isEmpty(msg)) {
                    Toast.makeText(this, "输入内容不可为空！", Toast.LENGTH_SHORT).show();
                    return;
                }
                // 开始搜索；
                List<AccountBean> list = DBManager.getAccountListByRemarkFromAccounttb(msg);
                mDatas.clear();
                mDatas.addAll(list);
                adapter.notifyDataSetChanged();
                break;
        }
    }
}
