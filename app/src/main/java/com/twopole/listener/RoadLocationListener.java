package com.twopole.listener;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.twopole.app.R;

public class RoadLocationListener implements BDLocationListener {
    public StringBuffer sb;
    Context context;
    View view;
    String gpsStatus;

    public RoadLocationListener(Context _context, View _view) {
        context = _context;
        view = _view;
    }

    @Override
    public void onReceiveLocation(BDLocation location) {
        TextView loc_latitude = (TextView) view.findViewById(R.id.loc_latitude);//经度
        TextView loc_longitude = (TextView) view.findViewById(R.id.loc_longitude);//纬度
        TextView loc_direction = (TextView) view.findViewById(R.id.loc_direction);//方向
        TextView loc_radius = (TextView) view.findViewById(R.id.loc_radius);//精度
        TextView car_speed = (TextView) view.findViewById(R.id.car_speed);//速度


        if(loc_latitude != null){
            loc_latitude.setText("经度:"+String.valueOf(location.getLatitude()));
            loc_latitude.setTag(String.valueOf(location.getLatitude()));

        }
        if(loc_longitude != null){
            loc_longitude.setText("纬度:"+String.valueOf(location.getLongitude()));
            loc_longitude.setTag(String.valueOf(location.getLongitude()));
        }
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