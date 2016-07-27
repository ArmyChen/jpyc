package com.twopole.app.settings;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;

import com.twopole.app.base.BaseActivity;
import com.twopole.app.R;
import com.twopole.app.base.BaseOnHeaderActivity;
import com.twopole.utils.Vari;

public class VoiceLightSettingsActivity extends BaseOnHeaderActivity {
    private EditText mTimeSpanEditText;
    private Button msave_time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voice_light_settings);
        initHeader();
        setMyTitle("灯光指令设置");
        mTimeSpanEditText = (EditText) findViewById(R.id.timeSpan);
        msave_time = (Button)findViewById(R.id.save_time);


        init();
    }

    /**
     * 注册界面按钮文本事件
     */
    private void init() {
        mTimeSpanEditText.setText(String.valueOf(getSharedPreferences("settings", Context.MODE_PRIVATE).getInt("speakTimeSpans",5)));
        msave_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str  = mTimeSpanEditText.getText().toString();
                SharedPreferences.Editor timeSpane =  getSharedPreferences("settings");
                if(!str.isEmpty()){
                    showTip("保存成功");
                    timeSpane.putInt("speakTimeSpans",Integer.valueOf(str));
                    timeSpane.commit();
                }else{
                    showTip("请填写时间间隔");
                    timeSpane.putInt("speakTimeSpans",5);
                    timeSpane.commit();
                }
            }
        });
    }


}
