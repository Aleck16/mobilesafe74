package com.itheima.mobilesafe74.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.itheima.mobilesafe74.R;

/**
 * Created by Aleck_ on 2016/10/14.
 */
public class Setup3Activity extends Activity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup3);
    }

    public void nextPage(View view){
        Intent intent =new Intent(getApplicationContext(),Setup4Activity.class);
        startActivity(intent);

        finish();
    }

    public void prePage(View view){
        Intent intent =new Intent(getApplicationContext(),Setup2Activity.class);
        startActivity(intent);

        finish();
    }
}
