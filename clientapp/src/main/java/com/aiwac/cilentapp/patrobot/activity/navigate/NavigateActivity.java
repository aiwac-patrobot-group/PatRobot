package com.aiwac.cilentapp.patrobot.activity.navigate;

import android.content.DialogInterface;
import android.database.DataSetObserver;
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
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aiwac.cilentapp.patrobot.R;
import com.aiwac.cilentapp.patrobot.activity.feed.FeedActivity;
import com.aiwac.cilentapp.patrobot.server.WebSocketApplication;
import com.aiwac.cilentapp.patrobot.utils.BasisTimesUtils;
import com.aiwac.cilentapp.patrobot.utils.JsonUtil;
import com.aiwac.robotapp.commonlibrary.task.ThreadPoolManager;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class NavigateActivity extends AppCompatActivity {

    private Button startTime_bt;
    private Button endTime_bt;
    private Button addTime_bt;
    private String[] add = {"巡航时间"};
    private TextView sTime;
    private TextView eTime;

    public final static ArrayList<String> navigateTimes = new ArrayList();
    private String setStartTime = "";
    private String setEndTime = "";
    private String startTime1 = "9:0";
    private String endTIme1 = "9:0";
    ExpandableListView mView;
    //private int navigateNum = 0;
    //获得制定组的位置、指定子列表项处的字列表项数据

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigate);
        mView = (ExpandableListView) findViewById(R.id.el_list);
        mView.setAdapter(new MyAdapter());
        startTime_bt = (Button)findViewById(R.id.startTime);
        endTime_bt = (Button)findViewById(R.id.endTime);
        addTime_bt = (Button)findViewById(R.id.addNavigateTime);
        sTime = (TextView)findViewById(R.id.showStartTime);
        eTime = (TextView)findViewById(R.id.showEndTime);
        startTime_bt.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                String tempTime = showTimerPicker("start");
                if(!tempTime.equals("")){
                    startTime1 = tempTime;
                }

            }
        });
        endTime_bt.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                String tempTime = showTimerPicker("end");
                if(!tempTime.equals("")){
                    endTIme1 = tempTime;
                }

            }
        });
        addTime_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navigateTimes.add(sTime.getText()+"-"+eTime.getText());
                ThreadPoolManager.getThreadPoolManager().submitTask(new Runnable() {
                    @Override
                    public void run() {
                        try{
                            WebSocketApplication.getWebSocketApplication().send( JsonUtil.feedTransform2Json(navigateTimes.toArray(new String[navigateTimes.size()])));
                        }catch (Exception e){
                            e.printStackTrace();
                            Log.d("tag", "endTime Navigate exception");
                        }
                    }
                });
                sTime.setText("9:00");
                eTime.setText("9:00");
            }
        });

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
                    AlertDialog.Builder builder=new AlertDialog.Builder(NavigateActivity.this);
                    builder.setMessage("确定删除?");
                    builder.setTitle("提示");
                    builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            navigateTimes.remove(childPosition);
                            ThreadPoolManager.getThreadPoolManager().submitTask(new Runnable() {
                                @Override
                                public void run() {
                                    try{
                                        WebSocketApplication.getWebSocketApplication().send( JsonUtil.feedTransform2Json(navigateTimes.toArray(new String[navigateTimes.size()])));
                                    }catch (Exception e){
                                        e.printStackTrace();
                                        Log.d("tag", "endTime Navigate exception");
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

    }

    class MyAdapter extends BaseExpandableListAdapter {

        //自己定义一个获得textview的方法
        TextView getTextView() {
            AbsListView.LayoutParams lp = new AbsListView.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, 100);
            TextView textView = new TextView(NavigateActivity.this);
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
            return navigateTimes.size();
        }

        @Override
        public Object getGroup(int groupPosition) {
            return add[groupPosition];
        }

        @Override
        public Object getChild(int groupPosition, int childPosition) {
            return navigateTimes.get(childPosition);
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
            LinearLayout ll = new LinearLayout(NavigateActivity.this);
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
            LinearLayout ll = new LinearLayout(NavigateActivity.this);
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

    private String showTimerPicker(final String startEnd) {

        BasisTimesUtils.showTimerPickerDialog(NavigateActivity.this, true, "请选择时间", 9, 0, true, new BasisTimesUtils.OnTimerPickerListener() {
            @Override
            public void onConfirm(int hourOfDay, int minute) {
                //toast(hourOfDay + ":" + minute);
                if(startEnd.equals("start")){
                    setStartTime = String.valueOf(hourOfDay)+":"+String.valueOf(minute);
                    sTime.setText(setStartTime);

                }else if(startEnd.equals("end")){
                    setEndTime = String.valueOf(hourOfDay)+":"+String.valueOf(minute);
                    eTime.setText(setEndTime);

                }
            }

            @Override
            public void onCancel() {
                if(startEnd.equals("start")){
                    setStartTime = "9:0";
                }else if(startEnd.equals("end")){
                    setEndTime = "9:0";
                };
            }
        });
        if(startEnd.equals("start")){
            return setStartTime;
        }else if(startEnd.equals("end")){
            return setEndTime;
        }
        return "";
    }

}
