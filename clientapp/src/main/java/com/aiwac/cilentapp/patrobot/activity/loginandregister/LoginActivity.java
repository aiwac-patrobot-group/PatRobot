package com.aiwac.cilentapp.patrobot.activity.loginandregister;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.aiwac.cilentapp.patrobot.R;
import com.aiwac.cilentapp.patrobot.activity.MainActivity;
import com.aiwac.cilentapp.patrobot.database.UserData;
import com.aiwac.cilentapp.patrobot.utils.HttpUtil;
import com.aiwac.cilentapp.patrobot.utils.JsonUtil;
import com.aiwac.robotapp.commonlibrary.common.Constant;
import com.aiwac.robotapp.commonlibrary.exception.HttpException;
import com.aiwac.robotapp.commonlibrary.exception.JsonException;
import com.aiwac.robotapp.commonlibrary.task.ThreadPoolManager;
import com.aiwac.robotapp.commonlibrary.utils.LogUtil;
import com.aiwac.robotapp.commonlibrary.utils.StringUtil;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {

    private Button checkcodeBtn;
    private Button loginBtn;
    private String errorDesc="";
    private AutoCompleteTextView numberEdit;
    private EditText checkcodeEidt;
    private TextView textToRegister;
    private String phoneNumber;
    private TextView tvToLoginByPassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
        initEvent();




    }
    private void initView(){
        numberEdit = (AutoCompleteTextView) findViewById(R.id.change_password_number_edit);
        checkcodeEidt = (EditText) findViewById(R.id.change_password_checkcode_edit);
        checkcodeBtn = (Button) findViewById(R.id.register_check_code_button);
        loginBtn = (Button) findViewById(R.id.login_bycode_button);

        textToRegister = findViewById(R.id.btn_to_register);
        textToRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent toRegisterIntent=new Intent(LoginActivity.this,RegisterCodeActivity.class);
                //ActivityUtil.skipActivity(LoginActivity.this,RegisterCodeActivity.class,true);
                startActivity(toRegisterIntent);
                finish();
            }
        });
        tvToLoginByPassword = findViewById(R.id.tv_password_login);
        tvToLoginByPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this,LoginByPasswordActivity.class));
                finish();
            }
        });

    }
    private void initEvent(){
        checkcodeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String number = numberEdit.getText().toString().trim();
                if(StringUtil.isNumber(number)) {
                    phoneNumber = number;

                    //获取验证码
                    ThreadPoolManager.getThreadPoolManager().submitTask(new Runnable() {
                        @Override
                        public void run() {
                            Message message = handler.obtainMessage();
                            try{
                                //zyt修改
                                JSONObject root = new JSONObject();
                                root.put(Constant.USER_REGISTER_NUMBER, phoneNumber);
                                LogUtil.d(Constant.JSON_GENERATE_SUCCESS + root.toString());
                                String resultJson = HttpUtil.requestPostJson(Constant.HTTP_CHECKCODE_URL, root.toString());
                                LogUtil.d("resultJson : " + resultJson);
                                String resultCode=JsonUtil.parseErrorCode(resultJson);
                                if((resultCode != null) && resultCode.equals(Constant.RETURN_JSON_ERRORCODE_VALUE_SUCCEED)) {
                                    message.what = Constant.USER_GET_CHECKCODE;
                                }else{
                                    message.what = Constant.USER_GET_CHECKCODE_EXCEPTION;
                                    LogUtil.d(Constant.USER_GET_CHECKCODE_EXCEPTION_MESSAGE);
                                }

                            }catch (Exception e){
                                e.printStackTrace();
                                if(e instanceof HttpException) {
                                    message.what = Constant.USER_GET_CHECKCODE_EXCEPTION;
                                    LogUtil.d(Constant.USER_GET_CHECKCODE_EXCEPTION_MESSAGE);
                                }else if(e instanceof JsonException){
                                    message.what = Constant.USER_JSON_EXCEPTION;
                                    LogUtil.d(Constant.JSON_EXCEPTION);
                                }else{
                                    message.what = Constant.USER_GET_CHECKCODE_EXCEPTION;
                                    LogUtil.d(Constant.USER_UNKNOW_EXCEPTION);
                                }
                            }finally {
                                handler.sendMessage(message);
                            }
                        }
                    });

                    checkcodeEidt.requestFocus(); //输入验证码文本框聚焦
                }else {
                    LogUtil.d(Constant.USER_INPUT_NUMBER_ERROR);
                    numberEdit.setError(Constant.USER_INPUT_NUMBER_ERROR);
                    Toast.makeText(LoginActivity.this, Constant.USER_INPUT_NUMBER_ERROR, Toast.LENGTH_SHORT).show();
                    numberEdit.setFocusable(true);
                    numberEdit.setFocusableInTouchMode(true);
                    numberEdit.requestFocus();
                }
            }
        });

        //用户登录
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ThreadPoolManager.getThreadPoolManager().submitTask(new Runnable() {
                    @Override
                    public void run() {
                        String checkcode = checkcodeEidt.getText().toString().trim();
                        LogUtil.d(checkcode + " : " + phoneNumber);
                        Message message = new Message();
                        if(phoneNumber != null && checkcode != null){
                            JSONObject root = new JSONObject();
                            try {
                                root.put(Constant.USER_REGISTER_NUMBER, phoneNumber);
                                root.put(Constant.USER_REGISTER_CHECKCODE, checkcode);
                                LogUtil.d(Constant.JSON_GENERATE_SUCCESS + root.toString());
                                String resultJson = HttpUtil.requestPostJson(Constant.HTTP_USER_LOGIN_IDENTIFYCODE_BASEURL, root.toString());
                                LogUtil.d("resultJson : " + resultJson);
                                if(resultJson != null) {
                                    String errorCode = JsonUtil.parseErrorCode(resultJson);
                                    if(errorCode.equals(Constant.RETURN_JSON_ERRORCODE_VALUE_SUCCEED)){
                                        //登录成功
                                        String token =JsonUtil.parseToken(resultJson);
                                        String clientId=JsonUtil.parseClientID(resultJson);

                                        SharedPreferences.Editor editor = getSharedPreferences(Constant.DB_USER_TABLENAME, MODE_PRIVATE).edit();
                                        editor.putString(Constant.WEBSOCKET_MESSAGE_CLIENTID,clientId);
                                        editor.putString(Constant.USER_DATA_FIELD_TOKEN, token);
                                        editor.putString(Constant.USER_REGISTER_NUMBER,phoneNumber);
                                        editor.putLong(Constant.USER_DATA_FIELD_TOKENTIME, System.currentTimeMillis());
                                        editor.apply();

                                        UserData userData = UserData.getUserData();
                                        userData.setNumber(phoneNumber);
                                        userData.setClientID(clientId);

                                        message.what = Constant.USER_CHECKCODE_SUCCESS;
                                        LogUtil.d(Constant.USER_LOGIN_SUCCEED);


                                    }else{
                                        message.what = Constant.USER_CHECKCODE_ERROR_EXCEPTION;
                                        LogUtil.d(Constant.USER_CHECKCODE_ERROR_EXCEPTION_MESSAGE);

                                        errorDesc=JsonUtil.parseErrorDesc(resultJson);
                                    }

                                }else{
                                    message.what = Constant.USER_GET_CHECKCODE_EXCEPTION;
                                    LogUtil.d(Constant.USER_GET_CHECKCODE_EXCEPTION_MESSAGE);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                message.what = Constant.USER_JSON_EXCEPTION;
                                LogUtil.d(Constant.JSON_EXCEPTION);
                            }finally {
                                handler.sendMessage(message);
                            }

                        }
                    }
                });
            }
        });


        //返回键
        findViewById(R.id.backButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    //处理获取验证码handler
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {

            switch (msg.what){
                case Constant.USER_GET_CHECKCODE:
                    loginBtn.setClickable(true);
                    loginBtn.setEnabled(true);
                    checkcodeBtn.setClickable(false);
                    checkcodeBtn.setEnabled(false);
                    /* 倒计时60秒，一次1秒  在此期间不能在此点击获取验证码按钮 */
                    CountDownTimer timer = new CountDownTimer(Constant.USER_CHECKCODE_MILLISINFUTURE, Constant.USER_CHECKCODE_COUNTDOWNINTERVAL) {
                        @Override
                        public void onTick(long millisUntilFinished) {
                            checkcodeBtn.setText(StringUtil.joinButtonText(checkcodeBtn.getText().toString(), millisUntilFinished/1000));
                        }

                        @Override
                        public void onFinish() {
                            checkcodeBtn.setText(StringUtil.joinButtonText(checkcodeBtn.getText().toString(), 0));
                            checkcodeBtn.setClickable(true);
                            checkcodeBtn.setEnabled(true);
                        }
                    }.start();

                    break;
                case Constant.USER_GET_CHECKCODE_EXCEPTION:
                    Toast.makeText(LoginActivity.this, Constant.USER_GET_CHECKCODE_EXCEPTION_MESSAGE, Toast.LENGTH_LONG).show();
                    break;
                case Constant.USER_JSON_EXCEPTION:
                    Toast.makeText(LoginActivity.this, Constant.USER_JSON_EXCEPTION_MESSAGE, Toast.LENGTH_LONG).show();
                    break;
                case Constant.USER_CHECKCODE_ERROR_EXCEPTION:
                    Toast.makeText(LoginActivity.this, errorDesc, Toast.LENGTH_LONG).show();
                    break;
                case Constant.USER_CHECKCODE_SUCCESS:
                    //直接跳转到主函数
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));


                    //开启服务，创建websocket连接
                    /*Intent intent = new Intent(LoginActivity.this, WebSocketService.class);
                    intent.putExtra(Constant.SERVICE_TIMER_TYPE, Constant.SERVICE_TIMER_TYPE_WEBSOCKET);
                    startService(intent);
                    ThreadPoolManager.getThreadPoolManager().submitTask(new Runnable() {
                        @Override
                        public void run() {
                            // 检查该用户是否注册
                            UserSqliteHelper userSqliteHelper = new UserSqliteHelper(LoginActivity.this);
                            User user = userSqliteHelper.getUser(phoneNumber);
                            LogUtil.d(UserData.getUserData().getNumber());
                            User user2 = new User(UserData.getUserData().getNumber(),"123456");//没有改之前的用户数据库表代码，随机写了一个密码替代
                            LogUtil.d("user："+user);
                            if(user != null) {

                                LogUtil.d(Constant.USER_REGISTER + user.toString());
                                //判断有没有填写个人信息
                                SharedPreferences pref = getSharedPreferences(Constant.DB_USER_TABLENAME, MODE_PRIVATE);
                                Boolean isRegister = pref.getBoolean(Constant.USER_DATA_FIELD_REGISTER, false);
                                if(isRegister){
                                    //ActivityUtil.skipActivity(LoginActivity.this, MainActivity.class);
                                }else{
                                    //ActivityUtil.skipActivity(LoginActivity.this, RegisterActivity.class);
                                }

                            } else {
                                userSqliteHelper.insert(user2);
                                SharedPreferences.Editor editor = getSharedPreferences(Constant.DB_USER_TABLENAME, MODE_PRIVATE).edit();
                                editor.putString(Constant.USER_DATA_FIELD_NUMBER, phoneNumber);
                                editor.putBoolean(Constant.USER_DATA_FIELD_REGISTER, false);
                                editor.apply();
                                //跳转到填写个人信息界面
                                //ActivityUtil.skipActivity(LoginActivity.this, RegisterActivity.class, true);

                            }
                        }
                    });
*/

                    break;
                default:
                    break;
            }
        }
    };


}
