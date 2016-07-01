package com.twopole.app.base;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.twopole.app.R;

/**
 * Created by Administrator on 2016-06-16.
 */
public class BaseOnHeaderActivity extends BaseVoiceActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.header);
    }

    /**
     * 初始化头部菜单
     *
     */
    public void initHeader() {
       findViewById(R.id.btn_header_left).setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
                finish();
           }
       });
    }

    public void setMyTitle(String str) {
        TextView m_setting_head_title = (TextView) findViewById(R.id.setting_head_title);
        m_setting_head_title.setText(str);
    }
}
