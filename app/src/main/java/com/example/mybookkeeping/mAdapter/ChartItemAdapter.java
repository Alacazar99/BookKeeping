package com.example.mybookkeeping.mAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mybookkeeping.R;
import com.example.mybookkeeping.db.ChartItemBean;
import com.example.mybookkeeping.utils.FloatUtils;

import java.util.List;

/**
 * 账单详情界面， listview 的适配器；
 */

public class ChartItemAdapter extends BaseAdapter {
    Context context;
    List<ChartItemBean> mDatas;
    LayoutInflater inflater;

    public ChartItemAdapter(Context context, List<ChartItemBean> mDatas) {
        this.context = context;
        this.mDatas = mDatas;
        this.inflater = LayoutInflater.from(context);
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

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null){
            convertView = inflater.inflate(R.layout.item_chartshow_fragment,parent,false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }
        // 获取显示内容；
        ChartItemBean bean = mDatas.get(position);
        viewHolder.iv.setImageResource(bean.getsImageId());
        viewHolder.typeTv.setText(bean.getType());
        float ratio = bean.getRatio();
        String ratio1 = FloatUtils.ratioToPercent(ratio);
        viewHolder.ratioTv.setText(ratio1);
        viewHolder.totalTv.setText("￥ " + bean.getTotalMoney());
        return convertView;
    }
    class ViewHolder{
        TextView typeTv,ratioTv,totalTv;
        ImageView iv;

        public ViewHolder(View view){
            typeTv = view.findViewById(R.id.item_chartfrag_Tv_type);
            ratioTv = view.findViewById(R.id.item_chartfrag_tv_pertion);
            totalTv = view.findViewById(R.id.item_chartfrag_price);
            iv = view.findViewById(R.id.item_chartfrag_iv);
        }
    }
}
