package com.twopole.app.settings;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.twopole.app.R;
import com.twopole.app.base.BaiduMapActivity;
import com.twopole.app.base.BaseOnHeaderActivity;
import com.twopole.model.autoplay.Road;

import java.util.HashMap;
import java.util.List;

public class RoadSettingsActivity extends BaseOnHeaderActivity {

    ListView listView;
    Button addButton;
    SimpleAdapter simpleAdapter;
    List<HashMap<String, String>> dataList;

    int roadId;
    int postion;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_road_list);
        initHeader();
        setMyTitle("路线名设置");
        listView = (ListView) findViewById(R.id.line_list);
        addButton = (Button) findViewById(R.id.btn_roadmap_new);
        findViewById(R.id.btn_header_right).setVisibility(View.VISIBLE);
        findViewById(R.id.btn_header_right).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(getBaseContext(), BaiduMapActivity.class);
                startActivity(intent);
            }
        });

        list();
        //点击路线进入路线设置
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                HashMap<String, ?> map = (HashMap<String, ?>) parent.getItemAtPosition(position);
                Intent intent = new Intent();
                intent.putExtra("name", map.get("name").toString());
                intent.putExtra("classify_id", Integer.valueOf(map.get("classify_id").toString()));
                intent.setClass(getBaseContext(), RoadDetailSettingsActivity.class);
                startActivity(intent);
            }
        });

        //长按路线列表
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                final HashMap<String, ?> map = (HashMap<String, ?>) parent.getItemAtPosition(position);
                roadId = Integer.parseInt(map.get("classify_id").toString());
                postion = position;
                final String[] operation = {"删除线路", "编辑线路"};
                new AlertDialog.Builder(RoadSettingsActivity.this)
                        .setTitle("选择操作")
                        .setItems(operation, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which){
                                    case 0:
                                        new AlertDialog.Builder(RoadSettingsActivity.this)
                                                .setTitle("提示")
                                                .setMessage("确定删除？")
                                                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        deleteRoad();
                                                    }
                                                })
                                                .show();
                                        break;
                                    case 1:
                                        View view = View.inflate(RoadSettingsActivity.this, R.layout.dialog_input, null);
                                        final EditText editText = (EditText) view.findViewById(R.id.dialog_input_value);
                                        editText.setText(map.get("name").toString());
                                        new AlertDialog.Builder(RoadSettingsActivity.this)
                                                .setTitle("新增路线")
                                                .setView(view)
                                                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        String meditext = editText.getText().toString();
                                                        if (meditext == "") {
                                                            showTip("请输入内容");
                                                        } else {
                                                            updataRoad(meditext);
                                                        }
                                                    }
                                                })
                                                .setNegativeButton("取消", null)
                                                .show();
                                        break;
                                }
                            }
                        })
                        .setNegativeButton("确定", null)
                        .show();
                return true;
            }
        });

        //新增路线
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View view = View.inflate(RoadSettingsActivity.this, R.layout.dialog_input, null);
                final EditText editText = (EditText) view.findViewById(R.id.dialog_input_value);

                new AlertDialog.Builder(RoadSettingsActivity.this)
                        .setTitle("新增路线")
                        .setView(view)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String meditext = editText.getText().toString();
                                if (meditext == "") {
                                    showTip("请输入内容");
                                } else {
                                    addRoad(meditext);
                                }
                            }
                        })
                        .setNegativeButton("取消", null)
                        .show();
            }
        });

        initHeader();//初始化头部信息


    }

    private void addRoad(String str) {
        try {
            Road road = new Road();
            road.setImei(getTelephonyManager().getDeviceId());
            road.setIs_default(false);
            road.setRoad_name(str);
            int result = getDatabaseHelper().getRoadDao().create(road);
            if (result > 0) {
                showTip("保存成功");

                HashMap<String, String> map = new HashMap<>();
                map.put("name", road.getRoad_name());
                map.put("classify_id", String.valueOf(result));
                dataList.add(map);
            } else {
                showTip("保存失败");
            }
        } catch (Exception e) {
            showTip("保存失败");
        }
    }

    private void updataRoad(String str) {
        try {
            Road road = new Road();
            road.setImei(getTelephonyManager().getDeviceId());
            road.setIs_default(false);
            road.setRoad_name(str);
            road.setId(roadId);
            int result = getDatabaseHelper().getRoadDao().update(road);
            if (result > 0) {
                showTip("保存成功");

                HashMap<String, String> map = new HashMap<>();
                map.put("name", road.getRoad_name());
                map.put("classify_id", String.valueOf(result));
                list();
            } else {
                showTip("保存失败");
            }
        } catch (Exception e) {
            showTip("保存失败");
        }
    }

    private void deleteRoad() {
        try {

            int result = getDatabaseHelper().getRoadDao().deleteById(roadId);
            if (result > 0) {
                showTip("删除成功");
                list();
            } else {
                showTip("删除失败");
            }
        } catch (Exception e) {
            showTip("删除失败");
        }
    }

    private void list(){
        String[] from = {"name", "classify_id"};
        int[] to = {R.id.name, R.id.classify_code};

        dataList = getRoadListData();
        simpleAdapter = new SimpleAdapter(this, dataList, R.layout.dedution_menu_item, from, to);
        listView.setAdapter(simpleAdapter);
    }
}

