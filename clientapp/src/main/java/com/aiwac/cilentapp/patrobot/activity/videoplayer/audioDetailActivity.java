package com.aiwac.cilentapp.patrobot.activity.videoplayer;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.AsyncTask;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_video_detail);


        //隐藏标题栏
        ActionBar actionbar = getSupportActionBar();
        if (actionbar != null) {
            actionbar.hide();
        }

        setView();

    }



    private void setView() {
        lectureCourseNow = (videoInfo) getIntent().getSerializableExtra("LectureCourse");

        lectureCover = (ImageView)findViewById(R.id.lecture_cover);
        audioTitle = (TextView)findViewById(R.id.lecture_name);
        audioDescription = (TextView)findViewById(R.id.lecture_description);
        buttonPlay_pause1 = (Button)findViewById(R.id.buttonPlayPause);
        buttonPlay_pause2 = (Button)findViewById(R.id.buttonPlayPause3);

        //集成需要加入
        //lectureCover.setImageBitmap(lectureCourseNow.getCover());
        Bitmap receive=(Bitmap)(getIntent().getParcelableExtra("bitmap"));
        lectureCover.setImageBitmap(receive);
        audioTitle.setText(lectureCourseNow.getTitle());
        //lectureDuration.setText(lectureCourseNow.getDuration());
        //lectureUpdateTime.setText(lectureCourseNow.getUpdateTime());
        audioDescription.setText(lectureCourseNow.getDescription());

        backButton = (Button)findViewById(R.id.backButton) ;
        //buttonPlay_pause1 = (Button)findViewById(R.id.buttonPlayPause3);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        buttonplay_pause = (Button)findViewById(R.id.buttonPlayPause) ;
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
                        buttonplay_pause.setText("暂停播放");
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
                        MediaPlayer mediaPlayer = new MediaPlayer();
                        try{
                            mediaPlayer.setDataSource(link);
                            mediaPlayer.prepare();
                        }catch(Exception o){
                            o.printStackTrace();
                        }
                    }
                }else if(buttonplay_pause.getText().equals("暂停播放")){
                    buttonplay_pause.setText("继续播放");
                    ThreadPoolManager.getThreadPoolManager().submitTask(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                WebSocketApplication.getWebSocketApplication().send(JsonUtil.videoPlay2Json(Constant.WEBSOCKET_COMMAND_AUDIO_PLAY_CODE,"Pause"));
                            } catch (Exception e) {
                                e.printStackTrace();
                                Log.d("tag", "LoadVideoAsync onPostExecute setOnItemClickListener exception");
                            }
                        }
                    });

                }else if(buttonplay_pause.getText().equals("继续播放")){
                    buttonplay_pause.setText("暂停播放");
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
                buttonPlay_pause2.setText("机器人端播放");
                ThreadPoolManager.getThreadPoolManager().submitTask(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            WebSocketApplication.getWebSocketApplication().send(JsonUtil.videoPlay2Json(Constant.WEBSOCKET_COMMAND_AUDIO_STOP_CODE,link));
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
        receive = getURLimage(lectureCourseNow.getCover());
        lectureCover.setImageBitmap(receive);
        audioTitle.setText(lectureCourseNow.getTitle());
        audioDescription.setText(lectureCourseNow.getDescription());
        link = lectureCourseNow.getLink();

    }



}
