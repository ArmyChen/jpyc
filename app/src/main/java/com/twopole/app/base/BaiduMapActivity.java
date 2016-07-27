package com.twopole.app.base;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.model.inner.GeoPoint;
import com.baidu.mapapi.model.inner.Point;
import com.twopole.app.R;

import java.util.List;

@SuppressWarnings("MissingPermission")
public class BaiduMapActivity extends BaseOnHeaderActivity {
    MapView mapView;
    BaiduMap baiduMap;
    MarkerOptions markerOptions;
    Marker marker;
    private LocationManager lm;
    private double lat = 29.605009;
    private double lng = 106.315218;
    private LatLng latLng;
    private LocationManager locationManager;
    private String locationProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //初始化地图 SDK
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.activity_baidu_map);
        initHeader();
        setMyTitle("定位查看");
        //获取地理位置管理器
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        //获取所有可用的位置提供器
        List<String> providers = locationManager.getProviders(true);
        if(providers.contains(LocationManager.GPS_PROVIDER)){
            //如果是GPS
            locationProvider = LocationManager.GPS_PROVIDER;
        }else if(providers.contains(LocationManager.NETWORK_PROVIDER)){
            //如果是Network
            locationProvider = LocationManager.NETWORK_PROVIDER;
        }else{
            Toast.makeText(this, "没有可用的位置提供器", Toast.LENGTH_SHORT).show();
            return ;
        }
        //获取Location
        Location location = locationManager.getLastKnownLocation(locationProvider);
        //1 获取经度
        lat = location.getLongitude();
        //2 获取纬度值
        lng = location.getLongitude();

        //绑定XML中得 mapview 控件
        mapView = (MapView) findViewById(R.id.bmapView);
        baiduMap = mapView.getMap();

        if(isGPSOpen()){

            latLng = new LatLng(lat, lng);

        }else{
            // showTip("GPS未打开，无法获取定位");
            latLng = baiduMap.getMapStatus().target;
        }

        //准备 marker 的图片
        BitmapDescriptor bitmap = BitmapDescriptorFactory.fromResource(R.drawable.icon_marka);
        //准备 marker option 添加 marker 使用
        markerOptions = new MarkerOptions().icon(bitmap).position(latLng);
        //获取添加的 marker 这样便于后续的操作
        marker = (Marker) baiduMap.addOverlay(markerOptions);
        //对 marker 添加点击相应事件
//        baiduMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
//
//            @Override
//            public boolean onMarkerClick(Marker arg0) {
//                // TODO Auto-generated method stub
//                Toast.makeText(getApplicationContext(), "Marker被点击了！", Toast.LENGTH_SHORT).show();
//                return false;
//            }
//        });
    }
}

