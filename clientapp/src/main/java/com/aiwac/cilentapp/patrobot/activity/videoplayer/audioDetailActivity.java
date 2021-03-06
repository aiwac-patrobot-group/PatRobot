package com.aiwac.cilentapp.patrobot.activity.videoplayer;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.aiwac.cilentapp.patrobot.R;
import com.aiwac.cilentapp.patrobot.bean.aVDetail;
import com.aiwac.cilentapp.patrobot.bean.videoInfo;
import com.aiwac.cilentapp.patrobot.server.WebSocketApplication;
import com.aiwac.cilentapp.patrobot.utils.JsonUtil;
import com.aiwac.robotapp.commonlibrary.common.Constant;
import com.aiwac.robotapp.commonlibrary.task.ThreadPoolManager;
import com.bumptech.glide.Glide;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import static com.aiwac.cilentapp.patrobot.utils.CacheFileUtil.getURLimage;

public class audioDetailActivity extends AppCompatActivity {

    protected videoInfo lectureCourseNow;
    //private aVDetail lectureAVDetail;
    protected ImageView lectureCover;
    protected TextView audioTitle, audioDescription;
    protected String link = "noLink";
    private Button backButton, buttonplay_pause,buttonPlay_pause1,buttonPlay_pause2;
    private Handler handler=new Handler(){
        public void handleMessage(Message msg){
            super.handleMessage(msg);
            switch(msg.what){
                case 1:   //在这里变换按钮为暂停播放
                    buttonplay_pause.setText("暂停播放");
                    break;
                case 2:   //在这里变换按钮为暂停播放
                    buttonplay_pause.setText("继续播放");
                    break;
                case 3:   //在这里变换按钮为机器人端播放
                    buttonplay_pause.setText("机器人端播放");
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);*/
        setContentView(R.layout.activity_audio_detail);


        //隐藏标题栏
        ActionBar actionbar = getSupportActionBar();
        if (actionbar != null) {
            actionbar.hide();
        }

        setView();

    }



    private void setView() {
        lectureCourseNow = (videoInfo) getIntent().getSerializableExtra("audioInfo");

        lectureCover = (ImageView)findViewById(R.id.lecture_cover);
        audioTitle = (TextView)findViewById(R.id.lecture_name);
        audioDescription = (TextView)findViewById(R.id.lecture_description);
        buttonPlay_pause1 = (Button)findViewById(R.id.buttonPlayPause);
        buttonPlay_pause2 = (Button)findViewById(R.id.buttonPlayPause3);
        audioTitle.setText(lectureCourseNow.getTitle());
        audioDescription.setText(lectureCourseNow.getDescription());
        Glide.with(audioDetailActivity.this).load(lectureCourseNow.getCover()).into(lectureCover);
        backButton = (Button)findViewById(R.id.backButton) ;
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        buttonplay_pause = (Button)findViewById(R.id.buttonPlayPause2) ;
        buttonplay_pause.setSelected(false);
        buttonplay_pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(buttonplay_pause.getText().equals("机器人端播放")){
                    if (link.equals("noLink")) {
                        Toast.makeText(audioDetailActivity.this, "抱歉，暂无相关资源", Toast.LENGTH_SHORT).show();

                    } else {
                        //测试
//                    link  = "http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4";
                        ThreadPoolManager.getThreadPoolManager().submitTask(new Runnable() {
                            @Override
                            public void run() {
                                Message msg = new Message();
                                msg.what = 1;//按钮变为暂停播放
                                handler.sendMessage(msg);
                            }
                        });
                        ThreadPoolManager.getThreadPoolManager().submitTask(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    WebSocketApplication.getWebSocketApplication().send(JsonUtil.videoPlay2Json(Constant.WEBSOCKET_COMMAND_AUDIO_PLAY_CODE,link));
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    Log.d("tag", "LoadVideoAsync onPostExecute setOnItemClickListener exception");
                                }
                            }
                        });
                        Toast.makeText(audioDetailActivity.this, "已发送到机器人端播放", Toast.LENGTH_SHORT).show();
//                        MediaPlayer mediaPlayer = new MediaPlayer();
//                        try{
//                            mediaPlayer.setDataSource(link);
//                            mediaPlayer.prepare();
//                        }catch(Exception o){
//                            o.printStackTrace();
//                        }
                    }
                }else if(buttonplay_pause.getText().equals("暂停播放")){
                    ThreadPoolManager.getThreadPoolManager().submitTask(new Runnable() {
                        @Override
                        public void run() {
                            Message msg = new Message();
                            msg.what = 2;//按钮变为继续播放
                            handler.sendMessage(msg);
                        }
                    });
                    ThreadPoolManager.getThreadPoolManager().submitTask(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                WebSocketApplication.getWebSocketApplication().send(JsonUtil.videoPlay2Json(Constant.WEBSOCKET_COMMAND_AUDIO_PAUSE_CODE,"Pause"));
                            } catch (Exception e) {
                                e.printStackTrace();
                                Log.d("tag", "LoadVideoAsync onPostExecute setOnItemClickListener exception");
                            }
                        }
                    });

                }else if(buttonplay_pause.getText().equals("继续播放")){
                    ThreadPoolManager.getThreadPoolManager().submitTask(new Runnable() {
                        @Override
                        public void run() {
                            Message msg = new Message();
                            msg.what = 1;//按钮变为暂停播放
                            handler.sendMessage(msg);
                        }
                    });
                    ThreadPoolManager.getThreadPoolManager().submitTask(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                WebSocketApplication.getWebSocketApplication().send(JsonUtil.videoPlay2Json(Constant.WEBSOCKET_COMMAND_AUDIO_CONTINUE_CODE,"Continue"));
                            } catch (Exception e) {
                                e.printStackTrace();
                                Log.d("tag", "LoadVideoAsync onPostExecute setOnItemClickListener exception");
                            }
                        }
                    });
                }
            }
        });

        buttonPlay_pause1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ( link.equals("noLink" ) )
                {
                    Toast.makeText(audioDetailActivity.this, "抱歉，暂无相关资源", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Intent intent = new Intent(audioDetailActivity.this, AudioPlayActivity.class);
                    Log.d("Video",link);
                    intent.putExtra("Link",link);
                    startActivity(intent);
                }

                buttonPlay_pause1.setSelected(false);
            }

        });
        buttonPlay_pause2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ThreadPoolManager.getThreadPoolManager().submitTask(new Runnable() {
                    @Override
                    public void run() {
                        Message msg = new Message();
                        msg.what = 3;//按钮变为暂停播放
                        handler.sendMessage(msg);
                    }
                });
                ThreadPoolManager.getThreadPoolManager().submitTask(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            WebSocketApplication.getWebSocketApplication().send(JsonUtil.videoPlay2Json(Constant.WEBSOCKET_COMMAND_AUDIO_STOP_CODE,"Stop"));
                        } catch (Exception e) {
                            e.printStackTrace();
                            Log.d("tag", "LoadVideoAsync onPostExecute setOnItemClickListener exception");
                        }
                    }
                });
            }
        });

        backButton = (Button)findViewById(R.id.backButton) ;
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        audioTitle.setText(lectureCourseNow.getTitle());
        audioDescription.setText(lectureCourseNow.getDescription());
        link = lectureCourseNow.getLink();

    }



}
