package com.aiwac.cilentapp.patrobot.activity.loginandregister;

import android.content.Intent;
import android.content.SharedPreferences;
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
import com.aiwac.robotapp.commonlibrary.task.ThreadPoolManager;
import com.aiwac.robotapp.commonlibrary.utils.LogUtil;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginByPasswordActivity extends AppCompatActivity {

    private Button loginBtn;
    private EditText loginPassword;
    private AutoCompleteTextView numberEdit;
    private TextView textToRegister;
    private String phoneNumber;
    private TextView tvToLoginByCode;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_by_password);
        initView();
        initEvent();
    }


    private void initView(){
        numberEdit = (AutoCompleteTextView) findViewById(R.id.change_password_number_edit);
        loginBtn = (Button) findViewById(R.id.login_bypassword_button);
        loginPassword=findViewById(R.id.login_password_edit);


        textToRegister = findViewById(R.id.btn_to_register);
        textToRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent toRegisterIntent=new Intent(LoginByPasswordActivity.this,RegisterCodeActivity.class);
                //ActivityUtil.skipActivity(LoginActivity.this,RegisterCodeActivity.class,true);
                startActivity(toRegisterIntent);
                finish();
            }
        });
        tvToLoginByCode = findViewById(R.id.tv_code_login);
        tvToLoginByCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginByPasswordActivity.this,LoginActivity.class));
                finish();
            }
        });

    }
    private void initEvent(){

        //用户登录
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ThreadPoolManager.getThreadPoolManager().submitTask(new Runnable() {
                    @Override
                    public void run() {
                        phoneNumber=numberEdit.getText().toString().trim();
                        String password = loginPassword.getText().toString().trim();
                        LogUtil.d(password + " : " + phoneNumber);
                        Message message = new Message();
                        if(phoneNumber != null && password != null){
                            JSONObject root = new JSONObject();
                            try {
                                root.put(Constant.USER_REGISTER_NUMBER, phoneNumber);
                                root.put(Constant.USER_DATA_FIELD_PASSWORD, password);
                                LogUtil.d(Constant.JSON_GENERATE_SUCCESS + root.toString());
                                String resultJson = HttpUtil.requestPostJson(Constant.HTTP_USER_LOGIN_BY_PASSWORD_URL, root.toString());
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
                                        editor.putString(Constant.USER_DATA_FIELD_PASSWORD,password);
                                        editor.putLong(Constant.USER_DATA_FIELD_TOKENTIME, System.currentTimeMillis());
                                        editor.apply();

                                        UserData userData = UserData.getUserData();
                                        userData.setNumber(phoneNumber);
                                        userData.setClientID(clientId);
                                        userData.setPassword(password);

                                        message.what = Constant.USER_CHECKCODE_SUCCESS;
                                        LogUtil.d(Constant.USER_LOGIN_SUCCEED);


                                    }else{
                                        message.what = Constant.USER_CHECKCODE_ERROR_EXCEPTION;
                                        LogUtil.d(Constant.USER_CHECKCODE_ERROR_EXCEPTION_MESSAGE);
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
    }

    //处理获取验证码handler
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {

            switch (msg.what){
                case Constant.USER_JSON_EXCEPTION:
                    Toast.makeText(LoginByPasswordActivity.this, Constant.USER_JSON_EXCEPTION_MESSAGE, Toast.LENGTH_LONG).show();
                    break;
                case Constant.USER_CHECKCODE_ERROR_EXCEPTION:
                    Toast.makeText(LoginByPasswordActivity.this, Constant.USER_CHECKCODE_ERROR_EXCEPTION_MESSAGE, Toast.LENGTH_LONG).show();
                    break;
                case Constant.USER_CHECKCODE_SUCCESS:
                    //直接跳转到主函数
                    startActivity(new Intent(LoginByPasswordActivity.this, MainActivity.class));


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
