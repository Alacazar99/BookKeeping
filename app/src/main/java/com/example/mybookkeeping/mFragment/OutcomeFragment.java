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
 */
public class OutcomeFragment extends BaseRecordFragment {

    @Override
    public void loadDataToGV() {
        super.loadDataToGV();
        // //获取数据库当中的数据源；
        List<TypeBean> outlist = DBManager.getTypeList(0);
        typeList.addAll(outlist);
        adapter.notifyDataSetChanged();
        typeTv.setText("其他");
        moneyEdit.setHint("支出金额");
        typeIv.setImageResource(R.mipmap.ic_qita_fs);
    }

    @Override
    protected void saveAccountToDB() {
        accountBean.setKind(0);
//        accountBean.setTypename();
        DBManager.insertItemToAccounttb(accountBean);
    }
}
