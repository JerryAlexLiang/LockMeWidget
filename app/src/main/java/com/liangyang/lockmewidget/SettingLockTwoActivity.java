package com.liangyang.lockmewidget;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.List;

public class SettingLockTwoActivity extends AppCompatActivity implements View.OnClickListener {

    private GestureLockTwoView mGesturelockTwo;
    private Button mResetTwoBtn;
    private Button mSaveTwoBtn;

    //添加类的变量用于保存密码
    List<Integer> passList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_lock_two);

        mGesturelockTwo = (GestureLockTwoView) findViewById(R.id.lockView_two);
        mResetTwoBtn = (Button) findViewById(R.id.reset_lock_two);
        mSaveTwoBtn = (Button) findViewById(R.id.save_lock_two);

        //设置点击监听事件
        mResetTwoBtn.setOnClickListener(this);
        mSaveTwoBtn.setOnClickListener(this);

        /**
         * 绘制完成的时候的监听事件
         */
        mGesturelockTwo.setOnDrawFinishedListener(new GestureLockTwoView.OnDrawFinishedListener() {
            @Override
            public boolean OnDrawFinished(List<Integer> passList) {
                if (passList.size()<3){
                    Toast.makeText(SettingLockTwoActivity.this, "密码设置不能少于三个点", Toast.LENGTH_SHORT).show();
                    return false;
                }else {
                    //将密码(点)passList保存下来
                    SettingLockTwoActivity.this.passList = passList;
                    return true;
                }
            }
        });



    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.reset_lock_two:
                mGesturelockTwo.resetPoints();
                break;

            case R.id.save_lock_two:
                if (passList != null){
                    StringBuilder sb = new StringBuilder();
                    for (Integer integer : passList) {
                        sb.append(integer);
                    }
                    //将字符串保存下来
                    SharedPreferences sharedPreferences = getSharedPreferences("password",MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("password",sharedPreferences.toString());
                    editor.commit();
                    Toast.makeText(SettingLockTwoActivity.this, "密码创建完成", Toast.LENGTH_SHORT).show();
                    //跳转检验页面
                    Intent intent = new Intent(SettingLockTwoActivity.this,UnlockTwoActivity.class);
                    startActivity(intent);
                }else {
                    Toast.makeText(SettingLockTwoActivity.this, "请绘制手势密码", Toast.LENGTH_SHORT).show();
                }
                break;
        }

    }
}
