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

public class audioDetailActivity extends AppCompatActivity {

    protected videoInfo lectureCourseNow;
    //private aVDetail lectureAVDetail;
    protected ImageView lectureCover;
    protected TextView audioTitle, audioDescription;
    protected String link = "noLink";
    private Button backButton, buttonplay_pause;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_audio_detail);


        //隐藏标题栏
        ActionBar actionbar = getSupportActionBar();
        if (actionbar != null) {
            actionbar.hide();
        }

//        //获取已经到达 的讲座组消息数据，信息请求在 fragment_lecture_article 被发送
//        getLectureAudioDetailAsync loadCourseGroupAsync = new getLectureAudioDetailAsync();
//        loadCourseGroupAsync.execute();

        setView();

    }



    private void setView() {
        lectureCourseNow = (videoInfo) getIntent().getSerializableExtra("LectureCourse");

        lectureCover = (ImageView)findViewById(R.id.lecture_cover);
        audioTitle = (TextView)findViewById(R.id.lecture_name);
        //lectureDuration = (TextView)findViewById(R.id.lecture_duration);
        //lectureUpdateTime = (TextView)findViewById(R.id.lecture_update_time);
        audioDescription = (TextView)findViewById(R.id.lecture_description);

        //集成需要加入
        //lectureCover.setImageBitmap(lectureCourseNow.getCover());
        Bitmap receive=(Bitmap)(getIntent().getParcelableExtra("bitmap"));
        lectureCover.setImageBitmap(receive);
        audioTitle.setText(lectureCourseNow.getTitle());
        //lectureDuration.setText(lectureCourseNow.getDuration());
        //lectureUpdateTime.setText(lectureCourseNow.getUpdateTime());
        audioDescription.setText(lectureCourseNow.getDescription());

        backButton = (Button)findViewById(R.id.backButton) ;
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
                        Toast.makeText(audioDetailActivity.this, "抱歉，暂无相关资源", Toast.LENGTH_SHORT).show();

                    }
                    else
                    {

                        //测试
//                    link  = "http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4";
                        link =lectureCourseNow.getLink();
                        ThreadPoolManager.getThreadPoolManager().submitTask(new Runnable() {
                            @Override
                            public void run() {
                                try{
                                    WebSocketApplication.getWebSocketApplication().send( JsonUtil.messageTransform2Json("PlayAudio："+link));
                                }catch (Exception e){
                                    e.printStackTrace();
                                    Log.d("tag", "LoadEducationInfoAsync onPostExecute setOnItemClickListener exception");
                                }
                            }
                        });

                        Intent intent = new Intent(audioDetailActivity.this, AudioPlayActivity.class);

//                    //测试
//                    link  = "http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4";
//                    //测试
                        Log.d("lecture test",link);
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
        //Bitmap receive=(Bitmap)(getIntent().getParcelableExtra("bitmap"));
        lectureCover.setImageBitmap(receive);
        audioTitle.setText(lectureCourseNow.getTitle());
        audioDescription.setText(lectureCourseNow.getDescription());
        link = lectureCourseNow.getLink();

    }


}
