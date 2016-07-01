package com.twopole.provider;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.twopole.android.pinned.PinnedSectionListView;
import com.twopole.app.R;
import com.twopole.bean.DeductionItemBean;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016-06-14.
 */
public class AMPinnedSectionListAdapter extends BaseAdapter implements PinnedSectionListView.PinnedSectionListAdapter {
    ArrayList<DeductionItemBean> data;
    Context context;
    private EditcateListener editcateListener;
    private AddItemListener addItemListener;
    public AMPinnedSectionListAdapter(Context _context, ArrayList<DeductionItemBean> _data, EditcateListener _editcateListener, AddItemListener _addItemListener) {
        context = _context;
        data = _data;
        editcateListener =_editcateListener;
        addItemListener = _addItemListener;
    }


    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public int getItemViewType(int position) {
        return data.get(position).type;
    }

    @Override
    public int getViewTypeCount() {
        return DeductionItemBean.STYE;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        int type = getItemViewType(position);
        DeductionItemBean data = (DeductionItemBean) getItem(position);
        switch (type) {
            case DeductionItemBean.GROUP:
                if (convertView == null)
                    convertView = LayoutInflater.from(context).inflate(R.layout.dedution_category_item, null);

                TextView tv = (TextView) convertView.findViewById(R.id.btn_category_mode_1_text);//分类名称
                tv.setText(data.text);


                LinearLayout meditcate = (LinearLayout) convertView.findViewById(R.id.add_cate_text);//点击文字
                meditcate.setTag(data.categoryCode);
                meditcate.setOnClickListener(editcateListener);

                LinearLayout addItem = (LinearLayout) convertView.findViewById(R.id.add_cate_btn);//点击添加扣分
                addItem.setTag(data.categoryCode);
                addItem.setOnClickListener(addItemListener);

                break;

            case DeductionItemBean.CHILD:
                if (convertView == null)
                    convertView = LayoutInflater.from(context).inflate(R.layout.dedution_detail_item, null);

                TextView tv1 = (TextView) convertView.findViewById(R.id.deduction_shortname);//简称
                TextView tv2 = (TextView) convertView.findViewById(R.id.deduction_content);//内容
                TextView tv3 = (TextView) convertView.findViewById(R.id.deduction_score);//分数
                tv1.setText(data.contacts.reason.charAt(0) + "");
                tv2.setText(data.contacts.reason);
                tv3.setText(data.contacts.score);


                break;

            default:
                break;
        }
        return convertView;
    }

    @Override
    public boolean isItemViewTypePinned(int viewType) {
        boolean type = false;
        switch (viewType) {
            case DeductionItemBean.CHILD:

                type = false;

                break;

            case DeductionItemBean.GROUP:

                type = true;

                break;

            default:
                break;
        }
        return type;
    }

    public static abstract class EditcateListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            String id = (String) v.getTag();
            myOnClick(id,v);
        }

        public abstract void myOnClick(String id ,View v);
    }

    public static abstract class AddItemListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            String id = (String) v.getTag();
            myOnClick(id,v);
        }

        public abstract void myOnClick(String id ,View v);
    }
}
