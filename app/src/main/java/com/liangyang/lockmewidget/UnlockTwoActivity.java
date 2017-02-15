package com.liangyang.lockmewidget;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import java.util.List;

public class UnlockTwoActivity extends AppCompatActivity {

    private GestureLockTwoView mUnGesturelockTwo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unlock_two);

        mUnGesturelockTwo = (GestureLockTwoView) findViewById(R.id.un_lockView_two);

        SharedPreferences sb = getSharedPreferences("password",MODE_PRIVATE);
        final String password = sb.getString("password", "");

        mUnGesturelockTwo.setOnDrawFinishedListener(new GestureLockTwoView.OnDrawFinishedListener() {
            @Override
            public boolean OnDrawFinished(List<Integer> passList) {
                StringBuilder builder = new StringBuilder();
                for (Integer integer : passList) {
                    builder.append(integer);
                }
                //判断当前的密码和读取的密码是否一致
                if (builder.toString().equals(password)){
                    Toast.makeText(UnlockTwoActivity.this, "密码正确", Toast.LENGTH_SHORT).show();
                    return true;
                }else {
                    Toast.makeText(UnlockTwoActivity.this, "密码错误", Toast.LENGTH_SHORT).show();
                    return false;
                }
            }
        });

    }
}
