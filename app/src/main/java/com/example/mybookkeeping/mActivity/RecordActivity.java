package com.example.mybookkeeping.mActivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.View;

import com.example.mybookkeeping.R;
import com.example.mybookkeeping.mAdapter.RecordPagerAdapter;
import com.example.mybookkeeping.mFragment.InComeFragment;
import com.example.mybookkeeping.mFragment.OutcomeFragment;
import com.google.android.material.tabs.TabLayout;
import com.example.mybookkeeping.mFragment.BaseRecordFragment;
import java.util.ArrayList;
import java.util.List;

public class RecordActivity extends AppCompatActivity {

    TabLayout tabLayout;
    ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);
        // 查找控件；

        tabLayout = findViewById(R.id.record_tabs);
        viewPager = findViewById(R.id.record_vp);

        // 设置init ViewPager 加载页面；
        initViewPager();

    }
    private  void initViewPager(){
        // 初始化ViewPager 页面；
        List<Fragment> fragmentList = new ArrayList<>();
        // 创建收入和支出页面，放置在Fragment中；
        OutcomeFragment outFrag = new OutcomeFragment();  // 支出
        InComeFragment inFrag = new InComeFragment();  // 收入；
        fragmentList.add(outFrag);
        fragmentList.add(inFrag);

        // 创建适配器；
        RecordPagerAdapter pagerAdapter = new RecordPagerAdapter(getSupportFragmentManager(), fragmentList);
        // 设置适配器；
        viewPager.setAdapter(pagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    /*点击事件*/
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.record_iv_back:
                finish();
                break;
        }
    }
}
