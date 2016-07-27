package com.twopole.app.base;

import android.content.Context;
import android.os.Bundle;
import android.os.Environment;

import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.cloud.SpeechUtility;
import com.iflytek.cloud.util.ResourceUtil;
import com.twopole.app.R;
import com.twopole.app.speech.util.ApkInstaller;

import java.util.Queue;
import java.util.concurrent.PriorityBlockingQueue;

/**
 * Created by Administrator on 2016-06-16.
 */
public class BaseVoiceActivity extends BaseActivity {
    SpeechSynthesizer mTts;
    Queue queue;
    private Thread thread;
    int timeSpan = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initVoice();
        if(getSharedPreferences("mFlag", Context.MODE_PRIVATE).getInt("isFirstInstallAPP",0) == 0){
            setParams();
        }
        queue = new PriorityBlockingQueue();
    }



    // 线程类
    class BaseThreadShow implements Runnable {

        @Override
        public void run() {
            // TODO Auto-generated method stub
            while (true) {
                if(thread!=null && queue.size() > 0){
                    if(!mTts.isSpeaking()){
                        if(null != queue.peek()){
                            mTts.startSpeaking(String.valueOf(queue.poll()), mTtsListener);
                        }
                    }
                }else{
                    try {
                        thread.sleep(timeSpan * 1000);
                        thread.interrupt();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    /**
     * 间隔播放
     * @param _voice
     * @param _timeSpan
     */
    public void playVoiceAsync(String _voice, int _timeSpan){
        try {
            if(mTts == null){
                mTts = getmTts();
            }
            timeSpan = _timeSpan;
            queue.offer(_voice);

            thread = new Thread(new BaseThreadShow());
            thread.start();
        }catch (Exception e){
            gLogger.debug(getBaseContext().getPackageName() + "间隔播放 ："+ e.getMessage());
        }
    }

    /**
     * 异步播放
     * @param _voice
     */
    public void playVoiceAsync(String _voice){
        gLogger.debug("进入异步线程自动播报 ："+_voice);
        try {
            if(mTts == null){
                mTts = getmTts();
            }

            queue.offer(_voice);

            thread = new Thread(new BaseThreadShow());
            thread.start();
        }catch (Exception e){
            gLogger.debug(getBaseContext().getPackageName() + "线程自动播报 ："+ e.getMessage());
        }
    }


    /**
     * 初始化语音设置
     * @return 语音对象，用于播报
     */
    public SpeechSynthesizer getmTts() {
        initVoice();
        setParams();
        return mTts;
    }

    public void initVoice() {
        SpeechUtility.createUtility(getApplicationContext(), SpeechConstant.APPID + "=" + R.string.app_id);
        //1.创建SpeechSynthesizer对象, 第二个参数：本地合成时传InitListener
        mTts = SpeechSynthesizer.createSynthesizer(getApplicationContext(), null);

        mInstaller = new ApkInstaller(this);
        /**
         * 选择本地合成
         * 判断是否安装语记,未安装则跳转到提示安装页面
         */
        if (!SpeechUtility.getUtility().checkServiceInstalled()) {
            mInstaller.install();
        }
    }

    public void setParams() {
        //2.合成参数设置，详见《科大讯飞MSC API手册(Android)》SpeechSynthesizer 类
        mTts.setParameter(SpeechConstant.VOICE_NAME, "xiaoyan");//设置发音人
        mTts.setParameter(SpeechConstant.SPEED, "50");//设置语速
        mTts.setParameter(SpeechConstant.VOLUME, "80");//设置音量，范围0~100

        //设置合成
        if (isNetworkConnected(this)) {
            //设置使用云端引擎
            mTts.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_CLOUD);
            //设置发音人
            mTts.setParameter(SpeechConstant.VOICE_NAME, "xiaoyan");
        } else {
            // 本地设置跳转到语记中
            if (!SpeechUtility.getUtility().checkServiceInstalled()) {
                mInstaller.install();
            }else {
                SpeechUtility.getUtility().openEngineSettings(null);
            }
            //设置使用本地引擎
            mTts.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_LOCAL);
            //设置发音人
            mTts.setParameter(SpeechConstant.VOICE_NAME, "");
        }
        //设置合成音频保存位置（可自定义保存位置），保存在“./sdcard/iflytek.pcm”
        //保存在SD卡需要在AndroidManifest.xml添加写SD卡权限
        //如果不需要保存合成音频，注释该行代码
        mTts.setParameter(SpeechConstant.STREAM_TYPE, "3");
        // 设置播放合成音频打断音乐播放，默认为true
        mTts.setParameter(SpeechConstant.KEY_REQUEST_FOCUS, "true");

        // 设置音频保存路径，保存音频格式支持pcm、wav，设置路径为sd卡请注意WRITE_EXTERNAL_STORAGE权限
        // 注：AUDIO_FORMAT参数语记需要更新版本才能生效
        mTts.setParameter(SpeechConstant.AUDIO_FORMAT, "wav");
        mTts.setParameter(SpeechConstant.TTS_AUDIO_PATH, Environment.getExternalStorageDirectory()+"/msc/tts.wav");
    }

    //获取发音人资源路径
    public String getResourcePath() {
        StringBuffer tempBuffer = new StringBuffer();
        //合成通用资源
        tempBuffer.append(ResourceUtil.generateResourcePath(this, ResourceUtil.RESOURCE_TYPE.assets, "tts/common.jet"));
        tempBuffer.append(";");
        //发音人资源
        tempBuffer.append(ResourceUtil.generateResourcePath(this, ResourceUtil.RESOURCE_TYPE.assets, "tts/" + "xiaoyan" + ".jet"));
        return tempBuffer.toString();
    }

    private boolean completed;
    /**
     * 合成回调监听。
     */
    public com.iflytek.cloud.SynthesizerListener mTtsListener = new com.iflytek.cloud.SynthesizerListener() {

        @Override
        public void onSpeakBegin() {
           // showTip("播报开始");
            completed = false;
        }

        @Override
        public void onSpeakPaused() {
            mTts.pauseSpeaking();
            completed = false;
           // showTip("播报暂停");
        }

        @Override
        public void onSpeakResumed() {
             mTts.resumeSpeaking();
            completed = false;
           // showTip("播报恢复");
        }

        @Override
        public void onBufferProgress(int percent, int beginPos, int endPos,
                                     String info) {
            // 合成进度
        }

        @Override
        public void onSpeakProgress(int percent, int beginPos, int endPos) {
            // 播放进度
            completed = false;
           // showTip(String.valueOf(percent));
        }

        @Override
        public void onCompleted(SpeechError error) {
           // showTip("播报完成");
            completed = true;
            thread.interrupt();
        }

        @Override
        public void onEvent(int eventType, int arg1, int arg2, Bundle obj) {
            // 以下代码用于获取与云端的会话id，当业务出错时将会话id提供给技术支持人员，可用于查询会话日志，定位出错原因
            // 若使用本地能力，会话id为null
            //	if (SpeechEvent.EVENT_SESSION_ID == eventType) {
            //		String sid = obj.getString(SpeechEvent.KEY_EVENT_SESSION_ID);
            //		Log.d(TAG, "session id =" + sid);
            //	}
        }
    };

}
