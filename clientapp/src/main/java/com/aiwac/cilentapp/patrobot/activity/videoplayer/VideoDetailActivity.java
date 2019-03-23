package com.aiwac.cilentapp.patrobot.activity.videoplayer;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
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

public class VideoDetailActivity extends AppCompatActivity {

    protected videoInfo lectureCourseNow;
    protected ImageView lectureCover;
    protected TextView videoTitle, videoDescription;
    private Button backButton, buttonplay_pause;
    protected String link = "noLink";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d("lecture","viodeo detail");
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_video_detail);


        //隐藏标题栏
        ActionBar actionbar = getSupportActionBar();
        if (actionbar != null) {
            actionbar.hide();
        }

        setView();
        //Vitamio.isInitialized(this);

    }

    private void setView() {
        lectureCourseNow = (videoInfo) getIntent().getSerializableExtra("videoInfo");

        lectureCover = (ImageView)findViewById(R.id.lecture_cover);
        videoTitle = (TextView)findViewById(R.id.lecture_name);
        videoDescription = (TextView)findViewById(R.id.lecture_description);
        buttonplay_pause = (Button)findViewById(R.id.buttonPlayPause) ;
        buttonplay_pause.setSelected(false);
        buttonplay_pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(buttonplay_pause.isSelected() == true)
                {
                    buttonplay_pause.setSelected(false);
                }
                else
                {
                    buttonplay_pause.setSelected(true);

                    if ( link.equals("noLink" ) )
                    {
                        Toast.makeText(VideoDetailActivity.this, "抱歉，暂无相关资源", Toast.LENGTH_SHORT).show();

                    }
                    else
                    {
                        //测试
//                    link  = "http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4";
                        ThreadPoolManager.getThreadPoolManager().submitTask(new Runnable() {
                            @Override
                            public void run() {
                                try{
                                    WebSocketApplication.getWebSocketApplication().send( JsonUtil.messageTransform2Json("PlayVideo："+link));
                                }catch (Exception e){
                                    e.printStackTrace();
                                    Log.d("tag", "LoadVideoAsync onPostExecute setOnItemClickListener exception");
                                }
                            }
                        });
                        //测试
                        Intent intent = new Intent(VideoDetailActivity.this, VideoPlayActivity.class);

                        Log.d("Video",link);
                        intent.putExtra("Link",link);
                        startActivity(intent);

                    }

                    buttonplay_pause.setSelected(false);
                }
            }
        });

        backButton = (Button)findViewById(R.id.backButton) ;
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //lectureCover.setImageBitmap(lectureCourseNow.getCover());
        Bitmap receive=(Bitmap)(getIntent().getParcelableExtra("bitmap"));
        lectureCover.setImageBitmap(receive);
        videoTitle.setText(lectureCourseNow.getTitle());
        videoDescription.setText(lectureCourseNow.getDescription());
        link = lectureCourseNow.getLink();

    }
}
