package com.itheima.mobilesafe74.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.provider.MediaStore;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;

import com.itheima.mobilesafe74.R;
import com.itheima.mobilesafe74.service.LocationService;
import com.itheima.mobilesafe74.utils.ConstantValue;
import com.itheima.mobilesafe74.utils.SpUtil;

/**
 * Created by Aleck_ on 2016/10/17.
 */
public class SmsReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        //1，判断是否开启了放到保护
        boolean open_security = SpUtil.getBoolean(context, ConstantValue.OPEN_SECURITY, false);
        if (open_security) {
            //2，获取短信内容
            Object[] objects = (Object[]) intent.getExtras().get("pdus");
            //3，循环遍历短信过程
            for (Object object : objects) {
                //4，获取短信对象
                SmsMessage sms = SmsMessage.createFromPdu((byte[]) object);
                //5,获取短信对象的信息
                String originatingAddress = sms.getOriginatingAddress();
                String messageBody = sms.getMessageBody();

                //6，是否包含播放音乐的关键字
                if (messageBody.contains("#*alarm*#")) {
                    //7，播放报警音乐（准备音乐，MediaPlayer）
                    MediaPlayer mediaPlayer = MediaPlayer.create(context, R.raw.ylzs);
                    mediaPlayer.setLooping(true);       //一直循环播放
                    mediaPlayer.start();
                }

                if(messageBody.contains("#*location*#")){
                    //8，开启获取位置的服务
                    context.startService(new Intent(context,LocationService.class));

                }
            }
        }
    }
}
