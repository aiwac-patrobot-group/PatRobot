package com.aiwac.cilentapp.patrobot.activity.videoplayer;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.aiwac.cilentapp.patrobot.R;
import com.aiwac.cilentapp.patrobot.server.WebSocketApplication;
import com.aiwac.cilentapp.patrobot.utils.JsonUtil;
import com.aiwac.robotapp.commonlibrary.task.ThreadPoolManager;
import com.aiwac.robotapp.commonlibrary.utils.LogUtil;

public class VideoAudioActivity extends AppCompatActivity {

    //视频  音频  正在播放
    private View videoView,audioView,playView;
    private Button backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //隐藏系统栏
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_video_audio);

        //隐藏标题栏
        ActionBar actionbar = getSupportActionBar();
        if (actionbar != null) {
            actionbar.hide();
        }

        // 发起向服务器 获取视音频摘要的请求
        sendeQquestLectureAllAbstract();

        setView();
    }

    public void selected(){
        videoView.setSelected(false);
        audioView.setSelected(false);
        playView.setSelected(false);
    }

    private void setView() {

        videoView = findViewById(R.id.topbar_lecture_video);
        audioView = findViewById(R.id.topbar_lecture_music);
        playView = findViewById(R.id.topbar_play_list);


        videoView.setSelected(true);

        videoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selected();
                videoView.setSelected(true);
                addFragment(new videoFragment());
            }
        });

        audioView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selected();
                audioView.setSelected(true);
                addFragment(new audioFragment());
            }
        });

        playView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                selected();
                playView.setSelected(true);
                addFragment(new playFragment());
            }
        });

        videoView.performClick();

        backButton = (Button)findViewById(R.id.backButton) ;
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void addFragment(Fragment fragment) {
        //获取到FragmentManager，在V4包中通过getSupportFragmentManager，
        //在系统中原生的Fragment是通过getFragmentManager获得的。
        FragmentManager FMs = getSupportFragmentManager();
        //2.开启一个事务，通过调用beginTransaction方法开启。
        FragmentTransaction MfragmentTransactions = FMs.beginTransaction();
        //把自己创建好的fragment创建一个对象
        //向容器内加入Fragment，一般使用add或者replace方法实现，需要传入容器的id和Fragment的实例。
        MfragmentTransactions.replace(R.id.main_content,fragment);
        //提交事务，调用commit方法提交。
        MfragmentTransactions.commit();
    }

    private void sendeQquestLectureAllAbstract()
    {

        // 视频摘要 请求
        ThreadPoolManager.getThreadPoolManager().submitTask(new Runnable() {
            @Override
            public void run() {
                try{
                    WebSocketApplication.getWebSocketApplication().send(JsonUtil.videoAbstract2Json());
                    LogUtil.d("发送视频请求");
                }catch (Exception e){
                    e.printStackTrace();
                    LogUtil.d( "LoadVideoAsync onPostExecute setOnItemClickListener exception");
                }
            }
        });

        // 音频摘要 请求
        ThreadPoolManager.getThreadPoolManager().submitTask(new Runnable() {
            @Override
            public void run() {
                try{
                    WebSocketApplication.getWebSocketApplication().send( JsonUtil.audioAbstract2Json());
                    LogUtil.d("发送音频请求");
                }catch (Exception e){
                    e.printStackTrace();
                    Log.d("tag", "LoadAudioAsync onPostExecute setOnItemClickListener exception");
                }
            }
        });

    }
}
