package com.liangyang.lockmewidget;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button mSettingBtn;
    private Button mUnlockBtn;
    private Button mSettingTwoBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //初始化视图
        mSettingBtn = (Button) findViewById(R.id.mSettingBtn);
        mSettingTwoBtn = (Button) findViewById(R.id.mSettingTwoBtn);
        mUnlockBtn = (Button) findViewById(R.id.mUnlockBtn);

        //设置监听事件
        mSettingBtn.setOnClickListener(this);
        mSettingTwoBtn.setOnClickListener(this);
        mUnlockBtn.setOnClickListener(this);
    }

    /**
     * 监听事件
     *
     * @param view
     */
    @Override
    public void onClick(View view) {
        Intent intent = new Intent();
        switch (view.getId()) {
            case R.id.mSettingBtn:
                intent.setClass(MainActivity.this,SettingLockActivity.class);
                break;

            case R.id.mSettingTwoBtn:
                intent.setClass(MainActivity.this,SettingLockTwoActivity.class);
                break;

            case R.id.mUnlockBtn:
                intent.setClass(MainActivity.this,UnlockActivity.class);
                break;
        }
        startActivity(intent);
    }
}
