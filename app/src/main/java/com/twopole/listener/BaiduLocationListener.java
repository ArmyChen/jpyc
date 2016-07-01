package com.twopole.listener;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.squareup.okhttp.Request;
import com.twopole.app.R;
import com.twopole.utils.OkHttpClientManager;

public class BaiduLocationListener implements BDLocationListener {
    public StringBuffer sb;
    Context context;
    View view;
    String gpsStatus;

    public BaiduLocationListener(Context _context, View _view) {
        context = _context;
        view = _view;
    }

    @Override
    public void onReceiveLocation(BDLocation location) {
        TextView black_text = (TextView) view.findViewById(R.id.network_status);//网络状态
        black_text.setText(gpsStatus);
        TextView test_result = (TextView) view.findViewById(R.id.test_result);//结果
        test_result.setText(sb.toString());
        TextView setlite_num = (TextView) view.findViewById(R.id.setlite_num);//卫星数
        setlite_num.setText(String.valueOf(location.getSatelliteNumber()));

    }

}