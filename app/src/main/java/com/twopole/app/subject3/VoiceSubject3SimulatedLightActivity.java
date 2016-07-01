package com.twopole.app.subject3;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.twopole.app.base.BaseActivity;
import com.twopole.app.R;
import com.twopole.app.base.BaseVoiceActivity;
import com.twopole.app.base.BaseVoiceHeaderActivity;
import com.twopole.model.subject3.Subject3Light;
import com.twopole.utils.Vari;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class VoiceSubject3SimulatedLightActivity extends BaseVoiceHeaderActivity {

    LinearLayout mHeaderRightButton;
    LinearLayout mImg_finish;
    LinearLayout mSubject3LightViewItem;
    Thread th;

    ListView listView = null;
    ArrayList<HashMap<String, String>> list = null;
    HashMap<String, String> map = null;
    SimpleAdapter adapter = null;
    String[] from = {"s_id", "s_name", "s_content"};              //这里是ListView显示内容每一列的列名
    int[] to = {R.id.s_id, R.id.s_name, R.id.s_content};   //这里是ListView显示每一列对应的list_item中控件的id
    private String clickMsg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voice_subject3_simulated_light);
        initHeader();
        setMyTitle("灯光模拟列表");
        list = new ArrayList<>();

        mHeaderRightButton = (LinearLayout) findViewById(R.id.btn_header_right);
        mSubject3LightViewItem = (LinearLayout) findViewById(R.id.suject3_light_viewitem);
        mHeaderRightButton.setVisibility(View.VISIBLE);
        mImg_finish = (LinearLayout) findViewById(R.id.img_finish);
        listView = (ListView) findViewById(R.id.list_viewitem);

        mHeaderRightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(VoiceSubject3SimulatedLightActivity.this, VoiceSubject3SimulatedLightSettingActivity.class);
                startActivity(intent);
            }
        });

        mImg_finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getmTts().stopSpeaking();
                Thread.interrupted();
            }
        });

        adapter = new SimpleAdapter(this, getVoiceSubject3SimulatedLightData(), R.layout.activity_voice_subject3_simulated_light_viewitem, from, to);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ListView lv = (ListView) parent;
                HashMap<String, Object> map = (HashMap<String, Object>) lv.getItemAtPosition(position);//SimpleAdapter返回Map
                clickMsg = map.get("s_content").toString();
                String[] speakArr = clickMsg.split("；");
                for (int i = 0;i<speakArr.length;i++){
                    playVoiceAsync(speakArr[i],Vari.getSpeakTimeSpans());
                }
            }
        });

    }

    @Override
    protected void onResume() {
        list.clear();
        adapter = new SimpleAdapter(VoiceSubject3SimulatedLightActivity.this, getVoiceSubject3SimulatedLightData(), R.layout.activity_voice_subject3_simulated_light_viewitem, from, to);
        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        super.onResume();
    }




}
