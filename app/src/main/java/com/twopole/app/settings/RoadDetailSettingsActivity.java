package com.twopole.app.settings;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.twopole.app.R;
import com.twopole.app.base.BaiduMapActivity;
import com.twopole.app.base.BaseOnHeaderActivity;
import com.twopole.app.detector.GPSDetectorActivity;
import com.twopole.listener.RoadLocationListener;
import com.twopole.model.autoplay.RoadDetail;
import com.twopole.provider.AMGridViewAdapter;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class RoadDetailSettingsActivity extends BaseOnHeaderActivity {
    LinearLayout mbtn_header_right;
    TextView mLoc_latitude;
    TextView mLoc_longitude;
    TextView mLoc_direction;
    TextView mLoc_radius;
    TextView mCar_speed;
    TextView mRoad_title;

    GridView mGridView;
    AMGridViewAdapter adapter;
    String cId;
    String speakMsg;

    ListView mListView;
    SimpleAdapter roadsAdapter;

    LinearLayout mloc_radius_about;
    ImageView micon_gps_blue;

    List<HashMap<String, String>> dataList;
    public RoadLocationListener myListener;
    int id;
    String name;
    String project_name;
    int project_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_road_detail_settings);
        initHeader();
        setMyTitle("路线详细设置");
        mGridView = (GridView) findViewById(R.id.kemu3_manually_gridview2);
        mListView = (ListView) findViewById(R.id.roadmap_list);
        mbtn_header_right = (LinearLayout) findViewById(R.id.btn_header_right);
        mLoc_latitude = (TextView) findViewById(R.id.loc_latitude);
        mLoc_longitude = (TextView) findViewById(R.id.loc_longitude);
        mLoc_direction = (TextView) findViewById(R.id.loc_direction);
        mLoc_radius = (TextView) findViewById(R.id.loc_radius);
        mloc_radius_about = (LinearLayout) findViewById(R.id.loc_radius_about);
        mCar_speed = (TextView) findViewById(R.id.car_speed);
        mRoad_title = (TextView) findViewById(R.id.road_title);
        micon_gps_blue = (ImageView) findViewById(R.id.icon_gps_blue);
        id = getIntent().getIntExtra("classify_id", 0);
        name = getIntent().getStringExtra("name");
        //mbtn_header_right.setVisibility(View.VISIBLE);

        mRoad_title.setText(name);

        mbtn_header_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(getBaseContext(), BaiduMapActivity.class);
                startActivity(intent);
            }
        });

        mloc_radius_about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(getBaseContext(), GPSDetectorActivity.class);
                startActivity(intent);
            }
        });

        adapter = new AMGridViewAdapter(this, getVoiceSubject3Data(), mListener);
        mGridView.setAdapter(adapter);

        if (isGPSOpen()) {
            micon_gps_blue.setImageResource(R.drawable.icon_gps_blue);
        } else {
            micon_gps_blue.setImageResource(R.drawable.block_red);
        }

        String[] from = {"name", "classify_id"};
        int[] to = {R.id.name, R.id.classify_code};
        dataList = new ArrayList<>();
        dataList = getProjectListData(id);
        roadsAdapter = new SimpleAdapter(this, dataList, R.layout.dedution_menu_item, from, to);
        mListView.setAdapter(roadsAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                HashMap<String, ?> map = (HashMap<String, ?>) parent.getItemAtPosition(position);
                final int cId = Integer.valueOf(String.valueOf(map.get("classify_id")));//项目id
                new AlertDialog.Builder(RoadDetailSettingsActivity.this)
                        .setTitle("提示")
                        .setMessage("确定删除？")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                deleteProject(position, cId);
                            }
                        })
                        .show();
            }


        });


        myListener = new RoadLocationListener(this, findViewById(R.id.road_gps));

        mLocationClient.registerLocationListener(myListener);    //注册监听函数
        mLocationClient.start();

    }

    private void deleteProject(int position, int cId) {
        try {
            dataList.remove(position);
            roadsAdapter.notifyDataSetChanged();
            getDatabaseHelper().getRoadDetailDao().deleteById(cId);
        } catch (Exception e) {
            showTip("删除失败");
        }
    }

    private AMGridViewAdapter.MyGridClickListener mListener = new AMGridViewAdapter.MyGridClickListener() {
        @Override
        public void myOnClick(String speakMsg, View v) {
            Button button = (Button) v;
            playVoiceAsync(speakMsg);

            HashMap<String, String> hashMap = new HashMap<>();
            hashMap.put("name", speakMsg);
            hashMap.put("classify_id", String.valueOf(button.getHint()));
            project_name = String.valueOf(button.getTag());
            project_id = Integer.parseInt(String.valueOf(button.getHint()));
            dataList.add(hashMap);
            roadsAdapter.notifyDataSetChanged();
            addRoadDetailItem();

        }
    };

    private void addRoadDetailItem() {
        try {
            RoadDetail roadDetail = new RoadDetail();
            roadDetail.setIs_default(false);
            roadDetail.setImei(getTelephonyManager().getDeviceId());
            roadDetail.setRoad_id(id);
            roadDetail.setRoad_project_name(project_name);
            roadDetail.setRoad_project_id(project_id);
            roadDetail.setIs_speaker(0);
            String latitude = mLoc_latitude.getTag().toString();
            String longitude = mLoc_longitude.getTag().toString();
            String direction = mLoc_direction.getTag().toString();
            String radius = mLoc_radius.getTag().toString();
            String speed = mCar_speed.getTag().toString();
            String jsonParam = "{\"id\":" + roadDetail.getId() + "," +
                    "\"latitude\":" + latitude + "," +
                    "\"longitude\":" + longitude + "," +
                    "\"direction\":" + direction + "," +
                    "\"radius\":" + radius + "," +
                    "\"speed\":" + speed + "," +
                    "\"address\":" + roadDetail.getRoad_project_name() + "," +
                    "\"road_name\":" + roadDetail.getRoad_project_name() + "," +
                    "\"is_speak\":" + roadDetail.is_speaker() + "," +
                    "\"road_voice\":" + roadDetail.getRoad_project_name() + "}";

            roadDetail.setRoad_param_status(jsonParam);

            getDatabaseHelper().getRoadDetailDao().create(roadDetail);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        mLocationClient.stop();// 停止定位
    }


    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        mLocationClient.start();// 開始定位
    }
}
