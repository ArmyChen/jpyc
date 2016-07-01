package com.twopole.app.subject3;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;

import com.j256.ormlite.stmt.QueryBuilder;
import com.twopole.android.dslv.DragSortListView;
import com.twopole.app.base.BaseActivity;
import com.twopole.app.R;
import com.twopole.app.base.BaseVoiceActivity;
import com.twopole.app.base.BaseVoiceHeaderActivity;
import com.twopole.model.subject3.Subject3;
import com.twopole.provider.AMDragRateAdapter;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class VoiceSubject3CommandListActivity extends BaseVoiceHeaderActivity implements AdapterView.OnItemClickListener {
    private DragSortListView listView;
    private AMDragRateAdapter adapter;
    private Button btnEdit;
    List<body> l;//listview的数据源

    LinearLayout msetting_head_right_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voice_subject3_command_list);
        initHeader();
        setMyTitle("科目三语音指令列表");
        msetting_head_right_btn = (LinearLayout) findViewById(R.id.btn_header_right_add);
        msetting_head_right_btn.setVisibility(View.VISIBLE);
        msetting_head_right_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(getBaseContext(), VoiceSubject3AddCommandActivity.class);
                intent.putExtra("isEdit", false);
                startActivity(intent);
            }
        });
        //取数据设置适配器类的数据源。
        initData();
        //得到滑动listview并且设置监听器。

        listView.setDropListener(onDrop);
        listView.setRemoveListener(onRemove);

        adapter = new AMDragRateAdapter(this, l, mListener, mDeleteListener, mEditListener);
        listView.setAdapter(adapter);
        listView.setDragEnabled(true); //设置是否可拖动。

        listView.setOnItemClickListener(this);
    }

    //监听器在手机拖动停下的时候触发
    private DragSortListView.DropListener onDrop =
            new DragSortListView.DropListener() {
                @Override
                public void drop(int from, int to) {//from to 分别表示 被拖动控件原位置 和目标位置
                    if (from != to) {
                        body item = (body) adapter.getItem(from);//得到listview的适配器
                        body toItem = (body) adapter.getItem(to);//得到listview的适配器
                        int fromOrder = item.getOrder();
                        int fromId = item.getVoiceId();
                        int toOrder = toItem.getOrder();
                        int toId = toItem.getVoiceId();

                        adapter.remove(from);//在适配器中”原位置“的数据。
                        adapter.insert(item, to);//在目标位置中插入被拖动的控件。

                        try {


                            Subject3 fromSubject = getDatabaseHelper().getSubject3Dao().queryForId(fromId);
                            fromSubject.setSubject3_command_order(toOrder);
                            item.setOrder(toOrder);

                            //拖动的按钮数据更新
                            getDatabaseHelper().getSubject3Dao().update(fromSubject);

                            //重新排序
                            for (int i = 0;i<l.size();i++){
                                Subject3 subject3 =  getDatabaseHelper().getSubject3Dao().queryForId(l.get(i).getVoiceId());
                                subject3.setSubject3_command_order(i);
                                getDatabaseHelper().getSubject3Dao().update(subject3);
                            }

                            adapter.notifyDataSetChanged();
                        } catch (Exception e) {
                            Log.d("e", e.getMessage());
                        }
                    }
                }
            };
    //删除监听器，点击左边差号就触发。删除item操作。
    private DragSortListView.RemoveListener onRemove =
            new DragSortListView.RemoveListener() {
                @Override
                public void remove(int which) {
                    adapter.remove(which);
                }
            };

    private void initData() {//初始化
        String imei = getTelephonyManager().getDeviceId();
        try {
            QueryBuilder queryBuilder =  getDatabaseHelper().getSubject3Dao().queryBuilder();
            queryBuilder.orderBy("subject3_command_order",true);

            List<Subject3> subject3List = queryBuilder.where().eq("imei",imei).query();

            l = new ArrayList<body>();
            for (int i = 0; i < subject3List.size(); i++) {
                body b = new body();
                b.coin = subject3List.get(i).getSubject3_command_title();
                b.src = subject3List.get(i).getSubject3_command_short();
                b.tag = subject3List.get(i).getSubject3_command();
                b.voiceId = subject3List.get(i).getId();
                b.order = subject3List.get(i).getSubject3_command_order();
                l.add(b);
            }
            listView = (DragSortListView) findViewById(R.id.dslvList);

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onResume() {
        l.clear();
        initData();
        adapter = new AMDragRateAdapter(this, l, mListener, mDeleteListener, mEditListener);
        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        super.onResume();
    }

    @Override
    public void onItemClick(AdapterView<?> arg0, View v, int position, long id) {
        showTip("点击了列表");
    }

    private AMDragRateAdapter.MyClickListener mListener = new AMDragRateAdapter.MyClickListener() {
        @Override
        public void myOnClick(String speakMsg, View v) {

           playVoiceAsync(speakMsg);
        }
    };

    private AMDragRateAdapter.MyDeleteClickListener mDeleteListener = new AMDragRateAdapter.MyDeleteClickListener() {
        @Override
        public void myOnClick(final int position, final View v) {
            AlertDialog.Builder builder = new AlertDialog.Builder(VoiceSubject3CommandListActivity.this);
            builder.setMessage("确认删除吗？");
            builder.setTitle("提示");
            builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    try {
                        body item = (body) adapter.getItem(position);//得到listview的适配器
                        getDatabaseHelper().getSubject3Dao().deleteById(item.getVoiceId());
                        l.remove(position);
                        adapter.notifyDataSetChanged();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            });
            builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            builder.create().show();
        }
    };

    private AMDragRateAdapter.MyEditClickListener mEditListener = new AMDragRateAdapter.MyEditClickListener() {
        @Override
        public void myOnClick(int id, View v) {
            Intent intent = new Intent();
            intent.setClass(getBaseContext(), VoiceSubject3AddCommandActivity.class);
            intent.putExtra("isEdit", true);
            intent.putExtra("id", id);
            startActivity(intent);
        }
    };

    public class body {//放置adapter数据的类
        String src;
        String coin;
        String tag;
        int order;
        int voiceId;

        public String getSrc() {
            return src;
        }

        public void setSrc(String src) {
            this.src = src;
        }

        public String getCoin() {
            return coin;
        }

        public void setCoin(String coin) {
            this.coin = coin;
        }

        public String getTag() {
            return tag;
        }

        public void setTag(String tag) {
            this.tag = tag;
        }

        public int getOrder() {
            return order;
        }

        public void setOrder(int order) {
            this.order = order;
        }

        public int getVoiceId() {
            return voiceId;
        }

        public void setVoiceId(int voiceId) {
            this.voiceId = voiceId;
        }

    }
}
