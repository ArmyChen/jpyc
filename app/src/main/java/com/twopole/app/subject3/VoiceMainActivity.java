package com.twopole.app.subject3;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RelativeLayout;

import com.twopole.app.base.BaseActivity;
import com.twopole.app.R;
import com.twopole.app.autoplay.AutoPlayGuard1Activity;
import com.twopole.app.base.BaseOnFooterActivity;

public class VoiceMainActivity extends BaseOnFooterActivity {

    RelativeLayout mSimulatedLightButton;
    RelativeLayout mSubject3Button;
    RelativeLayout mAutoPlayButton;
    RelativeLayout mBtnKm2Voice;
    RadioButton btn_4;
    RadioButton btn_2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voice_main);

        mBtnKm2Voice = (RelativeLayout) findViewById(R.id.btn_km2_voice);
        mSimulatedLightButton = (RelativeLayout) findViewById(R.id.btn_light_voice);
        mSubject3Button = (RelativeLayout) findViewById(R.id.btn_km3_voice);
        mAutoPlayButton = (RelativeLayout) findViewById(R.id.btn_km3_auto);
        mBtnKm2Voice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               showTip("开发中，请耐心等待下个版本");
            }
        });
        mSimulatedLightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(VoiceMainActivity.this,VoiceSubject3SimulatedLightActivity.class);
                startActivity(intent);
            }
        });
        mSubject3Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(VoiceMainActivity.this,VoiceSubject3Activity.class);
                startActivity(intent);
            }
        });
        mAutoPlayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(VoiceMainActivity.this,AutoPlayGuard1Activity.class);
                startActivity(intent);
            }
        });

        initFooter();
        SharedPreferences.Editor editor = getSharedPreferences("mFlag");
        editor.putInt( "isFirstInstallAPP", 1);
        editor.commit();//保存第一次进入的的状态值
    }


}
