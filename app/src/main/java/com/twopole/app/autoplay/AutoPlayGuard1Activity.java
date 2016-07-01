package com.twopole.app.autoplay;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.twopole.app.R;
import com.twopole.app.base.BaiduMapActivity;
import com.twopole.app.settings.RoadSettingsActivity;

public class AutoPlayGuard1Activity extends AppCompatActivity {
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_autoplay_guard_1);
        button = (Button) findViewById(R.id.btn_start);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(getBaseContext(), AutoPlayActivity.class);
                startActivity(intent);
            }
        });

    }
}
