package com.aiwac.cilentapp.patrobot.activity.loginandregister;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.aiwac.cilentapp.patrobot.R;
import com.aiwac.cilentapp.patrobot.utils.ActivityUtil;

public class RegisterCodeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_code);

        TextView toLoginTextView = findViewById(R.id.btn_to_login);
        toLoginTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent toLoginIntent = new Intent(RegisterCodeActivity.this,LoginActivity.class);
                //ActivityUtil.skipActivity(RegisterCodeActivity.this,LoginActivity.class,true);
                startActivity(toLoginIntent);
                finish();
            }
        });

    }
}
