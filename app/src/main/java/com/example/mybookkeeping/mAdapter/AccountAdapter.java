package com.example.mybookkeeping.mAdapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mybookkeeping.MainActivity;
import com.example.mybookkeeping.R;
import com.example.mybookkeeping.db.AccountBean;

import java.util.Calendar;
import java.util.List;

public class AccountAdapter extends BaseAdapter {
    Context context;
    List<AccountBean> mDatas;
    LayoutInflater inflater;
    // 年份，时间；
    int year,month,day;

    public AccountAdapter(Context context, List<AccountBean> mDatas) {
        this.context = context;
        this.mDatas = mDatas;
        inflater = LayoutInflater.from(context);
        // 初始化时间；
        initTime();
    }

    private void initTime() {
        Calendar calendar = Calendar.getInstance();
        year = calendar.get(calendar.YEAR);
        month = calendar.get(calendar.MONTH)+1;
        day = calendar.get(calendar.DAY_OF_MONTH);
    }


    @Override
    public int getCount() {
        return mDatas.size();
    }

    @Override
    public Object getItem(int position) {
        return mDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null){
            convertView = inflater.inflate(R.layout.item_mainiv,parent,false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }else holder = (ViewHolder) convertView.getTag();

        AccountBean bean = mDatas.get(position);
        holder.typeIv.setImageResource(bean.getsImageId());
        // 设置类型名称；
        holder.typeTv.setText(bean.getTypename());

        holder.beizhuTv.setText(bean.getBeizhu());
        holder.moneyTv.setText("￥ " + bean.getMoney());

        if (bean.getYear() == year && bean.getMonth() == month && bean.getDay() == day){
            String time = bean.getTime().split(" ")[1];
            holder.timeTv.setText("今天"+time);
        }else{
            holder.timeTv.setText(bean.getTime());
        }
        return convertView;
    }

    class ViewHolder{
        ImageView typeIv;
        TextView typeTv,beizhuTv,timeTv,moneyTv;
        public ViewHolder(View view){
            typeIv = view.findViewById(R.id.item_main_iv);
            typeTv = view.findViewById(R.id.item_mainTv_title);
            beizhuTv = view.findViewById(R.id.item_mainTv_beizhu);
            timeTv = view.findViewById(R.id.item_mainTv_time);
            moneyTv = view.findViewById(R.id.item_mainTv_money);

        }
    }
}
