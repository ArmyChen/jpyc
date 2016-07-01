package com.twopole.app.settings;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.twopole.app.base.BaseActivity;
import com.twopole.app.R;
import com.twopole.app.base.BaseOnHeaderActivity;
import com.twopole.app.subject3.VoiceSubject3CommandListActivity;
import com.twopole.app.subject3.deduct.DeductionCategoryActivity;

public class VoiceSubject3SettingsActivity extends BaseOnHeaderActivity {
    LinearLayout mcmd_setting;
    LinearLayout medit_score;
    LinearLayout mroad_setting;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voice_subject3_settings);
        initHeader();
        setMyTitle("语音指令设置");
        mcmd_setting = (LinearLayout) findViewById(R.id.cmd_setting);
        medit_score = (LinearLayout) findViewById(R.id.edit_score);
        mroad_setting = (LinearLayout) findViewById(R.id.road_setting);

        mcmd_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(getBaseContext(),VoiceSubject3CommandListActivity.class);
                startActivity(intent);
            }
        });

        medit_score.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(getBaseContext(),DeductionCategoryActivity.class);
                startActivity(intent);
            }
        });

        mroad_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(getBaseContext(),RoadSettingsActivity.class);
                startActivity(intent);
            }
        });

    }
}
