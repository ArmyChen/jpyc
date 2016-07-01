package com.twopole.provider;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.twopole.app.R;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Created by Administrator on 2016/6/13.
 */
public class AMGridViewAdapter extends BaseAdapter {
    private Context context;
    List<Map<String, Object>> items;//适配器的数据源

    private MyGridClickListener mListener;

    public AMGridViewAdapter(Context context,
                             List<Map<String, Object>> list,
                             MyGridClickListener listener) {
        this.context = context;
        this.items = list;
        this.mListener = listener;
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

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        HashMap<String,Object> item = (HashMap<String, Object>) getItem(position);
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.all_command_list_item, null);
            viewHolder.voice_subject3_main_button = (Button) convertView.findViewById(R.id.voice_subject3_main_button);
            viewHolder.voice_subject3_main_text = (TextView) convertView.findViewById(R.id.voice_subject3_main_text);
            viewHolder.voice_subject3_speaker = (TextView) convertView.findViewById(R.id.voice_subject3_speaker);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.voice_subject3_main_button.setText(String.valueOf(item.get("voice_subject3_main_button").toString().charAt(0)));
        viewHolder.voice_subject3_main_text.setText(item.get("voice_subject3_main_text").toString());
        viewHolder.voice_subject3_main_button.setTag(item.get("voice_subject3_speaker").toString());
        viewHolder.voice_subject3_main_button.setHint(item.get("voice_subject3_id").toString());
        viewHolder.voice_subject3_main_button.setOnClickListener(mListener);
        return convertView;
    }

    class ViewHolder {
        Button voice_subject3_main_button;
        TextView voice_subject3_main_text;
        TextView voice_subject3_speaker;
    }

    public static abstract class MyGridClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            Button btn = (Button)v;
            myOnClick(btn.getTag().toString(), v);
        }

        public abstract void myOnClick(String speakMsg, View v);
    }

}
