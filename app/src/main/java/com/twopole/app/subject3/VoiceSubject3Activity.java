package com.twopole.app.subject3;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.twopole.app.R;
import com.twopole.app.base.BaseVoiceActivity;
import com.twopole.app.base.BaseVoiceHeaderActivity;
import com.twopole.app.settings.VoiceSubject3SettingsActivity;
import com.twopole.model.subject3.DeductionCategory;
import com.twopole.model.subject3.DeductionItem;
import com.twopole.provider.AMGridViewAdapter;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VoiceSubject3Activity extends BaseVoiceHeaderActivity implements AdapterView.OnItemClickListener {
    LinearLayout mbtn_header_right;

    List<Map<String, Object>> data_list;
    ListView mCateListView;
    SimpleAdapter mCateAdapter;
    ListView mItemListView;
    SimpleAdapter mItemAdapter;

    GridView mGridView;
    AMGridViewAdapter adapter;
    HashMap<String, ?> hashMapList;
    String classify_code;
    String speakMsg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voice_subject3);
        mbtn_header_right = (LinearLayout) findViewById(R.id.btn_header_right);
        mGridView = (GridView) findViewById(R.id.kemu3_manually_gridview);
        mCateListView = (ListView) findViewById(R.id.koufen_category_list);
        mItemListView = (ListView) findViewById(R.id.koufen_item_list);
        initHeader();
        setMyTitle("科目三语音播报");
        initItemData();

        //分类
        String[] from = {"name", "classify_code"};
        int[] to = {R.id.name, R.id.classify_code};
        mCateAdapter = new SimpleAdapter(this, getCateDate(), R.layout.dedution_menu_item, from, to);
        mCateListView.setAdapter(mCateAdapter);
        mCateListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                hashMapList = (HashMap<String, ?>) parent.getItemAtPosition(position);
                classify_code = String.valueOf(hashMapList.get("classify_code"));
                try {
                    List<DeductionItem> deductionItemList = getDatabaseHelper().getDeductionItemDao().queryBuilder().where().eq("item_category_code", classify_code).query();
                    if (deductionItemList.size() > 0) {
                        String[] from2 = {"name"};
                        int[] to2 = {R.id.koufen_content};
                        mItemAdapter = new SimpleAdapter(VoiceSubject3Activity.this, getItemData(deductionItemList), R.layout.dedution_deteil_item_for_manually, from2, to2);
                        mItemListView.setAdapter(mItemAdapter);
                    } else {
                        showTip("该分类没有扣分项目");
                    }
                } catch (SQLException e) {
                    showTip(e.getMessage());
                }
            }
        });

        mItemListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                hashMapList = (HashMap<String, ?>) parent.getItemAtPosition(position);
                speakMsg = String.valueOf(hashMapList.get("name"));

                playVoiceAsync(speakMsg);
            }
        });


        mbtn_header_right.setVisibility(View.VISIBLE);
        mbtn_header_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(getBaseContext(), VoiceSubject3SettingsActivity.class);
                startActivity(intent);
            }
        });

        //新建List
        data_list = new ArrayList<>();
        data_list = getVoiceSubject3Data();
        adapter = new AMGridViewAdapter(this, data_list, mListener);
        mGridView.setAdapter(adapter);


        mGridView.setOnItemClickListener(this);
    }

    private List<? extends Map<String, ?>> initItemData() {
        List<HashMap<String, Object>> data = new ArrayList<>();
        try {
            DeductionCategory deductionCategory = getDatabaseHelper().getDeductionCategoryDao().queryBuilder().queryForFirst();
            if(deductionCategory != null){
                List<DeductionItem> deductionItemList = getDatabaseHelper().getDeductionItemDao().queryBuilder().where().eq("item_category_code",deductionCategory.getCategory_code()).query();
                if (deductionItemList.size() > 0) {
                    String[] from2 = {"name"};
                    int[] to2 = {R.id.koufen_content};
                    mItemAdapter = new SimpleAdapter(VoiceSubject3Activity.this, getItemData(deductionItemList), R.layout.dedution_deteil_item_for_manually, from2, to2);
                    mItemListView.setAdapter(mItemAdapter);
                } else {
                    showTip("查询失败");
                }

            }

        } catch (SQLException e) {

        }

        return data;
    }



    private AMGridViewAdapter.MyGridClickListener mListener = new AMGridViewAdapter.MyGridClickListener() {
        @Override
        public void myOnClick(String speakMsg, View v) {

          playVoiceAsync(speakMsg);
        }
    };

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        showTip("点击了列表");
    }

    @Override
    protected void onResume() {
        data_list.clear();
        adapter = new AMGridViewAdapter(this, getVoiceSubject3Data(), mListener);
        mGridView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        super.onResume();
    }
}
