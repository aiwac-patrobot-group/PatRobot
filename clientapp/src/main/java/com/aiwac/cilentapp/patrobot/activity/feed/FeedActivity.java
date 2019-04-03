package com.aiwac.cilentapp.patrobot.activity.feed;

import android.content.DialogInterface;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.aiwac.cilentapp.patrobot.R;
import com.aiwac.cilentapp.patrobot.server.WebSocketApplication;
import com.aiwac.cilentapp.patrobot.utils.BasisTimesUtils;
import com.aiwac.cilentapp.patrobot.utils.JsonUtil;
import com.aiwac.robotapp.commonlibrary.bean.MessageEvent;
import com.aiwac.robotapp.commonlibrary.common.Constant;
import com.aiwac.robotapp.commonlibrary.task.ThreadPoolManager;
import com.aiwac.robotapp.commonlibrary.utils.LogUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;


import java.text.DecimalFormat;
import java.util.ArrayList;

public class FeedActivity extends AppCompatActivity {
    private Button addTime_bt;
    //private String[] add = {"投食时间"};
    //投食时间最多不超过10个
    //private String[][] times = new String[1][];
    private String setTime = "";
    private int feedNum = 0;
    ExpandableListView mView;



    //设置组视图的图片
//        int[] logos = new int[] { R.drawable.wei, R.drawable.shu,R.drawable.wu};
    //设置组视图的显示文字
    private String[] add = new String[] { "投食时间"};
    //子视图显示文字
    public final static ArrayList<String> feedTimes = new ArrayList();
    //获得制定组的位置、指定子列表项处的字列表项数据

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_feed);
        mView = (ExpandableListView) findViewById(R.id.el_list);
        mView.setAdapter(new MyAdapter());
        addTime_bt = (Button)findViewById(R.id.addFeedTime);
        addTime_bt.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                String tempTime = showTimerPicker();

            }
        });


        //注册消息
        EventBus.getDefault().register(this);

        mView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener()
        {
            public boolean onItemLongClick(AdapterView<?> parent, View childView, int flatPos, long id)
            {
                if (ExpandableListView.getPackedPositionType(id) == ExpandableListView.PACKED_POSITION_TYPE_CHILD)
                {
                    long packedPos = ((ExpandableListView) parent).getExpandableListPosition(flatPos);
                    int groupPosition = ExpandableListView.getPackedPositionGroup(packedPos);
                    final int childPosition = ExpandableListView.getPackedPositionChild(packedPos);
                    //定义AlertDialog.Builder对象，当长按列表项的时候弹出确认删除对话框
                    AlertDialog.Builder builder=new AlertDialog.Builder(FeedActivity.this);
                    builder.setMessage("确定删除?");
                    builder.setTitle("提示");
                    builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            feedTimes.remove(childPosition);
                            ThreadPoolManager.getThreadPoolManager().submitTask(new Runnable() {
                                @Override
                                public void run() {
                                    try{
                                        WebSocketApplication.getWebSocketApplication().send( JsonUtil.feedTransform2Json(feedTimes.toArray(new String[feedTimes.size()])));
                                    }catch (Exception e){
                                        e.printStackTrace();
                                        Log.d("tag", "FeedTransform exception");
                                    }
                                }
                            });

                        }
                    });
                    builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    });
                    builder.create().show();
                    return true;
                }

                return false;
            }

        });


        initFeedTimeFromServer();

    }


    //从服务器获取时间列表
    private void initFeedTimeFromServer(){
        ThreadPoolManager.getThreadPoolManager().submitTask(new Runnable() {
            @Override
            public void run() {
                try{
                    WebSocketApplication.getWebSocketApplication().send(JsonUtil.time2Json(Constant.WEBSOCKET_SOCKET_AUTOTYPE_AUTO_FEED));
                }catch (Exception e){
                    e.printStackTrace();
                    Log.d("tag", "FeedTransform exception");
                }
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void Event(MessageEvent messageEvent) {
        if(messageEvent.getTo().equals(Constant.WEBSOCKET_SOCKET_GET_TIME_LIST)){
            String json = messageEvent.getMessage();
            LogUtil.d("onevent"+json);
            String []timeResult=JsonUtil.parseFeedTime(json);
            for(String time:timeResult){
                feedTimes.add(time);
            }
        }

    }


    class MyAdapter extends BaseExpandableListAdapter {

        //自己定义一个获得textview的方法
        TextView getTextView() {
            AbsListView.LayoutParams lp = new AbsListView.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, 100);
            TextView textView = new TextView(FeedActivity.this);
            textView.setLayoutParams(lp);
            textView.setGravity(Gravity.CENTER_VERTICAL);
            textView.setPadding(36, 0, 0, 0);
            textView.setTextSize(20);
            textView.setTextColor(Color.BLACK);
            return textView;
        }


        @Override
        public int getGroupCount() {
            return add.length;
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            return feedTimes.size();
        }

        @Override
        public Object getGroup(int groupPosition) {
            return add[groupPosition];
        }

        @Override
        public Object getChild(int groupPosition, int childPosition) {
            return feedTimes.get(childPosition);
        }

        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

        @Override
        public View getGroupView(int groupPosition, boolean isExpanded,
                                 View convertView, ViewGroup parent) {
            LinearLayout ll = new LinearLayout(FeedActivity.this);
            ll.setOrientation(LinearLayout.VERTICAL);
//             ImageView logo = new ImageView(ExpandableList.this);
//             logo.setImageResource(logos[groupPosition]);
//             logo.setPadding(50, 0, 0, 0);
//             ll.addView(logo);
            TextView textView = getTextView();
            textView.setTextColor(Color.BLUE);
            textView.setText(getGroup(groupPosition).toString());
            ll.addView(textView);
            ll.setPadding(100, 10, 10, 10);
            return ll;
        }

        @Override
        public View getChildView(int groupPosition, int childPosition,
                                 boolean isLastChild, View convertView, ViewGroup parent) {
            LinearLayout ll = new LinearLayout(FeedActivity.this);
            ll.setOrientation(LinearLayout.VERTICAL);
//             ImageView generallogo = new ImageView(TestExpandableListView.this);
//             generallogo.setImageResource(generallogos[groupPosition][childPosition]);
//             ll.addView(generallogo);
            TextView textView = getTextView();
            textView.setText(getChild(groupPosition, childPosition).toString());
            ll.addView(textView);
            return ll;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }

    }


    private String showTimerPicker() {

        BasisTimesUtils.showTimerPickerDialog(FeedActivity.this, true, "请选择时间", 9, 0, true, new BasisTimesUtils.OnTimerPickerListener() {
            @Override
            public void onConfirm(int hourOfDay, int minute) {
                //toast(hourOfDay + ":" + minute);
                //feedNum++;
                DecimalFormat df=new DecimalFormat("00");//设置格式
                setTime = df.format(hourOfDay)+":"+df.format(minute);
                feedTimes.add(setTime);
                //发送给服务器
                sendTimeToServer(setTime);
            }

            @Override
            public void onCancel() {
                setTime = "";
            }
        });
        return setTime;
    }


    private void sendTimeToServer(String tempTime){

        if(!tempTime.equals("")){
            ThreadPoolManager.getThreadPoolManager().submitTask(new Runnable() {
                @Override
                public void run() {
                    try{
                        String time[]=feedTimes.toArray(new String[feedTimes.size()]);
                        WebSocketApplication.getWebSocketApplication().send( JsonUtil.feedTransform2Json(time));
                    }catch (Exception e){
                        e.printStackTrace();
                        Log.d("tag", "FeedTransform exception");
                    }
                }
            });
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        feedTimes.clear();
    }

}



