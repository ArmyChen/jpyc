package com.twopole.app.subject3;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.twopole.app.R;
import com.twopole.app.base.BaseVoiceActivity;
import com.twopole.app.base.BaseVoiceHeaderActivity;
import com.twopole.model.subject3.Subject3Light;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class VoiceSubject3SimulatedLightSettingActivity extends BaseVoiceHeaderActivity {

    private ListView listView = null;
    ArrayList<HashMap<String, String>> list = null;
    HashMap<String, String> map = null;
    SimpleAdapter adapter = null;
    EditText editText = null;
    LinearLayout linearLayout = null;


    String[] from = {"id", "l_name", "l_content"};              //这里是ListView显示内容每一列的列名
    int[] to = {R.id.id, R.id.l_name, R.id.l_content};   //这里是ListView显示每一列对应的list_item中控件的id
    private String clickMsg;
    private String clickId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voice_subject3_simulated_light_setting);
        initHeader();
        setMyTitle("灯光模拟指令设置");
        listView = (ListView) findViewById(R.id.light_list);
        list = new ArrayList<>();
        adapter = new SimpleAdapter(this, getVoiceSubject3SimulatedLightSettingData(), R.layout.activity_voice_subject3_simulated_light_item, from, to);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ListView lv = (ListView) parent;
                HashMap<String, Object> map = (HashMap<String, Object>) lv.getItemAtPosition(position);//SimpleAdapter返回Map
                clickMsg = map.get("l_content").toString();
                clickId = map.get("id").toString();

                //加载布局管理器
                LayoutInflater inflater = LayoutInflater.from(VoiceSubject3SimulatedLightSettingActivity.this);
                //将xml布局转换为view对象
                View convertView = inflater.inflate(R.layout.common_dialog, parent, false);
                //利用view对象，找到布局中的组件
                editText = (EditText) convertView.findViewById(R.id.dialog_generic_htv_message);// 内容
                editText.setText(clickMsg);

                new AlertDialog.Builder(VoiceSubject3SimulatedLightSettingActivity.this)
                        .setTitle("指令编辑")
                        .setView(convertView)
                        .setPositiveButton("确定", OkOnClickListener )
                        .show();
                // showTip(String.valueOf(map.get("id")));
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                ListView lv = (ListView) parent;
                HashMap<String, Object> map = (HashMap<String, Object>) lv.getItemAtPosition(position);//SimpleAdapter返回Map
                clickMsg = map.get("l_content").toString();
                clickId = map.get("id").toString();

                new AlertDialog.Builder(VoiceSubject3SimulatedLightSettingActivity.this)
                        .setTitle("删除指令")
                        .setPositiveButton("确定", DeleteOnClickListener )
                        .show();
                return true;
            }
        });

        //添加按钮
        linearLayout = (LinearLayout) findViewById(R.id.btn_header_right_add);
        linearLayout.setVisibility(View.VISIBLE);
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //加载布局管理器
                LayoutInflater inflater = LayoutInflater.from(VoiceSubject3SimulatedLightSettingActivity.this);
                //将xml布局转换为view对象
                View convertView = inflater.inflate(R.layout.common_dialog, null);
                //利用view对象，找到布局中的组件
                editText = (EditText) convertView.findViewById(R.id.dialog_generic_htv_message);// 内容
                editText.setText(clickMsg);

                new AlertDialog.Builder(VoiceSubject3SimulatedLightSettingActivity.this)
                        .setTitle("指令添加")
                        .setView(convertView)
                        .setPositiveButton("确定", AddClickListener)
                        .show();
            }
        });
    }
    private DialogInterface.OnClickListener AddClickListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            Subject3Light subject3Light = new Subject3Light();
            subject3Light.setImei(getTelephonyManager().getDeviceId());
            subject3Light.setVoice(String.valueOf(editText.getText()));
            try {
                getDatabaseHelper().getSubject3LightDao().create(subject3Light);
                refreshLight();
            } catch (SQLException e) {
                Log.d(getLocalClassName(), e.getMessage());
            }
        }
    };



    private DialogInterface.OnClickListener OkOnClickListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            Subject3Light subject3Light = new Subject3Light();
            subject3Light.setId(Integer.parseInt(clickId));
            subject3Light.setImei(getTelephonyManager().getDeviceId());
            subject3Light.setVoice(String.valueOf(editText.getText()));
            try {
                getDatabaseHelper().getSubject3LightDao().update(subject3Light);
                refreshLight();
            } catch (SQLException e) {
                Log.d(getLocalClassName(), e.getMessage());
            }
        }
    };

    private DialogInterface.OnClickListener DeleteOnClickListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            Subject3Light subject3Light = new Subject3Light();
            subject3Light.setId(Integer.parseInt(clickId));
            subject3Light.setImei(getTelephonyManager().getDeviceId());
            subject3Light.setVoice(String.valueOf(editText.getText()));
            try {
                getDatabaseHelper().getSubject3LightDao().delete(subject3Light);
                refreshLight();
            } catch (SQLException e) {
                Log.d(getLocalClassName(), e.getMessage());
            }
        }
    };


    private void refreshLight() {
        list.clear();
        adapter = new SimpleAdapter(VoiceSubject3SimulatedLightSettingActivity.this, getVoiceSubject3SimulatedLightSettingData(), R.layout.activity_voice_subject3_simulated_light_item, from, to);
        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

}
