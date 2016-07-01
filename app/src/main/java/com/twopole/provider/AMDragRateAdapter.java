package com.twopole.provider;

import java.util.List;

import com.twopole.app.R;
import com.twopole.app.subject3.VoiceSubject3CommandListActivity.body;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class AMDragRateAdapter extends BaseAdapter {

    private Context context;
    List<body> items;//适配器的数据源
    private MyDeleteClickListener mDeleteListener;
    private MyEditClickListener myEditClickListener;
    private MyClickListener mListener;

    public AMDragRateAdapter(Context context,
                             List<body> list,
                             MyClickListener listener,
                             MyDeleteClickListener deleteListener,
                             MyEditClickListener editClickListener) {
        this.context = context;
        this.items = list;
        this.mListener = listener;
        this.mDeleteListener = deleteListener;
        this.myEditClickListener = editClickListener;
    }


    @Override
    public int getCount() {
        // TODO Auto-generated method stub  
        return items.size();
    }

    @Override
    public Object getItem(int arg0) {
        // TODO Auto-generated method stub  
        return items.get(arg0);
    }

    @Override
    public long getItemId(int arg0) {
        // TODO Auto-generated method stub  
        return arg0;
    }

    public void remove(int arg0) {//删除指定位置的item  
        items.remove(arg0);
        this.notifyDataSetChanged();//不要忘记更改适配器对象的数据源  
    }

    public void insert(body item, int arg0) {
        items.add(arg0, item);
        this.notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        body item = (body) getItem(position);
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.list_subject3_command_item, null);
            viewHolder.tvTitle = (TextView) convertView.findViewById(R.id.tvTitle);
            viewHolder.ivCountryLogo = (Button) convertView.findViewById(R.id.ivCountryLogo);
            viewHolder.ivDelete = (ImageView) convertView.findViewById(R.id.click_remove);
            viewHolder.ivDragHandle = (ImageView) convertView.findViewById(R.id.drag_handle);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.tvTitle.setText(item.getCoin());
        viewHolder.ivCountryLogo.setText(String.valueOf(item.getSrc().charAt(0)));
        viewHolder.ivCountryLogo.setTag(item.getTag());
        viewHolder.ivDelete.setTag(position);
        viewHolder.tvTitle.setTag(item.getVoiceId());
        viewHolder.ivCountryLogo.setOnClickListener(mListener);
        viewHolder.ivDelete.setOnClickListener(mDeleteListener);
        viewHolder.tvTitle.setOnClickListener(myEditClickListener);

        return convertView;
    }

    class ViewHolder {
        TextView tvTitle;
        Button ivCountryLogo;
        ImageView ivDelete;
        ImageView ivDragHandle;
    }

    public static abstract class MyClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            myOnClick(v.getTag().toString(), v);
        }

        public abstract void myOnClick(String speakMsg, View v);
    }

    public static abstract class MyDeleteClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            myOnClick((Integer) v.getTag(), v);
        }

        public abstract void myOnClick(int position, View v);
    }

    public static abstract class MyEditClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            myOnClick((Integer) v.getTag(), v);
        }

        public abstract void myOnClick(int position, View v);
    }
}  