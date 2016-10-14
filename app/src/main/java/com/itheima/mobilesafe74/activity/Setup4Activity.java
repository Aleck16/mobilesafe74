package com.itheima.mobilesafe74.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.itheima.mobilesafe74.R;
import com.itheima.mobilesafe74.utils.ConstantValue;
import com.itheima.mobilesafe74.utils.SpUtil;

/**
 * Created by Aleck_ on 2016/10/14.
 */
public class Setup4Activity extends Activity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup4);
    }

    public void nextPage(View view){
        Intent intent =new Intent(getApplicationContext(),SetupOverActivity.class);
        startActivity(intent);

        finish();

        SpUtil.putBoolean(this, ConstantValue.SETUP_OVER,true);
    }

    public void prePage(View view){
        Intent intent =new Intent(getApplicationContext(),Setup3Activity.class);
        startActivity(intent);

        finish();
    }
}
