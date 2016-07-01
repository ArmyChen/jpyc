package com.twopole.app.settings;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;

import com.twopole.app.base.BaseActivity;
import com.twopole.app.R;
import com.twopole.app.base.BaseOnHeaderActivity;
import com.twopole.utils.Vari;

public class VoiceLightSettingsActivity extends BaseOnHeaderActivity {
    private EditText mTimeSpanEditText;
    RadioButton btn_4;
    LinearLayout mbtn_header_left;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voice_light_settings);
        initHeader();
        setMyTitle("灯光指令设置");
        mTimeSpanEditText = (EditText) findViewById(R.id.timeSpan);
        mTimeSpanEditText.setOnClickListener(new MobileOnClickListener());
        mTimeSpanEditText.setOnFocusChangeListener(new MobileOnFocusChanageListener());
        mTimeSpanEditText.setOnKeyListener(new MobileOnKeyChanageListener());

        mTimeSpanEditText.setText(String.valueOf(Vari.getSpeakTimeSpans()));

    }

    //MobileOnFocusChanageListener焦点监听器
    private class MobileOnClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {

        }
    }
    //MobileOnFocusChanageListener焦点监听器
    private class MobileOnFocusChanageListener implements View.OnFocusChangeListener {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if(v.getId()==mTimeSpanEditText.getId())
                Vari.setSpeakTimeSpans(Integer.valueOf(String.valueOf(mTimeSpanEditText.getText())));
        }
    }

    private class MobileOnKeyChanageListener implements View.OnKeyListener {
        @Override
        public boolean onKey(View v, int keyCode, KeyEvent event) {
            Vari.setSpeakTimeSpans(Integer.valueOf(String.valueOf(mTimeSpanEditText.getText())));
            return false;
        }
    }
}
