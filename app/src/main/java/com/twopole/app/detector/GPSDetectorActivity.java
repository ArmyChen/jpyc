package com.twopole.app.detector;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import com.baidu.mapapi.map.MyLocationData;
import com.twopole.app.base.BaseActivity;
import com.twopole.app.R;
import com.twopole.app.base.BaseOnHeaderActivity;
import com.twopole.listener.BaiduLocationListener;
import com.twopole.listener.MyOrientationListener;
import com.twopole.listener.MyOrientationListener.OnOrientationListener;

public class GPSDetectorActivity extends BaseOnHeaderActivity {


    public BaiduLocationListener myListener;


    public boolean GPS_status;
    TextView gps_status;
    @SuppressLint("ShowToast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gps_detector);
        initHeader();
        setMyTitle("GPS检测信号");
        myListener = new BaiduLocationListener(this, findViewById(R.id.scroll_gps));

        mLocationClient.registerLocationListener(myListener);    //注册监听函数
        mLocationClient.start();
        setGPSStatus();
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
        setGPSStatus();
    }

    public void setGPSStatus(){
        GPS_status = isGPSOpen();
        gps_status = (TextView) findViewById(R.id.gps_status);//gps状态
        if(GPS_status){
            gps_status.setText("已打开");
        }else{
            gps_status.setText("未打开");
        }
    }
}
