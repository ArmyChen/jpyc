package com.twopole.app.subject3;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;
import com.twopole.app.base.BaseActivity;
import com.twopole.app.R;
import com.twopole.app.base.BaseVoiceActivity;
import com.twopole.app.base.BaseVoiceHeaderActivity;
import com.twopole.model.subject3.Subject3;

import java.sql.SQLException;

public class VoiceSubject3AddCommandActivity extends BaseVoiceHeaderActivity {
    LinearLayout mbtn_header_right;
    EditText medit_command_name;
    EditText medit_command_shortname;
    EditText medit_command_pronounce;
    Button mvoice_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voice_subject3_add_command);
        initHeader();
        setMyTitle("语音指令添加");
        mbtn_header_right = (LinearLayout) findViewById(R.id.btn_header_right2);
        medit_command_name = (EditText) findViewById(R.id.edit_command_name);//指令标题
        medit_command_shortname = (EditText) findViewById(R.id.edit_command_shortname);//指令缩略名
        medit_command_pronounce = (EditText) findViewById(R.id.edit_command_pronounce);//发音的文本
        mvoice_button = (Button) findViewById(R.id.voice_button);//发音按钮

        if(getIntent().getBooleanExtra("isEdit",false)){//如果是编辑模式，那么赋值
            int id = getIntent().getIntExtra("id",0);
            try {
                Subject3 subject3 = getDatabaseHelper().getSubject3Dao().queryForId(id);
                medit_command_name.setText(subject3.getSubject3_command_title());
                medit_command_shortname.setText(subject3.getSubject3_command_short());
                medit_command_pronounce.setText(subject3.getSubject3_command());
                mvoice_button.setText(subject3.getSubject3_command_short());

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        mbtn_header_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String imei = getTelephonyManager().getDeviceId();
                Subject3 subject3 = new Subject3();
                subject3.setImei(imei);
                try {
                    Subject3 addSubject3 = new Subject3();
                    addSubject3.setImei(imei);
                    addSubject3.setSubject3_command(medit_command_pronounce.getText().toString());
                    addSubject3.setSubject3_command_short(medit_command_shortname.getText().toString());
                    addSubject3.setSubject3_command_title(medit_command_name.getText().toString());

                    if(!getIntent().getBooleanExtra("isEdit",false)){
                        Dao dao = getDatabaseHelper().getSubject3Dao();
                        QueryBuilder builder = dao.queryBuilder();
                        Subject3 query = (Subject3) builder.orderBy("subject3_command_order",false).where().eq("imei",imei).queryForFirst();//查询最后一条数据
                        if(query != null){
                            addSubject3.setSubject3_command_order(query.getSubject3_command_order() + 1);
                        }//添加排序

                        dao.create(addSubject3);
                    }else{
                        getDatabaseHelper().getSubject3Dao().update(addSubject3);
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }

                showTip("保存成功");
            }
        });

        medit_command_shortname.addTextChangedListener(new EditChangedListener());
        mvoice_button.setOnClickListener(new VoiceButtonOnclickListener());
    }

    class EditChangedListener implements TextWatcher {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            showTip("文本处理");
            if(medit_command_shortname.getText().toString() != ""){
                mvoice_button.setText(medit_command_shortname.getText().toString());
            }

        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    class VoiceButtonOnclickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            String voice = medit_command_pronounce.getText().toString();

           playVoiceAsync(voice);
        }
    }
}
