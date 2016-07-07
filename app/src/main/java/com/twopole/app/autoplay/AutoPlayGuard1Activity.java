package com.twopole.app.autoplay;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.twopole.app.R;
import com.twopole.app.base.BaiduMapActivity;
import com.twopole.app.base.BaseActivity;
import com.twopole.app.settings.RoadSettingsActivity;
import com.twopole.model.autoplay.RoadDetail;

import java.sql.SQLException;
import java.util.List;

public class AutoPlayGuard1Activity extends BaseActivity {
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_autoplay_guard_1);
        button = (Button) findViewById(R.id.btn_start);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<RoadDetail> roadDetail;
                try {
                    roadDetail = getDatabaseHelper().getRoadDetailDao().queryForAll();
                    for(RoadDetail r : roadDetail){
                        RoadDetail updateRoad =getDatabaseHelper().getRoadDetailDao().queryForId(r.getId());
                        updateRoad.setIs_speaker(0);
                        getDatabaseHelper().getRoadDetailDao().update(updateRoad);
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }


                Intent intent = new Intent();
                intent.setClass(getBaseContext(), AutoPlayActivity.class);
                startActivity(intent);
            }
        });

    }
}
