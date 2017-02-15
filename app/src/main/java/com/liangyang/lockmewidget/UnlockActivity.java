package com.liangyang.lockmewidget;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import java.util.List;

/**
 * 解锁界面
 */
public class UnlockActivity extends AppCompatActivity {

    private GestureLock mGestureLock;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unlock);

        //读取已经保存的密码
        SharedPreferences passwordSb = getSharedPreferences("password", this.MODE_PRIVATE);
        final String password = passwordSb.getString("password","");
        //初始化视图
        mGestureLock = (GestureLock) findViewById(R.id.un_lockView);

        mGestureLock.setOnDrawFinishedListener(new GestureLock.OnDrawFinishedListener() {
            @Override
            public boolean OnDrawFinished(List<Integer> passList) {
                StringBuilder stringBuilder = new StringBuilder();
                for (Integer integer : passList) {
                    stringBuilder.append(integer);
                }
                //判断当前的密码和读取的密码是否一致
                Log.i("androidxxx", "解锁：" + "  " + stringBuilder.toString());
                if (stringBuilder.equals(password)){
                    Toast.makeText(UnlockActivity.this, "密码正确", Toast.LENGTH_SHORT).show();
                    return true;
                }else {
                    Toast.makeText(UnlockActivity.this, "密码错误", Toast.LENGTH_SHORT).show();
                    return false;
                }
            }
        });
    }
}
