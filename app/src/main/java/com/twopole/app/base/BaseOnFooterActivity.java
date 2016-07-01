package com.twopole.app.base;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.twopole.app.R;
import com.twopole.app.settings.VoiceSettingsActivity;
import com.twopole.app.subject3.VoiceMainActivity;
import com.twopole.app.treasure.TreasureActivity;

/**
 * Created by Administrator on 2016-06-16.
 */
public class BaseOnFooterActivity extends BaseVoiceActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.footer);

    }

    /**
     * 初始化底部按钮事件
     *
     */
    public void initFooter() {
        findViewById(R.id.btn_4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(getBaseContext(), VoiceSettingsActivity.class);
                startActivity(intent);
            }
        });
        findViewById(R.id.btn_3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(getBaseContext(), WebViewActivity.class);
                intent.putExtra("loadUrl","http://weixin.2pole.com/booking/subject3");
                startActivity(intent);
            }
        });
        findViewById(R.id.btn_2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(getBaseContext(), TreasureActivity.class);
                startActivity(intent);
            }
        });
//        findViewById(R.id.btn_1).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent();
//                intent.setClass(getBaseContext(), VoiceMainActivity.class);
//                startActivity(intent);
//            }
//        });
    }
}
