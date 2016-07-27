package com.twopole.app.autoplay;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.model.inner.GeoPoint;
import com.baidu.mapapi.utils.DistanceUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.okhttp.Request;
import com.twopole.android.listview.HorizontalListView;
import com.twopole.app.R;
import com.twopole.app.base.BaseOnHeaderActivity;
import com.twopole.app.subject3.VoiceSubject3SimulatedLightActivity;
import com.twopole.bean.MyLocationBean;
import com.twopole.listener.BaiduLocationListener;
import com.twopole.listener.RoadLocationListener;
import com.twopole.model.autoplay.RoadDetail;
import com.twopole.provider.HorizontalListViewAdapter;
import com.twopole.utils.OkHttpClientManager;

import java.lang.reflect.Type;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class AutoPlayActivity extends BaseOnHeaderActivity{
    private HorizontalListViewAdapter hlva;
    private HorizontalListView hlv;
    private SimpleAdapter roadsAdapter;
    private ListView mListView;
    LinearLayout m_btn_light_exam;
    private List<HashMap<String, String>> dataList;
    private List<HashMap<String, String>> roadList;
    private LinearLayout m_btn_change_line;
    String[] from = {"name", "classify_id", "command_speaker"};
    int[] to = {R.id.command_name, R.id.classify_code, R.id.command_speaker};
    private AutoPlayLocationListener myListener;
    private LinearLayout m_btn_stop_play;
    private LinearLayout m_btn_gps_check;
    Thread thread;
    int road_settings = -1;
    private LinearLayout m_btn_start_play;
    private TextView mMileage;

    double f_lat;
    double f_lgn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.activity_auto_play);
        initHeader();
        setMyTitle("语音自动播报");
        hlv = (HorizontalListView) findViewById(R.id.horizontallistview1);
        m_btn_light_exam = (LinearLayout) findViewById(R.id.btn_light_exam);
        m_btn_change_line = (LinearLayout) findViewById(R.id.btn_change_line);
        m_btn_stop_play = (LinearLayout) findViewById(R.id.btn_stop_play);
        m_btn_gps_check = (LinearLayout) findViewById(R.id.btn_gps_check);
        m_btn_start_play = (LinearLayout) findViewById(R.id.btn_start_play);


        hlva = new HorizontalListViewAdapter(this, getVoiceSubject3Data(), mListener);
        hlva.notifyDataSetChanged();
        hlv.setAdapter(hlva);

        dataList = new ArrayList<>();

        mListView = (ListView) findViewById(R.id.roadmap_list);
        if (dataList.size() == 0) {
            showRoadSelection();
        }
       //
        roadsAdapter = new SimpleAdapter(this, dataList, R.layout.autoplay_road_item_vertical, from, to);
        mListView.setAdapter(roadsAdapter);


        //初始化地图 SDK
        SDKInitializer.initialize(getApplicationContext());
        initLocation();
        clickRegister();


    }

    private void clickRegister() {
        m_btn_light_exam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(getBaseContext(), VoiceSubject3SimulatedLightActivity.class);
                startActivity(intent);
            }
        });

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                HashMap<String, ?> map = (HashMap<String, ?>) parent.getItemAtPosition(position);
                playVoiceAsync(map.get("command_speaker").toString());
                showTip(map.get("road_param").toString());
            }
        });

        m_btn_change_line.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showRoadSelection();
            }
        });

        m_btn_stop_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playVoiceAsync("结束播报");
               if(thread != null){
                   thread.interrupt();
                   thread = null;
               }
                if(mLocationClient != null){
                    mLocationClient.stop();
                }
                getmTts().stopSpeaking();
                getmTts().destroy();
            }
        });

        m_btn_start_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gLogger.debug("点击自动播报");
                playVoiceAsync("开始进行自动语音播报");

                try{

                    if(thread != null){
                        thread.interrupt();
                        thread = null;
                    }
                    if(mLocationClient != null){
                        mLocationClient.stop();
                    }

                    myListener = new AutoPlayLocationListener();
                    mLocationClient.registerLocationListener(myListener);    //注册监听函数
                    mLocationClient.start();
                }catch (Exception e){
                    gLogger.debug(getBaseContext().getPackageName() + "自动播报 ："+ e.getMessage());
                }

            }
        });


    }

    private HorizontalListViewAdapter.MyHListViewClickListener mListener = new HorizontalListViewAdapter.MyHListViewClickListener() {
        @Override
        public void myOnClick(String speakMsg, View v) {
            playVoiceAsync(speakMsg);

        }
    };

    private void showRoadSelection(){
        roadList = getRoadListData();
        String[] roadArray = new String[roadList.size()];
        final String[] roadId = new String[roadList.size()];

        for (int i=0;i<roadList.size();i++){
            roadArray[i] =  roadList.get(i).get("name").toString();
            roadId[i] = roadList.get(i).get("classify_id").toString();


        }
        new AlertDialog.Builder(this)
                .setTitle("列表框")
                .setItems(roadArray, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        freshData(Integer.parseInt(roadId[which]));

                    }
                })
                .setNegativeButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        showTip("确定");
                    }
                })
                .show();
    }

    private void freshData(int id){
        dataList.clear();
        dataList = getProjectListData(id);
        roadsAdapter = new SimpleAdapter(this, dataList, R.layout.autoplay_road_item_vertical, from, to);
        mListView.setAdapter(roadsAdapter);
        roadsAdapter.notifyDataSetChanged();
    }



    @Override
    protected void onResume() {
        if(thread!= null) {
            thread.run();
        }
        if(mLocationClient != null){
            mLocationClient.start();
        }
        super.onResume();
    }


    /**
     * 监听地图
     * 循环地理位置，将地理位置的语音传入playAsync
     *
     */
    class AutoPlayLocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation bdLocation) {
            if(bdLocation != null && dataList != null){
                setGPSinfo(bdLocation);

//                double bd_lat = 29.513088;
//                double bd_lgn = 106.555696;
//                double bd_drt = 307.6;

                double bd_lat = bdLocation.getLatitude();
                double bd_lgn = bdLocation.getLongitude();
                double bd_drt = bdLocation.getDirection();
                LatLng geo2 = new LatLng(bd_lat, bd_lgn);

                if((int)bdLocation.getDirection() <= 0 && (int)bdLocation.getSpeed() <= 0){
                    //记录起点
                    if(f_lat == 0){
                        f_lat = bd_lat;//经纬度纬度
                    }
                    if(f_lgn == 0){
                        f_lgn = bd_lgn;//经度
                    }

                    //计算行驶距离
                    mMileage = (TextView) findViewById(R.id.mileage);
                    LatLng s_geo = new LatLng(f_lat, f_lgn);
                    double s_distance = DistanceUtil.getDistance(s_geo,geo2);
                    if(s_distance > 0){
                        mMileage.setText("行驶距离: "+ String.valueOf(Math.round(s_distance))+" 米");
                    }

                    //循环地理位置
                    for (HashMap<String,String> data : dataList){
                        String param  = data.get("road_param");
                        Gson gson = new Gson();
                        MyLocationBean myLocationBean = gson.fromJson(param,MyLocationBean.class);
                        double lat = myLocationBean.getLatitude();
                        double lgn = myLocationBean.getLongitude();
                        double drt = myLocationBean.getDirection();

                        LatLng geo1 = new LatLng(lat, lgn);

                        double distance = DistanceUtil.getDistance(geo1,geo2);
                        //TODO 记录参数
                        gLogger.debug(getBaseContext().getPackageName() + "打点播报AutoPlayLocationListener：参数{"+ bd_lat+","+bd_lgn+","+bd_drt+"；"+lat+","+lgn+","+drt+"}" + distance +"_"+ Math.abs(drt - bd_drt));
                        if((int) distance <= 50 && Math.abs(drt - bd_drt) < 20)
                        {
                            gLogger.debug("准备开始播报");
                            RoadDetail roadDetail = null;
                            try {
                                roadDetail = getDatabaseHelper().getRoadDetailDao().queryForId(Integer.valueOf(data.get("classify_id")));
                                gLogger.debug("根据分类查询路线信息："+ data.get("classify_id"));
                                roadDetail.setIs_speaker(1);
                                myLocationBean.setIs_speak(true);
                                gLogger.debug("转换JSON数据");
                                roadDetail.setRoad_param_status(gson.toJson(myLocationBean));
                                gLogger.debug("判断本次语音是否播报："+ roadDetail.is_speaker());
                                if(roadDetail.is_speaker() ==  0)
                                {
                                    gLogger.debug("准备进入语音播报方法playVoiceAsync："+myLocationBean.getRoad_voice());
                                    playVoiceAsync(myLocationBean.getRoad_voice());
                                    getDatabaseHelper().getRoadDetailDao().update(roadDetail);
                                    gLogger.debug("准完成播报方法playVoiceAsync"+roadDetail.getRoad_project_name());
                                }else{
                                    continue;
                                }
                            } catch (SQLException e) {
                                gLogger.error(getBaseContext().getPackageName() + "打点播报AutoPlayLocationListener："+ e.getMessage());
                            }
                        }

                    }
                }else{
                    gLogger.error(getBaseContext().getPackageName() + "打点播报AutoPlayLocationListener：没有方向和速度");
                }
            }

        }

        private void setGPSinfo(BDLocation location) {
       //     TextView loc_latitude = (TextView) findViewById(R.id.loc_latitude);//经度
       //     TextView loc_longitude = (TextView) findViewById(R.id.loc_longitude);//纬度
            TextView loc_direction = (TextView) findViewById(R.id.loc_direction);//方向
            TextView loc_radius = (TextView) findViewById(R.id.loc_radius);//精度
            TextView car_speed = (TextView) findViewById(R.id.car_speed);//速度


//            if(loc_latitude != null){
//                loc_latitude.setText("经度:"+String.valueOf(location.getLatitude()));
//                loc_latitude.setTag(String.valueOf(location.getLatitude()));
//
//            }
//            if(loc_longitude != null){
//                loc_longitude.setText("纬度:"+String.valueOf(location.getLongitude()));
//                loc_longitude.setTag(String.valueOf(location.getLongitude()));
//            }
            if(loc_direction != null){
                loc_direction.setText("方向:"+String.valueOf(location.getDirection()) + "度");
                loc_direction.setTag(String.valueOf(location.getDirection()));
            }
            if(loc_radius != null){
                loc_radius.setText("精度:"+String.valueOf(location.getRadius()) + "米");
                loc_radius.setTag(String.valueOf(location.getRadius()));
            }
            if(car_speed != null){
                car_speed.setText("速度:"+String.valueOf(location.getSpeed()) + "公里/小时");
                car_speed.setTag(String.valueOf(location.getSpeed()));
            }
        }


    }
}
