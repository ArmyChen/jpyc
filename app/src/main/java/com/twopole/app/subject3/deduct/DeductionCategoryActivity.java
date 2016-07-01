package com.twopole.app.subject3.deduct;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;

import com.twopole.android.pinned.PinnedSectionListView;
import com.twopole.app.base.BaseActivity;
import com.twopole.app.R;
import com.twopole.bean.DeductionCategoryBean;
import com.twopole.bean.DeductionItemBean;
import com.twopole.provider.AMPinnedSectionListAdapter;

import java.util.ArrayList;

public class DeductionCategoryActivity extends BaseActivity {
    private ArrayList<DeductionCategoryBean> contactsList;
    private ArrayList<DeductionItemBean> dataList;
    LinearLayout mbtn_header_right_add;
    PinnedSectionListView pslv;
    AMPinnedSectionListAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deduction_category);

        pslv = (PinnedSectionListView) findViewById(R.id.deducion_listView);
        mbtn_header_right_add = (LinearLayout) findViewById(R.id.btn_header_right_add);
        mbtn_header_right_add.setVisibility(View.VISIBLE);

        contactsList = new ArrayList<>();
        dataList = new ArrayList<>();
        dataList = getDeductionCategoryData();
        adapter = new AMPinnedSectionListAdapter(this,dataList ,mEditcateListener,mAddItemListener);
        pslv.setAdapter(adapter);

        pslv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // 判断点击子菜单才会触发点击事件
                DeductionItemBean datas = dataList.get(position);

                if (dataList.get(position).type == DeductionItemBean.CHILD) {
                    String categoryCode = datas.categoryCode;//分类ID
                    int itemId = datas.contacts.id;//项目ID

                    Intent intent = new Intent();
                    intent.setClass(getBaseContext(), DeductionCategoryAddItemActivity.class);
                    intent.putExtra("itemId", itemId);
                    intent.putExtra("categoryCode", categoryCode);
                    startActivity(intent);
                }
            }
        });

        mbtn_header_right_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(getBaseContext(), DeductionAddCategoryActivity.class);
                startActivity(intent);
            }
        });
    }



    @Override
    protected void onResume() {
        getVoiceSubject3Data();
        adapter = new AMPinnedSectionListAdapter(this, getDeductionCategoryData(),mEditcateListener,mAddItemListener);
        pslv.setAdapter(adapter);
        adapter.notifyDataSetChanged();



        super.onResume();
    }

    private AMPinnedSectionListAdapter.EditcateListener mEditcateListener = new AMPinnedSectionListAdapter.EditcateListener() {

        @Override
        public void myOnClick(String id,View v) {
            Intent intent = new Intent();
            intent.setClass(getBaseContext(),DeductionAddCategoryActivity.class);
            intent.putExtra("categoryCode",id);
            startActivity(intent);
        }
    };

    private AMPinnedSectionListAdapter.AddItemListener mAddItemListener = new AMPinnedSectionListAdapter.AddItemListener() {

        @Override
        public void myOnClick(String id,View v) {
            Intent intent = new Intent();
            intent.setClass(getBaseContext(),DeductionCategoryAddItemActivity.class);
            intent.putExtra("categoryCode",id);
            startActivity(intent);
        }
    };


}
