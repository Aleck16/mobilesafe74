package com.itheima.mobilesafe74.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.itheima.mobilesafe74.R;


/**
 * Created by Aleck_ on 2016/10/14.
 */
public class Setup1Activity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup1);
    }
    public void nextPage(View view){
        Intent intent =new Intent(getApplicationContext(),Setup2Activity.class);
        startActivity(intent);

        finish();

        //开启平移动画
        overridePendingTransition(R.animator.next_in_anim,R.animator.next_out_anim);
    }
}
