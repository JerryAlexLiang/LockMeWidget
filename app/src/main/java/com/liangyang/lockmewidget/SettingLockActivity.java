package com.liangyang.lockmewidget;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.List;

/**
 * 创建图案手势密码
 */
public class SettingLockActivity extends AppCompatActivity implements View.OnClickListener {

    private GestureLock mGestureLock;
    private Button mResetBtn;
    private Button mSaveLockBtn;
    //添加类的变量用于保存密码
    List<Integer> passList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_lock);

        //初始化视图
        mGestureLock = (GestureLock) findViewById(R.id.lockView);
        mResetBtn = (Button) findViewById(R.id.reset_lock);
        mSaveLockBtn = (Button) findViewById(R.id.save_lock);

        //设置点击监听事件
        mResetBtn.setOnClickListener(this);
        mSaveLockBtn.setOnClickListener(this);

        /**
         * 绘制完成的时候的监听事件
         */
        mGestureLock.setOnDrawFinishedListener(new GestureLock.OnDrawFinishedListener() {
            @Override
            public boolean OnDrawFinished(List<Integer> passList) {
                if (passList.size() < 3) {
                    Toast.makeText(SettingLockActivity.this, "密码设置不能少于三个点", Toast.LENGTH_SHORT).show();
                    return false;//当前的设置是错误的
                } else {
                    //将密码(点)passList保存下来
                    SettingLockActivity.this.passList = passList;
                    return true;
                }
            }
        });
    }

    /**
     * 点击事件的监听
     *
     * @param view
     */
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.reset_lock:
                mGestureLock.resetPoints();
                break;

            case R.id.save_lock:
                if (passList != null){
                    StringBuilder stringBuilder = new StringBuilder();
                    for (Integer integer : passList) {
                        stringBuilder.append(integer);
                    }
                    //将字符串保存下来
                    SharedPreferences sharedPreferences = SettingLockActivity.this.getSharedPreferences("password", SettingLockActivity.this.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("password",sharedPreferences.toString());
                    editor.commit();
                    Toast.makeText(SettingLockActivity.this, "密码创建完成", Toast.LENGTH_SHORT).show();
                    //跳转检验页面
                    Intent intent = new Intent(SettingLockActivity.this,UnlockActivity.class);
                    startActivity(intent);
                }else {
                    Toast.makeText(SettingLockActivity.this, "请绘制手势密码", Toast.LENGTH_SHORT).show();
                }
                break;
        }


    }
}
