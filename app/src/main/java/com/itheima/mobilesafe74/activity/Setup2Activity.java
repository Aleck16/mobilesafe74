package com.itheima.mobilesafe74.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.View;

import com.itheima.mobilesafe74.R;
import com.itheima.mobilesafe74.utils.ConstantValue;
import com.itheima.mobilesafe74.utils.SpUtil;
import com.itheima.mobilesafe74.view.SettingItemView;

/**
 * Created by Aleck_ on 2016/10/14.
 */
public class Setup2Activity extends Activity {

    private SettingItemView siv_sim_bound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup2);

        initUI();
    }

    private void initUI() {
        siv_sim_bound = (SettingItemView) findViewById(R.id.siv_sim_bound);
        //1,回显（读取已有的绑定状态，用作显示，sp中是否存储了sim卡的序列号）
        String sim_number = SpUtil.getString(this, ConstantValue.SIM_NUMBER, "");
        //2,判断是否序列号为""
        if (TextUtils.isEmpty(sim_number)) {
            siv_sim_bound.setCheck(false);
        } else {
            siv_sim_bound.setCheck(true);
        }

        siv_sim_bound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //3,获取原有的状态
                boolean isCheck = siv_sim_bound.isCheck();
                //4,将原有的状态取反，状态设置给当前条目
                // 5，存储(序列卡号)，设置给当前条目
                siv_sim_bound.setCheck(!isCheck);
                if (!isCheck) {
                    //6，存储（序列号）
                    //6.1，获取对应的sim卡序列号TelephoneManager
                    TelephonyManager manager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
                    //6.2,获取sim卡的序列号
                    String simSerialNumber = manager.getSimSerialNumber();
                    //6.3存储
                    SpUtil.putString(getApplicationContext(), ConstantValue.SIM_NUMBER, simSerialNumber);

                } else {
                    //7,将存储序列卡号的节点从sp中删除
                    SpUtil.remove(getApplicationContext(), ConstantValue.SIM_NUMBER);
                }

            }
        });
    }

    public void nextPage(View view) {
        Intent intent = new Intent(getApplicationContext(), Setup3Activity.class);
        startActivity(intent);

        finish();
    }

    public void prePage(View view) {
        Intent intent = new Intent(getApplicationContext(), Setup1Activity.class);
        startActivity(intent);

        finish();
    }
}
