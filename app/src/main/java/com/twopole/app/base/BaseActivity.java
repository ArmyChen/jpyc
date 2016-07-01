package com.twopole.app.base;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.MyLocationData;
import com.iflytek.cloud.SpeechUtility;
import com.twopole.app.R;
import com.twopole.app.speech.util.ApkInstaller;
import com.twopole.bean.DeductionCategoryBean;
import com.twopole.bean.DeductionItemBean;
import com.twopole.listener.MyOrientationListener;
import com.twopole.model.User;
import com.twopole.model.autoplay.Road;
import com.twopole.model.autoplay.RoadDetail;
import com.twopole.model.subject3.DeductionCategory;
import com.twopole.model.subject3.DeductionItem;
import com.twopole.model.subject3.Subject3;
import com.twopole.model.subject3.Subject3Light;
import com.twopole.provider.DatabaseHelper;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.mindpipe.android.logging.log4j.LogConfigurator;

/**
 * Created by Administrator on 2016-06-08.
 */
public class BaseActivity extends Activity {
    Toast mToast;
    public LocationClient mLocationClient = null;
    public MyOrientationListener myOrientationListener;

    public int mXDirection;

    public double mCurrentLantitude;
    public double mCurrentLongitude;

    // 语记安装助手类
    ApkInstaller mInstaller ;

    public SharedPreferences sharedPreferences;

    public LogConfigurator logConfigurator = new LogConfigurator();
    public Logger gLogger;

    //public Logger createLogFile()
    public void configLog()
    {

        logConfigurator.setFileName(Environment.getExternalStorageDirectory() + "/jpyc_log4j.log");
        logConfigurator.setRootLevel(Level.DEBUG);
        // Set log level of a specific logger
        logConfigurator.setLevel("org.apache", Level.ERROR);
        logConfigurator.configure();

        gLogger = Logger.getLogger(logConfigurator.getClass());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mToast = Toast.makeText(this, "", Toast.LENGTH_SHORT);
        configLog();


        if (getSharedPreferences("mFlag", Context.MODE_PRIVATE).getInt("isFirstInstallAPP", 0) == 0) {
            initData();
        }
        initLocation();
        initOritationListener();

    }




    public SharedPreferences.Editor getSharedPreferences(String name) {
        sharedPreferences = getSharedPreferences(name, Context.MODE_PRIVATE); //私有数据
        SharedPreferences.Editor editor = sharedPreferences.edit();//获取编辑器
        return editor;
    }


    /**
     * 初始化用户的数据
     */
    private void initData() {

        User user = new User();
        user.setImei(getTelephonyManager().getDeviceId());
        try {
            List<User> userList = getDatabaseHelper().getUserDao().queryForMatching(user);
            if (userList.size() != 0) {
                String[] subject3Array = getResources().getStringArray(R.array.LightSimulation);
                for (String subject3 : subject3Array) {
                    Subject3Light addSubject3Light = new Subject3Light();
                    addSubject3Light.setImei(user.getImei());
                    addSubject3Light.setVoice(subject3.trim());

                    List<Subject3Light> subject3LightList = getDatabaseHelper().getSubject3LightDao().queryForMatching(addSubject3Light);
                    if (subject3LightList.size() == 0) {
                        getDatabaseHelper().getSubject3LightDao().create(addSubject3Light);
                    }
                }
            }

        } catch (SQLException e) {

            gLogger.error(getBaseContext().getPackageName() + " 初始化用户的数据 ：" + e.getMessage());
            e.printStackTrace();
        }
        /********************** 初始化科目三指令数据 **************************/
        try {
            String[] subject3CommandArray = getResources().getStringArray(R.array.Subject3Command);
            for (String subject3 : subject3CommandArray) {
                Subject3 addSubject3 = new Subject3();
                addSubject3.setImei(user.getImei());

                String[] items = subject3.trim().split("\\|");
                for (int i = 0; i < items.length; i++) {
                    addSubject3.setImei(getTelephonyManager().getDeviceId());
                    addSubject3.setSubject3_command(items[0]);
                    addSubject3.setSubject3_command_short(items[1]);
                    addSubject3.setSubject3_command_title(items[2]);
                    addSubject3.setSubject3_command_order(Integer.parseInt(items[3].trim()));
                }

                List<Subject3> subject3s = getDatabaseHelper().getSubject3Dao().queryForMatching(addSubject3);
                if (subject3s.size() == 0) {
                    getDatabaseHelper().getSubject3Dao().create(addSubject3);
                }
            }

        } catch (SQLException e) {
            gLogger.error(getBaseContext().getPackageName() + " 初始化科目三指令数据 ：" + e.getMessage());
            e.printStackTrace();
        }


        /********************** 初始化科目三扣分分类 **************************/
        try {
            String[] CategoryArray = getResources().getStringArray(R.array.Subject3DeductionCategory);
            for (String cate : CategoryArray) {
                DeductionCategory deductionCategory = new DeductionCategory();
                deductionCategory.setImei(getTelephonyManager().getDeviceId());
                String[] category = cate.trim().split("\\|");
                for (int i = 0;i<category.length;i++){
                    deductionCategory.setCategory_name(category[0]);
                    deductionCategory.setCategory_code(category[1].trim());
                }
                List<DeductionCategory> deductionCategories = getDatabaseHelper().getDeductionCategoryDao().queryForMatching(deductionCategory);
                if (deductionCategories.size() == 0) {
                    getDatabaseHelper().getDeductionCategoryDao().create(deductionCategory);
                }
            }
        } catch (SQLException e) {
            gLogger.error(getBaseContext().getPackageName() + " 初始化科目三扣分分类 ：" + e.getMessage());
            e.printStackTrace();
        }

        /********************** 初始化科目三扣分项目 **************************/
        try {
            String[] deductionItemArray = getResources().getStringArray(R.array.Subject3DeductionItem);
            for (String deductionItem : deductionItemArray) {
                DeductionItem deductionItem1 = new DeductionItem();
                deductionItem1.setImei(getTelephonyManager().getDeviceId());
                String[] items = deductionItem.trim().split("\\|");
                for (int i = 0;i<items.length;i++){
                    deductionItem1.setItem_name(items[0]);
                    deductionItem1.setItem_reason(items[1]);
                    deductionItem1.setItem_score(items[2]);
                    deductionItem1.setItem_category_code(items[3].trim());
                }
                List<DeductionItem> deductionItems = getDatabaseHelper().getDeductionItemDao().queryForMatching(deductionItem1);
                if (deductionItems.size() == 0) {
                    getDatabaseHelper().getDeductionItemDao().create(deductionItem1);
                }
            }
        } catch (SQLException e) {
            gLogger.error(getBaseContext().getPackageName() + " 初始化科目三扣分项目 ：" + e.getMessage());
            e.printStackTrace();
        }

    }

    public DatabaseHelper getDatabaseHelper() {
        DatabaseHelper helper = DatabaseHelper.getHelper(this);
        return helper;
    }

    public TelephonyManager getTelephonyManager() {
        TelephonyManager telephonyManager = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
        return telephonyManager;
    }

    public void showTip(final String str) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mToast.setText(str);
                mToast.show();
            }
        });
    }

    /**
     * 判断网络是否连接
     *
     * @param context
     * @return
     */
    public boolean isNetworkConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
            if (mNetworkInfo != null) {
                return mNetworkInfo.isAvailable();
            }
        }
        return false;
    }

    /**
     * 判断GPS是否连接
     *
     * @return
     */
    public boolean isGPSOpen() {
        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        boolean GPS_status = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);//获得手机是不是设置了GPS开启状态true：gps开启，false：GPS未开启
        return GPS_status;
    }

    /**
     * 获取科目三语音播放列表
     *
     * @return
     */
    public List<Map<String, Object>> getVoiceSubject3Data() {
        List<Map<String, Object>> data_list = new ArrayList<>();
        try {
            List<Subject3> subject3List = getDatabaseHelper().getSubject3Dao().queryBuilder().orderBy("subject3_command_order", true).query();
            if (subject3List.size() > 0) {
                for (int i = 0; i < subject3List.size(); i++) {
                    Map<String, Object> map = new HashMap<>();
                    map.put("voice_subject3_main_button", subject3List.get(i).getSubject3_command_short());
                    map.put("voice_subject3_main_text", subject3List.get(i).getSubject3_command_title());
                    map.put("voice_subject3_speaker", subject3List.get(i).getSubject3_command());
                    map.put("voice_subject3_id", subject3List.get(i).getId());
                    data_list.add(map);
                }
            }

        } catch (SQLException e) {
            gLogger.error(getBaseContext().getPackageName() + " 获取科目三语音播放列表 ：" + e.getMessage());
            e.printStackTrace();
        }
        return data_list;
    }

    /**
     * 获取扣分列表
     *
     * @return
     */
    public ArrayList<DeductionItemBean> getDeductionCategoryData() {


        ArrayList<DeductionItemBean> data = new ArrayList<>();
        try {
            List<com.twopole.model.subject3.DeductionCategory> deductionCategoryList = getDatabaseHelper().getDeductionCategoryDao().queryForAll();
            if (deductionCategoryList.size() != 0) {
                for (int i = 0; i < deductionCategoryList.size(); i++) {
                    DeductionItemBean data1 = new DeductionItemBean();
                    data1.text = deductionCategoryList.get(i).getCategory_name();
                    data1.type = DeductionItemBean.GROUP;
                    data1.categoryCode = deductionCategoryList.get(i).getCategory_code();
                    data.add(data1);

                    List<DeductionItem> deductionItemList = getDatabaseHelper().getDeductionItemDao().queryBuilder().where().eq("item_category_code", deductionCategoryList.get(i).getCategory_code()).query();
                    if (deductionItemList.size() > 0) {
                        for (int j = 0; j < deductionItemList.size(); j++) {

                            DeductionCategoryBean contacts1 = new DeductionCategoryBean();
                            contacts1.id = deductionItemList.get(j).getId();
                            contacts1.name = deductionItemList.get(j).getItem_name();
                            contacts1.reason = deductionItemList.get(j).getItem_reason();
                            contacts1.score = deductionItemList.get(j).getItem_score();
                            contacts1.sort_key_primary = deductionItemList.get(j).getItem_name();

                            DeductionItemBean child = new DeductionItemBean();
                            child.type = DeductionItemBean.CHILD;
                            child.text = deductionItemList.get(j).getItem_name();
                            child.categoryCode = deductionItemList.get(j).getItem_category_code();
                            child.contacts = contacts1;


                            data.add(child);
                        }
                    }
                }
            } else {
                showTip("暂时还未添加扣分项目");
            }

        } catch (SQLException e) {
            gLogger.error(getBaseContext().getPackageName() + " 获取扣分列表 ：" + e.getMessage());
        }


        return data;
    }

    /**
     * 获取扣分项目
     *
     * @param list
     * @return
     */
    public List<? extends Map<String, ?>> getItemData(List<DeductionItem> list) {
        List<HashMap<String, Object>> data = new ArrayList<>();
        for (DeductionItem deductionItem : list) {
            HashMap<String, Object> map = new HashMap<>();
            map.put("name", deductionItem.getItem_reason() + "扣 " + deductionItem.getItem_score() + "分");
            data.add(map);
        }
        return data;
    }

    /**
     * 获取扣分项目分类列表
     *
     * @return
     */
    public List<? extends Map<String, ?>> getCateDate() {
        List<HashMap<String, Object>> data = new ArrayList<>();
        try {
            List<com.twopole.model.subject3.DeductionCategory> deductionCategory = getDatabaseHelper().getDeductionCategoryDao().queryForAll();
            for (com.twopole.model.subject3.DeductionCategory category : deductionCategory) {
                HashMap<String, Object> map = new HashMap<>();
                map.put("classify_code", category.getCategory_code());
                map.put("name", category.getCategory_name());
                data.add(map);
            }
        } catch (SQLException e) {
            gLogger.error(getBaseContext().getPackageName() + " 获取扣分项目分类列表 ：" + e.getMessage());
            e.printStackTrace();
        }

        return data;
    }

    /**
     * 获取科目三灯光列表
     *
     * @return
     */
    public ArrayList<HashMap<String, String>> getVoiceSubject3SimulatedLightData() {
        ArrayList<HashMap<String, String>> list = new ArrayList<>();
        Subject3Light subject3Light = new Subject3Light();
        subject3Light.setImei(getTelephonyManager().getDeviceId());
        List<Subject3Light> subject3LightList;
        try {
            subject3LightList = getDatabaseHelper().getSubject3LightDao().queryForMatching(subject3Light);
            for (int i = 0; i < subject3LightList.size(); i++) {
                HashMap<String, String> map = new HashMap<>();
                map.put("s_id", String.valueOf(subject3LightList.get(i).getId()));
                map.put("s_name", "灯光指令" + (i + 1));
                map.put("s_content", subject3LightList.get(i).getVoice());
                list.add(map);
            }
        } catch (SQLException e) {
            gLogger.error(getBaseContext().getPackageName() + " 获取科目三灯光列表 ：" + e.getMessage());
            Log.d(getLocalClassName(), e.getMessage());
        }

        return list;
    }

    /**
     * 获取灯光指令列表
     *
     * @return
     */
    public ArrayList<HashMap<String, String>> getVoiceSubject3SimulatedLightSettingData() {
        ArrayList<HashMap<String, String>> list = new ArrayList<>();
        Subject3Light subject3Light = new Subject3Light();
        subject3Light.setImei(getTelephonyManager().getDeviceId());
        List<Subject3Light> subject3LightList = null;
        try {
            subject3LightList = getDatabaseHelper().getSubject3LightDao().queryForMatching(subject3Light);
            for (int i = 0; i < subject3LightList.size(); i++) {
                HashMap<String, String> map = new HashMap<>();
                map.put("id", String.valueOf(subject3LightList.get(i).getId()));
                map.put("l_name", "灯光指令" + (i + 1));
                map.put("l_content", subject3LightList.get(i).getVoice());
                list.add(map);
            }
        } catch (SQLException e) {
            gLogger.error(getBaseContext().getPackageName() + " 获取灯光指令列表 ：" + e.getMessage());
            Log.d(getLocalClassName(), e.getMessage());
        }

        return list;
    }

    /**
     * 获取线路数据
     *
     * @return
     */
    public List<HashMap<String, String>> getRoadListData() {
        List<HashMap<String, String>> data = new ArrayList<>();
        try {
            List<Road> roadList = getDatabaseHelper().getRoadDao().queryForAll();
            if (roadList.size() > 0) {
                for (int i = 0; i < roadList.size(); i++) {
                    HashMap<String, String> map = new HashMap<>();
                    Subject3 subject3s = getDatabaseHelper().getSubject3Dao().queryForId(roadList.get(i).getId());
                    map.put("name", roadList.get(i).getRoad_name());
                    map.put("classify_id", String.valueOf(roadList.get(i).getId()));
                    if (subject3s != null) {
                        map.put("command_speaker", String.valueOf(subject3s.getSubject3_command()));
                    }
                    data.add(map);
                }
            }
            return data;
        } catch (SQLException e) {
            gLogger.error(getBaseContext().getPackageName() + " 获取线路数据 ：" + e.getMessage());
            Log.d(getLocalClassName(), e.getMessage());
        }
        return data;
    }

    /**
     * 获取线路数据
     *
     * @return
     */
    public List<HashMap<String, String>> getProjectListData(int id) {
        List<HashMap<String, String>> data = new ArrayList<>();
        try {
            List<RoadDetail> roadList = getDatabaseHelper().getRoadDetailDao().queryBuilder().where().eq("road_id", id).query();
            if (roadList.size() > 0) {
                for (int i = 0; i < roadList.size(); i++) {
                    HashMap<String, String> map = new HashMap<>();
                    Subject3 subject3s = getDatabaseHelper().getSubject3Dao().queryForId(roadList.get(i).getRoad_project_id());


                    map.put("classify_id", String.valueOf(roadList.get(i).getId()));
                    map.put("name", String.valueOf(roadList.get(i).getRoad_project_name()));
                    map.put("road_param", String.valueOf(roadList.get(i).getRoad_param_status()));
                    if (subject3s != null) {
                        map.put("command_speaker", String.valueOf(subject3s.getSubject3_command()));


                    }
                    data.add(map);
                }
            }
            return data;
        } catch (SQLException e) {
            gLogger.error(getBaseContext().getPackageName() + " 获取线路数据 ：" + e.getMessage());
            Log.d(getLocalClassName(), e.getMessage());
        }
        return data;
    }

    public void initLocation() {
        //监听地图信息
        mLocationClient = new LocationClient(getApplicationContext());     //声明LocationClient类
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);//可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        option.setCoorType("bd09ll");//可选，默认gcj02，设置返回的定位结果坐标系
        int span = 5000;
        option.setScanSpan(span);//可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
        option.setIsNeedAddress(true);//可选，设置是否需要地址信息，默认不需要
        option.setOpenGps(true);//可选，默认false,设置是否使用gps
        option.setLocationNotify(true);//可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
        option.setIsNeedLocationDescribe(true);//可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
        option.setIsNeedLocationPoiList(true);//可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
        option.setIgnoreKillProcess(false);//可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
        option.SetIgnoreCacheException(false);//可选，默认false，设置是否收集CRASH信息，默认收集
        option.setEnableSimulateGps(false);//可选，默认false，设置是否需要过滤gps仿真结果，默认需要
        mLocationClient.setLocOption(option);
    }

    /**
     * 初始化方向传感器
     */
    public void initOritationListener() {
        myOrientationListener = new MyOrientationListener(
                getApplicationContext());
        myOrientationListener
                .setOnOrientationListener(new MyOrientationListener.OnOrientationListener() {
                    @Override
                    public void onOrientationChanged(float x) {
                        mXDirection = (int) x;

                        // 构造定位数据
                        MyLocationData locData = new MyLocationData.Builder()
                                .accuracy(30)
                                // 此处设置开发者获取到的方向信息，顺时针0-360
                                .direction(mXDirection)
                                .latitude(mCurrentLantitude)
                                .longitude(mCurrentLongitude).build();
                        String direction = String.valueOf(locData.direction);


                    }
                });
    }

    private String deductionSQL = "";
}
