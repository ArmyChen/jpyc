package com.twopole.app.settings;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.twopole.app.base.BaseActivity;
import com.twopole.app.R;
import com.twopole.app.base.BaseOnHeaderActivity;
import com.twopole.app.base.WebViewActivity;
import com.twopole.app.treasure.ArticleActivity;
import com.twopole.utils.DataCleanManager;

/**
 * Created by charmy on 2016/6/12 0012.
 */
public class VoiceSettingsActivity extends BaseOnHeaderActivity {

    LinearLayout mbtn_light_setting;
    LinearLayout mbtn_kemu3_setting;
    LinearLayout mhelp;
    LinearLayout mabout;
    LinearLayout msetting_btn_clear_cache;
    LinearLayout mbtn_kemu2_setting;
    TextView mcache_size;
    RadioButton btn_4;
    LinearLayout mbtn_header_left;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting);
        initHeader();
        setMyTitle("语音指令");
        mbtn_light_setting = (LinearLayout) findViewById(R.id.btn_light_setting);
        mbtn_kemu3_setting = (LinearLayout) findViewById(R.id.btn_kemu3_setting);
        mbtn_kemu2_setting = (LinearLayout) findViewById(R.id.btn_kemu2_setting);
        mhelp = (LinearLayout) findViewById(R.id.help);
        mabout = (LinearLayout) findViewById(R.id.about);
        msetting_btn_clear_cache = (LinearLayout) findViewById(R.id.setting_btn_clear_cache);
        mcache_size = (TextView) findViewById(R.id.cache_size);
        try {
            mcache_size.setText(DataCleanManager.getTotalCacheSize(this));
        } catch (Exception e) {

        }

        mbtn_kemu2_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTip("开发中，请耐心等待下个版本");
            }
        });
        mbtn_light_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(getBaseContext(),VoiceLightSettingsActivity.class);
                startActivity(intent);
            }
        });

        mbtn_kemu3_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(getBaseContext(),VoiceSubject3SettingsActivity.class);
                startActivity(intent);
            }
        });

        mhelp.setVisibility(View.GONE);
        mhelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(getBaseContext(),WebViewActivity.class);
                intent.putExtra("loadUrl","http://www.2pole.com/FAQ");
                startActivity(intent);
            }
        });

        mabout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(getBaseContext(),ArticleActivity.class);
                intent.putExtra("articleType",1);
                startActivity(intent);
            }
        });

        msetting_btn_clear_cache.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DataCleanManager.clearAllCache(VoiceSettingsActivity.this);
                try {
                    mcache_size.setText(DataCleanManager.getTotalCacheSize(VoiceSettingsActivity.this));
                } catch (Exception e) {

                }
            }
        });

    }


}
