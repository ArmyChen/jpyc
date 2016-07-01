package com.twopole.provider;

/**
 * Created by Administrator on 2016-06-20.
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.twopole.app.R;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class HorizontalListViewAdapter extends BaseAdapter{
    private Context context;
    List<Map<String, Object>> items;//适配器的数据源
    private MyHListViewClickListener mListener;

    public HorizontalListViewAdapter(Context context,
                             List<Map<String, Object>> list,
                             MyHListViewClickListener listener) {
        this.context = context;
        this.items = list;
        this.mListener = listener;
    }

    @Override
    public int getCount() {
        return items.size();
    }
    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        HashMap<String,Object> item = (HashMap<String, Object>) getItem(position);
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.horizontal_list_item, null);
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

    public static abstract class MyHListViewClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            Button btn = (Button)v;
            myOnClick(btn.getTag().toString(), v);
        }

        public abstract void myOnClick(String speakMsg, View v);
    }
}
