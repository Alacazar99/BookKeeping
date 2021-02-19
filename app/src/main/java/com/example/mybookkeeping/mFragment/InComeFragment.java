package com.example.mybookkeeping.mFragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mybookkeeping.R;
import com.example.mybookkeeping.db.AccountBean;
import com.example.mybookkeeping.db.DBManager;
import com.example.mybookkeeping.db.TypeBean;
import com.example.mybookkeeping.mAdapter.TypeBaseAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class InComeFragment extends BaseRecordFragment {


    @Override
    public void loadDataToGV() {
        super.loadDataToGV();
        // 获取数据源；
        List<TypeBean> inList = DBManager.getTypeList(1);
        typeList.addAll(inList);
        adapter.notifyDataSetChanged();
        typeTv.setText("其他");
        typeIv.setImageResource(R.mipmap.in_qt_fs);
        moneyEdit.setHint("收入金额");
    }

    @Override
    protected void saveAccountToDB() {
        accountBean.setKind(1);
        DBManager.insertItemToAccounttb(accountBean);
    }
}
