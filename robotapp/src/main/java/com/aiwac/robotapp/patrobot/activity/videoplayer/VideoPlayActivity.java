package com.aiwac.robotapp.patrobot.activity.videoplayer;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.media.AudioManager;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.Pair;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.aiwac.robotapp.commonlibrary.bean.MessageEvent;
import com.aiwac.robotapp.commonlibrary.common.Constant;
import com.aiwac.robotapp.commonlibrary.utils.LogUtil;
import com.aiwac.robotapp.patrobot.R;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import io.vov.vitamio.MediaPlayer;
import io.vov.vitamio.utils.ScreenResolution;
import io.vov.vitamio.utils.StringUtils;
import io.vov.vitamio.widget.VideoView;

public class VideoPlayActivity extends AppCompatActivity implements View.OnClickListener{

    public static final int UPDATE_PALY_TIME = 0x01;//更新播放时间
    public static final int UPDATE_TIME = 800;
    public static final int HIDE_CONTROL_BAR = 0x02;//隐藏控制条
    public static final int HIDE_TIME = 5000;//隐藏控制条时间
    public static final int SHOW_CENTER_CONTROL = 0x03;//显示中间控制
    public static final int SHOW_CONTROL_TIME = 1000;

    public final static int ADD_FLAG = 1;
    public final static int SUB_FLAG = -1;

    private FrameLayout mVideoLayout;
    public static VideoView mVideoView1;
    public static RelativeLayout mControlTop;//顶部控制栏
    public static RelativeLayout mControlBottom;//底部控制栏
    private ImageView mIvBack;//返回
    public static  ImageView mIvPlay;//播放/暂停
    public static TextView mTvTime;//时间显示
    public static SeekBar mSeekBar;//进度条
    private ImageView mIvIsFullScreen;//是否全屏
    private RelativeLayout mProgressBar;//缓冲提示
    public static LinearLayout mControlCenter;
    private ImageView mIvControl;
    private TextView mTvControl;
    private TextView mTvFast;//快进

    private int mScreenWidth = 0;//屏幕宽度
    private boolean mIsFullScreen = false;//是否为全屏
    public static long mVideoTotalTime = 0;//视频总时间
    private boolean mIntoSeek = false;//是否 快进/快退
    private long mSeek = 0;//快进的进度
    private boolean mIsFastFinish = false;

    private GestureDetector mGestureDetector;
    private GestureDetector.SimpleOnGestureListener mSimpleOnGestureListener;

    private AudioManager mAudioManager;
    private int mMaxVolume;//最大声音
    private int mShowVolume;//声音
    private int mShowLightness;//亮度

    private String mPlayUrl;

    public static Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case UPDATE_PALY_TIME:
                    long currentPosition = mVideoView1.getCurrentPosition();
                    if (currentPosition <= mVideoTotalTime) {
                        //更新时间显示
                        mTvTime.setText(sec2time(currentPosition)+"/"+sec2time(mVideoTotalTime));
                        //更新进度条
                        int progress = (int)((currentPosition * 1.0 / mVideoTotalTime) * 100);
                        mSeekBar.setProgress(progress);
                        mHandler.sendEmptyMessageDelayed(UPDATE_PALY_TIME, UPDATE_TIME);
                    }
                    break;
                case HIDE_CONTROL_BAR:
                    hideControlBar();
                    break;
                case SHOW_CENTER_CONTROL:
                    mControlCenter.setVisibility(View.GONE);
                    break;
            }
        }
    };

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void Event(MessageEvent messageEvent) {
        if(messageEvent.getTo().equals(Constant.WEBSOCKET_COMMAND_VIDEO_PAUSE)){
            LogUtil.d("video pause");
            /*if (mVideoView1.isPlaying()) {
                mVideoView1.pause();
                mIvPlay.setImageResource(R.drawable.video_play);
                mHandler.removeMessages(UPDATE_PALY_TIME);
                mHandler.removeMessages(HIDE_CONTROL_BAR);
                showControlBar();
            }*/
        }else  if(messageEvent.getTo().equals(Constant.WEBSOCKET_COMMAND_AUDIO_PAUSSE)){
            LogUtil.d("audio pause");
            /*if (mVideoView1.isPlaying()) {
                mVideoView1.pause();
                mIvPlay.setImageResource(R.drawable.video_play);
                mHandler.removeMessages(UPDATE_PALY_TIME);
                mHandler.removeMessages(HIDE_CONTROL_BAR);
                showControlBar();
            }*/
        }else  if(messageEvent.getTo().equals(Constant.WEBSOCKET_COMMAND_VIDEO_STOP)){
            LogUtil.d("video stop");
            /*if (mIsFullScreen) {
                if (mVideoView1.isPlaying()) {
                    mHandler.removeMessages(HIDE_CONTROL_BAR);
                    mHandler.sendEmptyMessageDelayed(HIDE_CONTROL_BAR, HIDE_TIME);
                }
                setupUnFullScreen();
            } else {
                finish();
            }
            finish();*/

        }else  if(messageEvent.getTo().equals(Constant.WEBSOCKET_COMMAND_AUDIO_STOP)){
            LogUtil.d("audio stop");
            /*if (mIsFullScreen) {
                if (mVideoView1.isPlaying()) {
                    mHandler.removeMessages(HIDE_CONTROL_BAR);
                    mHandler.sendEmptyMessageDelayed(HIDE_CONTROL_BAR, HIDE_TIME);
                }
                setupUnFullScreen();
            } else {
                finish();
            }

            finish();*/

        }else if(messageEvent.getTo().equals(Constant.WEBSOCKET_COMMAND_VIDEO_CONTINUE)) {
            LogUtil.d("video play");
           /* mVideoView1.start();
            mIvPlay.setImageResource(R.drawable.video_pause);
            mHandler.sendEmptyMessage(UPDATE_PALY_TIME);
            mHandler.sendEmptyMessageDelayed(HIDE_CONTROL_BAR, HIDE_TIME);*/
        }else if(messageEvent.getTo().equals(Constant.WEBSOCKET_COMMAND_AUDIO_CONTINUE)){
            LogUtil.d("audio play");
            /*mVideoView1.start();
            mIvPlay.setImageResource(R.drawable.video_pause);
            mHandler.sendEmptyMessage(UPDATE_PALY_TIME);
            mHandler.sendEmptyMessageDelayed(HIDE_CONTROL_BAR, HIDE_TIME);*/
        }

    }

    /**
     * 秒转化为常见格式
     * @param time
     * @return
     */
    public static String sec2time(long time) {
        String hms = StringUtils.generateTime(time);
        return hms;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_video_play);


        //隐藏标题栏
        ActionBar actionbar = getSupportActionBar();
        if (actionbar != null) {
            actionbar.hide();
        }

        mPlayUrl= (String) getIntent().getStringExtra("Link");
        //mPlayUrl = "http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4";
        Log.d("lecture", "link: "+mPlayUrl);


        mVideoLayout = (FrameLayout) findViewById(R.id.video_layout);
        mVideoView1 = (VideoView) findViewById(R.id.videoview);
        mControlTop = (RelativeLayout) findViewById(R.id.control_top);
        mControlBottom = (RelativeLayout) findViewById(R.id.control_bottom);
        mIvBack = (ImageView) findViewById(R.id.iv_back);
        mIvPlay = (ImageView) findViewById(R.id.iv_play);
        mTvTime = (TextView) findViewById(R.id.tv_time);
        mSeekBar = (SeekBar) findViewById(R.id.seekbar);
        //mIvIsFullScreen = (ImageView) findViewById(R.id.iv_is_fullscreen);
        mProgressBar = (RelativeLayout) findViewById(R.id.progressbar);
        mControlCenter = (LinearLayout) findViewById(R.id.control_center);
        mIvControl = (ImageView) findViewById(R.id.iv_control_img);
        mTvControl = (TextView) findViewById(R.id.tv_control);
        mTvFast = (TextView) findViewById(R.id.tv_fast);
        mIvBack.setOnClickListener(this);
        mIvPlay.setOnClickListener(this);
        // mIvIsFullScreen.setOnClickListener(this);

        init();


        //注册消息
        EventBus.getDefault().register(this);
    }


    private void init() {
        //获取屏幕宽度
        Pair<Integer, Integer> screenPair = ScreenResolution.getResolution(this);
        mScreenWidth = screenPair.first;
        //播放网络资源
        mVideoView1.setVideoPath(mPlayUrl);
        //设置缓冲大小为2M
        mVideoView1.setBufferSize(1024*512);

        initVolumeWithLight();
        addVideoViewListener();
        addSeekBarListener();
        addTouchListener();

    }

    /**
     * 初始化声音和亮度
     */
    private void initVolumeWithLight() {
        mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        mMaxVolume = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        mShowVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC) * 100 / mMaxVolume;

        mShowLightness = getScreenBrightness();
    }

    /**
     * 获得当前屏幕亮度值 0--255
     */
    private int getScreenBrightness() {
        int screenBrightness = 255;
        try {
            screenBrightness = Settings.System.getInt(getContentResolver(),
                    Settings.System.SCREEN_BRIGHTNESS);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return screenBrightness;
    }

    /**
     * 为VideoView添加监听
     */
    private void addVideoViewListener() {
        //准备播放完成
        mVideoView1.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                //获取播放总时长
                mVideoTotalTime = mVideoView1.getDuration();
            }
        });

        //正在缓冲
        mVideoView1.setOnBufferingUpdateListener(new MediaPlayer.OnBufferingUpdateListener() {
            @Override
            public void onBufferingUpdate(MediaPlayer mp, int percent) {
                if (!mIntoSeek)
                    mProgressBar.setVisibility(View.VISIBLE);

                mHandler.removeMessages(UPDATE_PALY_TIME);
                mHandler.removeMessages(HIDE_TIME);
                mIvPlay.setImageResource(R.drawable.video_play);

                if (mVideoView1.isPlaying())
                    mVideoView1.pause();
            }
        });

        mVideoView1.setOnInfoListener(new MediaPlayer.OnInfoListener() {
            @Override
            public boolean onInfo(MediaPlayer mp, int what, int extra) {

                switch (what) {
                    //缓冲完成
                    case MediaPlayer.MEDIA_INFO_BUFFERING_END:
                        mIvPlay.setImageResource(R.drawable.video_pause);
                        mHandler.removeMessages(UPDATE_PALY_TIME);
                        mHandler.removeMessages(HIDE_CONTROL_BAR);
                        mHandler.sendEmptyMessage(UPDATE_PALY_TIME);
                        mHandler.sendEmptyMessageDelayed(HIDE_CONTROL_BAR, HIDE_TIME);
                        mProgressBar.setVisibility(View.GONE);

                        if (!mVideoView1.isPlaying())
                            mVideoView1.start();
                        break;
                }

                return true;
            }
        });

        //视频播放出错
        mVideoView1.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {

                if (what == MediaPlayer.MEDIA_ERROR_UNKNOWN) {
                    Toast.makeText(VideoPlayActivity.this, "该视频无法播放！", Toast.LENGTH_SHORT).show();
                }

                return true;
            }
        });

        //视频播放完成
        mVideoView1.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                Toast.makeText(VideoPlayActivity.this, "视频播放完成", Toast.LENGTH_SHORT).show();
                mHandler.removeMessages(UPDATE_PALY_TIME);
                mHandler.removeMessages(HIDE_CONTROL_BAR);
                finish();
            }
        });
    }

    /**
     * 为SeekBar添加监听
     */
    private void addSeekBarListener() {
        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                long progress = (long) (seekBar.getProgress()*1.0/100*mVideoView1.getDuration());
                mTvTime.setText(sec2time(progress)+"/"+sec2time(mVideoTotalTime));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                mHandler.removeMessages(UPDATE_PALY_TIME);
                mHandler.removeMessages(HIDE_CONTROL_BAR);
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                long progress = (long) (seekBar.getProgress()*1.0/100*mVideoView1.getDuration());
                mVideoView1.seekTo(progress);
                mHandler.sendEmptyMessage(UPDATE_PALY_TIME);
            }
        });
    }

    /**
     * 添加手势操作
     */
    private void addTouchListener() {
        mSimpleOnGestureListener = new GestureDetector.SimpleOnGestureListener() {

            //滑动操作
            @Override
            public boolean onScroll(MotionEvent e1, MotionEvent e2,
                                    float distanceX, float distanceY) {
//                if (!mIsFullScreen)//非全屏不进行手势操作
//                    return false;

                float x1 = e1.getX();
                float y1 = e1.getY();
                float x2 = e2.getX();
                float y2 = e2.getY();
                float absX = Math.abs(x1 - x2);
                float absY = Math.abs(y1 - y2);

                float absDistanceX = Math.abs(distanceX);// distanceX < 0 从左到右
                float absDistanceY = Math.abs(distanceY);// distanceY < 0 从上到下

                // Y方向的距离比X方向的大，即 上下 滑动
                if (absDistanceX < absDistanceY && !mIntoSeek) {
                    if (distanceY > 0) {//向上滑动
                        if (x1 >= mScreenWidth*0.65) {//右边调节声音
                            changeVolume(ADD_FLAG);
                        } else {//调节亮度
                            changeLightness(ADD_FLAG);
                        }
                    } else {//向下滑动
                        if (x1 >= mScreenWidth*0.65) {
                            changeVolume(SUB_FLAG);
                        } else {
                            changeLightness(SUB_FLAG);
                        }
                    }

                } else {// X方向的距离比Y方向的大，即 左右 滑动
                    if (absX > absY) {
                        mIntoSeek = true;
                        onSeekChange(x1, x2);
                        return true;
                    }
                }

                return false;
            }

            //双击事件，有的视频播放器支持双击播放暂停，可从这实现
            @Override
            public boolean onDoubleTap(MotionEvent e) {
                return super.onDoubleTap(e);
            }

            //单击事件
            @Override
            public boolean onSingleTapConfirmed(MotionEvent e) {

                if (!mVideoView1.isPlaying())
                    return false;

                if (mControlBottom.getVisibility() == View.VISIBLE) {
                    mHandler.removeMessages(HIDE_CONTROL_BAR);
                    hideControlBar();
                } else {
                    showControlBar();
                    mHandler.sendEmptyMessageDelayed(HIDE_CONTROL_BAR, HIDE_TIME);
                }

                return true;
            }
        };
        mGestureDetector = new GestureDetector(this, mSimpleOnGestureListener);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (mGestureDetector != null)
            mGestureDetector.onTouchEvent(event);

        if (event.getAction() == MotionEvent.ACTION_UP) {//手指抬起
            mTvFast.setVisibility(View.GONE);
            mIntoSeek = false;
            if (mIsFastFinish) {
                mVideoView1.seekTo(mSeek);
                mIsFastFinish = false;
            }
        }

        return super.onTouchEvent(event);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                if (mIsFullScreen) {
                    if (mVideoView1.isPlaying()) {
                        mHandler.removeMessages(HIDE_CONTROL_BAR);
                        mHandler.sendEmptyMessageDelayed(HIDE_CONTROL_BAR, HIDE_TIME);
                    }
                    setupUnFullScreen();
                } else {
                    finish();
                }

                finish();
                break;

            case R.id.iv_play:
                if (mVideoView1.isPlaying()) {
                    mVideoView1.pause();
                    mIvPlay.setImageResource(R.drawable.video_play);
                    mHandler.removeMessages(UPDATE_PALY_TIME);
                    mHandler.removeMessages(HIDE_CONTROL_BAR);
                    showControlBar();
                } else {
                    mVideoView1.start();
                    mIvPlay.setImageResource(R.drawable.video_pause);
                    mHandler.sendEmptyMessage(UPDATE_PALY_TIME);
                    mHandler.sendEmptyMessageDelayed(HIDE_CONTROL_BAR, HIDE_TIME);
                }
                break;

//            case R.id.iv_is_fullscreen:
//                if (mIsFullScreen) {
//                    setupUnFullScreen();
//                } else {
//                    setupFullScreen();
//                }
//                if (mVideoView1.isPlaying()) {
//                    mHandler.removeMessages(HIDE_CONTROL_BAR);
//                    mHandler.sendEmptyMessageDelayed(HIDE_CONTROL_BAR, HIDE_TIME);
//                }
//                break;
        }
    }

    /**
     * 左右滑动距离计算快进/快退时间
     */
    private void onSeekChange(float x1, float x2) {

        long currentPosition = mVideoView1.getCurrentPosition();
        long seek=0;

        if (x1 - x2 > 200) {//向左滑
            if (currentPosition < 10000) {
                currentPosition = 0;
                seek = 0;
                setFashText(seek);
                mVideoView1.seekTo(currentPosition);
            } else {
                float ducation = (x1 - x2);
                mVideoView1.seekTo(currentPosition - (long)ducation*10);
                seek = currentPosition - (long)ducation*10;
                setFashText(seek);
            }
        } else if (x2 - x1 > 200) { //向右滑动
            if (currentPosition+10000>mVideoView1.getDuration()) {
                currentPosition = mVideoView1.getDuration();
                mVideoView1.seekTo(currentPosition);
                seek = currentPosition;
                setFashText(seek);
            } else {
                float ducation = x2 - x1;
                mVideoView1.seekTo(currentPosition+(long)ducation*10);
                seek = currentPosition+(long)ducation*10;
                setFashText(seek);
            }
        }

    }

    private void setFashText(long seek) {
        String showTime = StringUtils.generateTime(seek) +
                "/" + StringUtils.generateTime(mVideoView1.getDuration());
        mTvFast.setText(showTime);
        mSeek = seek;
        mIsFastFinish = true;

        if (mTvFast.getVisibility() != View.VISIBLE) {
            mTvFast.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 改变声音
     */
    private void changeVolume(int flag) {
        mShowVolume += flag;
        if (mShowVolume > 100) {
            mShowVolume = 100;
        } else if (mShowVolume < 0){
            mShowVolume = 0;
        }
        mIvControl.setImageResource(R.drawable.volume_icon);
        mTvControl.setText(mShowVolume+"%");
        int tagVolume = mShowVolume * mMaxVolume / 100;
        //tagVolume:音量绝对值
        mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, tagVolume, 0);

        mHandler.removeMessages(SHOW_CENTER_CONTROL);
        mControlCenter.setVisibility(View.VISIBLE);
        mHandler.sendEmptyMessageDelayed(SHOW_CENTER_CONTROL, SHOW_CONTROL_TIME);
    }

    /**
     * 改变亮度
     */
    private void changeLightness(int flag) {
        mShowLightness += flag;
        if (mShowLightness > 255) {
            mShowLightness = 255;
        } else if (mShowLightness <= 0 ) {
            mShowLightness = 0;
        }
        mIvControl.setImageResource(R.drawable.lightness_icon);
        mTvControl.setText(mShowLightness * 100 / 255+"%");
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.screenBrightness = mShowLightness / 255f;
        getWindow().setAttributes(lp);

        mHandler.removeMessages(SHOW_CENTER_CONTROL);
        mControlCenter.setVisibility(View.VISIBLE);
        mHandler.sendEmptyMessageDelayed(SHOW_CENTER_CONTROL, SHOW_CONTROL_TIME);
    }

    /**
     * 隐藏控制条
     */
    public static void hideControlBar() {
        mControlBottom.setVisibility(View.GONE);
        mControlTop.setVisibility(View.GONE);
    }

    /**
     * 显示控制条
     */
    public static void showControlBar() {
        mControlBottom.setVisibility(View.VISIBLE);
        mControlTop.setVisibility(View.VISIBLE);
    }

    /**
     * 设置为全屏
     */
    private void setupFullScreen() {
        //设置窗口模式
        WindowManager.LayoutParams attrs = getWindow().getAttributes();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
        attrs.flags |= WindowManager.LayoutParams.FLAG_FULLSCREEN;
        getWindow().setAttributes(attrs);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        //获取屏幕尺寸
        WindowManager manager = this.getWindowManager();
        DisplayMetrics metrics = new DisplayMetrics();
        manager.getDefaultDisplay().getMetrics(metrics);

        //设置Video布局尺寸
        mVideoLayout.getLayoutParams().width = metrics.widthPixels;
        mVideoLayout.getLayoutParams().height = metrics.heightPixels;

        //设置为全屏拉伸
        mVideoView1.setVideoLayout(VideoView.VIDEO_LAYOUT_SCALE, 0);
        mIvIsFullScreen.setImageResource(R.drawable.not_fullscreen);

        mIsFullScreen = true;
    }

    /**
     * 设置为非全屏
     */
    private void setupUnFullScreen() {
        WindowManager.LayoutParams attrs = getWindow().getAttributes();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        attrs.flags &= (~WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().setAttributes(attrs);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        float width = getResources().getDisplayMetrics().heightPixels;
        float height = dp2px(200.f);
        mVideoLayout.getLayoutParams().width = (int) width;
        mVideoLayout.getLayoutParams().height = (int) height;

        //设置为全屏
        mVideoView1.setVideoLayout(VideoView.VIDEO_LAYOUT_SCALE, 0);
        mIvIsFullScreen.setImageResource(R.drawable.play_fullscreen);

        mIsFullScreen = false;
    }

    @Override
    public void onBackPressed() {
        if (mIsFullScreen) {
            setupUnFullScreen();
        } else {
            super.onBackPressed();
        }
    }

    /**
     * dp转px
     * @param dpValue
     * @return
     */
    private int dp2px(float dpValue) {
        final float scale = getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    @Override
    protected void onStop() {
        super.onStop();
        //如果还在播放，则暂停
        if (mVideoView1.isPlaying())
            mVideoView1.pause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //释放资源
        mVideoView1.stopPlayback();

        EventBus.getDefault().unregister(this);
    }
}
