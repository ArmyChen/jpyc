package com.twopole.app.treasure;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.twopole.app.base.BaseActivity;
import com.twopole.app.R;
import com.twopole.app.base.BaseOnHeaderActivity;
import com.twopole.app.base.WebViewActivity;

public class TreasureActivity extends BaseOnHeaderActivity {
    LinearLayout mtreasure_btn_baoming;
    LinearLayout mtreasure_btn_xinshou;
    LinearLayout mbtn_connect_us;
    LinearLayout mcommon_question;
    LinearLayout mtreasure_btn_biaozhi;
    LinearLayout mtreasure_btn_law;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_treasure);
        initHeader();
        setMyTitle("发现");
        mtreasure_btn_baoming = (LinearLayout) findViewById(R.id.treasure_btn_baoming);
        mtreasure_btn_xinshou = (LinearLayout) findViewById(R.id.treasure_btn_xinshou);
        mbtn_connect_us = (LinearLayout) findViewById(R.id.btn_connect_us);
        mcommon_question = (LinearLayout) findViewById(R.id.common_question);
        mtreasure_btn_biaozhi = (LinearLayout) findViewById(R.id.treasure_btn_biaozhi);
        mtreasure_btn_law = (LinearLayout) findViewById(R.id.  treasure_btn_law);

        mtreasure_btn_baoming.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(getBaseContext(),WebViewActivity.class);
                intent.putExtra("loadUrl","http://dwz.cn/3zPjSQ");
                startActivity(intent);
            }
        });
        mtreasure_btn_xinshou.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(getBaseContext(),WebViewActivity.class);
                intent.putExtra("loadUrl","http://dwz.cn/3zPi7Z");
                startActivity(intent);
            }
        });

        mbtn_connect_us.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(getBaseContext(),WebViewActivity.class);
                intent.putExtra("loadUrl","http://www.2pole.com/ContactUs");
                startActivity(intent);
            }
        });

        mcommon_question.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(getBaseContext(),WebViewActivity.class);
                intent.putExtra("loadUrl","http://dwz.cn/3zPqBo");
                startActivity(intent);
            }
        });

        mtreasure_btn_biaozhi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(getBaseContext(),WebViewActivity.class);
                intent.putExtra("loadUrl","http://dwz.cn/3zPubb");
                startActivity(intent);
            }
        });

        mtreasure_btn_law.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(getBaseContext(),WebViewActivity.class);
                intent.putExtra("loadUrl","http://dwz.cn/3zPxwY");
                startActivity(intent);
            }
        });

        initHeader();
    }
}
