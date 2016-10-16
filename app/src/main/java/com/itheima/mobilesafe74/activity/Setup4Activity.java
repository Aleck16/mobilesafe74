package com.itheima.mobilesafe74.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.itheima.mobilesafe74.R;
import com.itheima.mobilesafe74.utils.ConstantValue;
import com.itheima.mobilesafe74.utils.SpUtil;
import com.itheima.mobilesafe74.utils.ToastUtil;

/**
 * Created by Aleck_ on 2016/10/14.
 */
public class Setup4Activity extends Activity {

    private CheckBox cb_box;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup4);

        initUI();
    }

    private void initUI() {
        cb_box = (CheckBox) findViewById(R.id.cb_box);
        //1，是否选中状态的回显过程
        boolean open_security = SpUtil.getBoolean(this, ConstantValue.OPEN_SECURITY, false);
        //2，根据状态修改CheckBox后续的文字显示
        cb_box.setChecked(open_security);
        if (open_security) {
            cb_box.setText("安全设置已开启");
        } else {
            cb_box.setText("安全设置已关闭");
        }
        //3，点击过程中，监听选中状态发生改变的过程。
        cb_box.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                //4，b:点击后的状态,存储点击后的状态
                SpUtil.putBoolean(getApplicationContext(), ConstantValue.OPEN_SECURITY, b);
                //5，根据开启还是关闭的状态去修改显示的文字
                if (b) {
                    cb_box.setText("安全设置已开启");
                } else {
                    cb_box.setText("安全设置已关闭");
                }
            }
        });

    }

    public void nextPage(View view) {
        boolean open_security = SpUtil.getBoolean(this, ConstantValue.OPEN_SECURITY, false);
        if(open_security){
            Intent intent = new Intent(getApplicationContext(), SetupOverActivity.class);
            startActivity(intent);

            finish();

            SpUtil.putBoolean(this, ConstantValue.SETUP_OVER, true);

            //开启平移动画
            overridePendingTransition(R.animator.next_in_anim,R.animator.next_out_anim);
        }else{
            ToastUtil.show(this,"请开启防盗保护");
        }
    }

    public void prePage(View view) {
        Intent intent = new Intent(getApplicationContext(), Setup3Activity.class);
        startActivity(intent);

        finish();

        //开启平移动画
        overridePendingTransition(R.animator.pre_in_anim,R.animator.pre_out_anim);
    }
}
